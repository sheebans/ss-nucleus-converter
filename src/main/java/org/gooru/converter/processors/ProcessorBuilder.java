package org.gooru.converter.processors;

import io.vertx.core.eventbus.Message;

import org.gooru.converter.processors.messageProcessor.MessageProcessor;
import org.gooru.converter.processors.messageProcessor.Processor;

public class ProcessorBuilder {
  private final Message<Object> message;


  public ProcessorBuilder(Message<Object> message) {
    this.message = message;
  }

  public Processor build() {
    return new MessageProcessor(message);
  }
}
