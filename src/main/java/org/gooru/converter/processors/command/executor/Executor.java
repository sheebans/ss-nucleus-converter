package org.gooru.converter.processors.command.executor;

import org.gooru.converter.processors.messageProcessor.MessageContext;


public interface Executor {
  MessageResponse execute(MessageContext messageContext);
}
