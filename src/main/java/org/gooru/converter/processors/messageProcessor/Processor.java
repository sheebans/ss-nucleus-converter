package org.gooru.converter.processors.messageProcessor;

import org.gooru.converter.processors.command.executor.MessageResponse;

public interface Processor {
  MessageResponse process();
}
