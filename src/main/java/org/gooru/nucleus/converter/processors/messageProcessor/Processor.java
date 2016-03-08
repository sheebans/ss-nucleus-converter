package org.gooru.nucleus.converter.processors.messageProcessor;

import org.gooru.nucleus.converter.processors.command.executor.MessageResponse;

public interface Processor {
  MessageResponse process();
}
