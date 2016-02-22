package org.gooru.converter.routes;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import org.gooru.converter.constants.CommandConstants;
import org.gooru.converter.constants.ConfigConstants;
import org.gooru.converter.constants.MessageConstants;
import org.gooru.converter.constants.MessagebusEndpoints;
import org.gooru.converter.constants.RouteConstants;
import org.gooru.converter.routes.utils.RouteRequestUtility;
import org.gooru.converter.routes.utils.RouteResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RouteConverterConfigurator implements RouteConfigurator {

  private static final Logger LOG = LoggerFactory.getLogger("org.gooru.converter.bootstrap.ServerVerticle");

  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    eb = vertx.eventBus();
    mbusTimeout = config.getLong(ConfigConstants.MBUS_TIMEOUT, RouteConstants.DEFAULT_TIMEOUT);
    router.post(RouteConstants.EP_NUCLUES_CONVERTER_HTML_TO_PDF).handler(this::convertHtmlToPdf);
    router.post(RouteConstants.EP_NUCLUES_CONVERTER_HTML_TO_EXCEL).handler(this::convertHtmlToExcel);
    router.post(RouteConstants.EP_NUCLUES_CONVERTER_DOC_TO_PDF).handler(this::convertDocToPdf);
  }

  private void convertHtmlToPdf(RoutingContext routingContext) {
    final DeliveryOptions options =
        new DeliveryOptions().setSendTimeout(mbusTimeout).addHeader(MessageConstants.MSG_HEADER_OP, CommandConstants.CONVERT_HTML_TO_PDF);
    eb.send(MessagebusEndpoints.MBEP_CONVERTER, RouteRequestUtility.getBodyForMessage(routingContext), options,
        reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOG));
  }

  private void convertHtmlToExcel(RoutingContext routingContext) {
    final DeliveryOptions options =
        new DeliveryOptions().setSendTimeout(mbusTimeout).addHeader(MessageConstants.MSG_HEADER_OP, CommandConstants.CONVERT_HTML_TO_EXCEL);
    eb.send(MessagebusEndpoints.MBEP_CONVERTER, RouteRequestUtility.getBodyForMessage(routingContext), options,
        reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOG));
  }

  private void convertDocToPdf(RoutingContext routingContext) {
    final DeliveryOptions options =
        new DeliveryOptions().setSendTimeout(mbusTimeout).addHeader(MessageConstants.MSG_HEADER_OP, CommandConstants.CONVERT_DOC_TO_PDF);
    eb.send(MessagebusEndpoints.MBEP_CONVERTER, RouteRequestUtility.getBodyForMessage(routingContext), options,
        reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOG));
  }
}
