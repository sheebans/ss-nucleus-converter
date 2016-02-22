package org.gooru.converter.constants;

public final class RouteConstants {

  // Helper constants
  private static final String API_VERSION = "v1";
  private static final String API_BASE_ROUTE = "/api/nucleus-converter/" + API_VERSION + "/";
  public static final String API_CONVERTER_AUTH_ROUTE = "/api/nucleus-converter/*";

  // Helper: Operations
  private static final String HTML_TO_PDF = "html-to-pdf";
  private static final String HTML_TO_EXCEL = "html-to-excel";
  private static final String DOC_TO_PDF = "doc-to-pdf";

  // Actual End Point Constants: Note that constant values may be duplicated but
  // we are going to have individual constant values to work with for each
  // point instead of reusing the same

  public static final String EP_NUCLUES_CONVERTER_HTML_TO_PDF = API_BASE_ROUTE + HTML_TO_PDF;

  public static final String EP_NUCLUES_CONVERTER_HTML_TO_EXCEL = API_BASE_ROUTE + HTML_TO_EXCEL;

  public static final String EP_NUCLUES_CONVERTER_DOC_TO_PDF = API_BASE_ROUTE + DOC_TO_PDF;

  public static final long DEFAULT_TIMEOUT = 30000L;

  private RouteConstants() {
    throw new AssertionError();
  }
}
