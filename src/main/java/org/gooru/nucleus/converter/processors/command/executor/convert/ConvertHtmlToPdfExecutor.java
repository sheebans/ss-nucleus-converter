package org.gooru.nucleus.converter.processors.command.executor.convert;

import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.FileOutputStream;

import org.gooru.nucleus.converter.constants.ConfigConstants;
import org.gooru.nucleus.converter.constants.HelperConstants;
import org.gooru.nucleus.converter.constants.MessageCodeConstants;
import org.gooru.nucleus.converter.infra.ConfigRegistry;
import org.gooru.nucleus.converter.infra.S3Client;
import org.gooru.nucleus.converter.processors.command.executor.Executor;
import org.gooru.nucleus.converter.processors.command.executor.MessageResponse;
import org.gooru.nucleus.converter.processors.messageProcessor.MessageContext;
import org.gooru.nucleus.converter.utils.InternalHelper;
import org.gooru.nucleus.converter.utils.ServerValidatorUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConvertHtmlToPdfExecutor implements Executor {

  private static final Logger LOG = LoggerFactory.getLogger(ConvertHtmlToPdfExecutor.class);

  private ConfigRegistry configRegistry = ConfigRegistry.instance();
  private S3Client s3Client = S3Client.instance();

  private ConvertHtmlToPdfExecutor() {
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
    return convertHtmlToPdf(fileLocation, filename, htmlContent);
  }

  private MessageResponse convertHtmlToPdf(String fileLocation, String filename, String htmlContent) {
    File targetDir = new File(fileLocation);
    if (!targetDir.exists()) {
      targetDir.mkdirs();
    }
    if (filename == null) {
      filename = String.valueOf(System.currentTimeMillis());
    } else {
      filename = InternalHelper.replaceSpecialCharWithUnderscore(filename) + HelperConstants.HYPEN + System.currentTimeMillis();
    }
    final String htmlFilename = fileLocation + filename + HelperConstants.FILE_EX_HTML;
    saveAsHtml(htmlFilename, htmlContent);
    convertHtmlToPdf(htmlFilename, fileLocation + filename + HelperConstants.FILE_EX_PDF, 0);
    File file = new File(htmlFilename);
    file.delete();
    boolean isUploaded = s3Client.uploadFileS3(fileLocation, filename + HelperConstants.FILE_EX_PDF, ConfigConstants.S3_REPORT_BUCKET_NAME);
    if (!isUploaded) {
      ServerValidatorUtility.throwASInternalServerError(ServerValidatorUtility.generateMessage(MessageCodeConstants.CVT005));
    }
    return new MessageResponse.Builder()
        .setHeader(HelperConstants.LOCATION, configRegistry.getReportCdnUrl() + filename + HelperConstants.FILE_EX_PDF).setContentTypeJson()
        .setStatusCreated().successful().build();
  }

  private void convertHtmlToPdf(String srcFileName, String destFileName, long delayInMillsec) {
    try {
      String resizeCommand = new String("/usr/bin/wkhtmltopdf@" + srcFileName + "@--redirect-delay@" + delayInMillsec + "@" + destFileName);
      String cmdArgs[] = resizeCommand.split("@");
      Process thumsProcess = Runtime.getRuntime().exec(cmdArgs);
      thumsProcess.waitFor();

    } catch (Exception e) {
      LOG.error("something went wrong while converting pdf. {}", e);
      ServerValidatorUtility.throwASInternalServerError();
    }
  }

  private String saveAsHtml(String fileName, String content) {
    File file = new File(fileName);
    try {
      FileOutputStream fop = new FileOutputStream(file);
      if (!file.exists()) {
        file.createNewFile();
      }
      byte[] contentInBytes = content.getBytes();

      fop.write(contentInBytes);
      fop.flush();
      fop.close();
      return fileName;
    } catch (Exception e) {
      LOG.error("Failed to create html file. {}", e);
      ServerValidatorUtility.throwASInternalServerError();
    }
    return null;
  }

  public static final Executor getInstance() {
    return Holder.INSTANCE;
  }

  private static final class Holder {
    private static final ConvertHtmlToPdfExecutor INSTANCE = new ConvertHtmlToPdfExecutor();
  }

}
