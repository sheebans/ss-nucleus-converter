package org.gooru.converter.routes.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.RoutingContext;

import org.gooru.converter.responses.writers.ResponseWriterBuilder;
import org.slf4j.Logger;

public class RouteResponseUtility {

  public static void responseHandler(final RoutingContext routingContext, final AsyncResult<Message<Object>> reply, final Logger LOG) {
    if (reply.succeeded()) {
      new ResponseWriterBuilder(routingContext, reply).build().writeResponse();
    } else {
      LOG.error("Not able to send message", reply.cause());
      routingContext.response().setStatusCode(500).end();
    }
  }

  public RouteResponseUtility() {
    throw new AssertionError();
  }
}
