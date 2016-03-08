package org.gooru.nucleus.converter.bootstrap;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

import org.gooru.nucleus.converter.constants.MessagebusEndpoints;
import org.gooru.nucleus.converter.processors.ProcessorBuilder;
import org.gooru.nucleus.converter.processors.command.executor.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(ConverterVerticle.class);

  @Override
  public void start(Future<Void> voidFuture) throws Exception {
    EventBus eb = vertx.eventBus();
    eb.consumer(MessagebusEndpoints.MBEP_CONVERTER, message -> {
      LOG.debug("Received message: " + message.body());
      vertx.executeBlocking(future -> {
        MessageResponse result = new ProcessorBuilder(message).build().process();
        future.complete(result);
      }, res -> {
        MessageResponse result = (MessageResponse) res.result();
        message.reply(result.reply(), result.deliveryOptions());
      });

    }).completionHandler(result -> {
      if (result.succeeded()) {
        LOG.info("Converter end point ready to listen");
      } else {
        LOG.error("Error registering the converter handler. Halting the user machinery");
        Runtime.getRuntime().halt(1);
      }
    });
  }

}
