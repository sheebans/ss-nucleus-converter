package org.gooru.converter.processors.command.executor.convert;

import io.vertx.core.json.JsonObject;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gooru.converter.constants.HelperConstants;
import org.gooru.converter.constants.MessageCodeConstants;
import org.gooru.converter.infra.ConfigRegistry;
import org.gooru.converter.infra.S3Client;
import org.gooru.converter.processors.command.executor.Executor;
import org.gooru.converter.processors.command.executor.MessageResponse;
import org.gooru.converter.processors.messageProcessor.MessageContext;
import org.gooru.converter.utils.InternalHelper;
import org.gooru.converter.utils.ServerValidatorUtility;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConvertHtmlToExcelExecutor implements Executor {

  private static final Logger LOG = LoggerFactory.getLogger(ConvertHtmlToExcelExecutor.class);
  private final ConfigRegistry configRegistry = ConfigRegistry.instance();
  private final S3Client s3Client = S3Client.instance();

  private ConvertHtmlToExcelExecutor() {
  }

  @Override
  public MessageResponse execute(MessageContext messageContext) {
    final String fileLocation = configRegistry.getConverterFileLocation();
    if (fileLocation == null) {
      LOG.warn(ServerValidatorUtility.generateMessage(MessageCodeConstants.CVT001));
      ServerValidatorUtility.throwASInternalServerError();
    }
    final JsonObject requestBody = messageContext.requestBody();
    ServerValidatorUtility.reject((requestBody == null), MessageCodeConstants.CVT002, 400);
    final String htmlContent = requestBody.getString(HelperConstants.HTML);
    ServerValidatorUtility.reject((htmlContent == null), MessageCodeConstants.CVT003, 400);
    final String filename = requestBody.getString(HelperConstants.FILENAME);
    return convertHtmltoExcel(fileLocation, filename, htmlContent);
  }

  private MessageResponse convertHtmltoExcel(String fileLocation, String filename, String htmlContent) {
    File targetDir = new File(fileLocation);
    if (!targetDir.exists()) {
      targetDir.mkdirs();
    }
    if (filename == null || (filename != null && filename.isEmpty())) {
      filename = String.valueOf(System.currentTimeMillis());
    } else {
      filename = InternalHelper.replaceSpecialCharWithUnderscore(filename);
      File file = new File(fileLocation + filename + HelperConstants.FILE_EX_XLSX);
      if (file.exists()) {
        filename = filename + HelperConstants.HYPEN + System.currentTimeMillis();
      }
    }
    final String filePath = fileLocation + filename + HelperConstants.FILE_EX_XLSX;
    final String file = convertHtmlToExcel(htmlContent, filePath);
    if (file == null) {
      ServerValidatorUtility.throwASInternalServerError(ServerValidatorUtility.generateMessage(MessageCodeConstants.CVT004));
    }
    boolean isUploaded = s3Client.uploadFileS3(fileLocation, filename + HelperConstants.FILE_EX_XLSX, HelperConstants.S3_REPORT_BUCKET_NAME);
    if (!isUploaded) {
      ServerValidatorUtility.throwASInternalServerError(ServerValidatorUtility.generateMessage(MessageCodeConstants.CVT005));
    }
    JsonObject response = new JsonObject().put(HelperConstants.URL, configRegistry.getReportCdnUrl() + filename + HelperConstants.FILE_EX_XLSX);
    return new MessageResponse.Builder().setResponseBody(response).setContentTypeJson().setStatusOkay().successful().build();
  }

  private String convertHtmlToExcel(String htmlContent, String filePath) {
    try {
      XSSFWorkbook workbook = new XSSFWorkbook();
      // create excel sheet
      XSSFSheet sheet = workbook.createSheet();
      workbook.setSheetName(workbook.getSheetIndex(sheet), HelperConstants.STUDENT_DATA);

      // Set Header Font
      XSSFFont headerFont = workbook.createFont();
      headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
      headerFont.setFontHeightInPoints((short) 12);

      // Set Header Style
      CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
      headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
      headerStyle.setFont(headerFont);

      int rowCount = 0;

      Document doc = Jsoup.parse(htmlContent);

      for (Element table : doc.select(HelperConstants.TABLE)) {
        // loop through all tr of table
        for (Element row : table.select(HelperConstants.TR)) {
          // create row for each tag
          Row header = sheet.createRow(rowCount);
          // loop through all tag of tag

          Elements ths = row.select(HelperConstants.TH);
          int count = 0;
          for (Element element : ths) {
            Cell cell = header.createCell(count);
            sheet.setColumnWidth(count, 15 * 256);
            // set header style
            cell.setCellValue(element.text());
            cell.setCellStyle(headerStyle);
            count++;
          }
          // now loop through all td tag
          Elements tds = row.select(HelperConstants.TD);
          count = 0;
          for (Element element : tds) {
            // create cell for each tag
            Cell cell = header.createCell(count);
            String bgStyle = element.attr(HelperConstants.STYLE);
            if (bgStyle != null && !bgStyle.isEmpty() && bgStyle.contains("#")) {
              cell.setCellStyle(getCellStyle(workbook.createCellStyle(), bgStyle.replaceAll(HelperConstants.BACKGROUND_COLOR, "")));
            }
            cell.setCellValue(element.text());
            count++;
          }
          rowCount++;
        }
      }
      File file = new File(filePath);
      String path = file.getAbsolutePath();
      FileOutputStream out = null;
      try {
        out = new FileOutputStream(new File(path));
      } catch (FileNotFoundException e) {
        filePath = null;
        LOG.error("file not found on server ", e);
      }
      try {
        workbook.write(out);
      } catch (IOException e) {
        filePath = null;
        LOG.error("error in the writing to the file ", e);
      } finally {
        try {
          workbook.close();
          out.close();
        } catch (Exception e) {
          filePath = null;
          LOG.error("error in the file output ", e);
        }
      }
    } catch (Exception e) {
      LOG.error("something went wrong while converting xlsx", e);
    }
    return filePath;
  }

  private XSSFCellStyle getCellStyle(XSSFCellStyle cellStyle, String hexa) {
    Color c =
        new Color(Integer.valueOf(hexa.substring(1, 3), 16), Integer.valueOf(hexa.substring(3, 5), 16), Integer.valueOf(hexa.substring(5, 7), 16));
    byte[] rgb = new byte[3];
    rgb[0] = (byte) c.getRed();
    rgb[1] = (byte) c.getGreen();
    rgb[2] = (byte) c.getBlue();
    cellStyle.setFillForegroundColor(new XSSFColor(rgb));
    cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
    cellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    return cellStyle;
  }

  public static final Executor getInstance() {
    return Holder.INSTANCE;
  }

  private static final class Holder {
    private static final ConvertHtmlToExcelExecutor INSTANCE = new ConvertHtmlToExcelExecutor();
  }

}
