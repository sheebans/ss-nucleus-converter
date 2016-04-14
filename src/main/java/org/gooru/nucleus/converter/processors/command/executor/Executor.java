package org.gooru.nucleus.converter.processors.command.executor;

import org.gooru.nucleus.converter.processors.messageProcessor.MessageContext;

public interface Executor {
    MessageResponse execute(MessageContext messageContext);
}
