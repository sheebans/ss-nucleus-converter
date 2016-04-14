package org.gooru.nucleus.converter.processors.messageProcessor;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import org.gooru.nucleus.converter.constants.CommandConstants;
import org.gooru.nucleus.converter.processors.command.executor.MessageResponse;
import org.gooru.nucleus.converter.processors.command.executor.convert.ConvertExecutorFactory;
import org.gooru.nucleus.converter.processors.exceptions.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final Message<Object> message;

    public MessageProcessor(Message<Object> message) {
        this.message = message;
    }

    @Override
    public MessageResponse process() {
        MessageResponse result = null;
        try {
            if (message == null || !(message.body() instanceof JsonObject)) {
                LOG.error("Invalid message received, either null or body of message is not JsonObject ");
                throw new InvalidRequestException();
            }
            MessageContext messageContext = new MessageContextHolder(message);
            switch (messageContext.command()) {
            case CommandConstants.CONVERTER_HTML_TO_EXCEL_CONVERT:
                result = ConvertExecutorFactory.ConvertHtmlToExcelExecutor().execute(messageContext);
                break;
            case CommandConstants.CONVERTER_HTML_TO_PDF_CONVERT:
                result = ConvertExecutorFactory.ConvertHtmlToPdfExecutor().execute(messageContext);
                break;
            default:
                LOG.error("Invalid command type passed in, not able to handle");
                throw new InvalidRequestException();
            }
            return result;
        } catch (Throwable throwable) {
            LOG.warn("Caught unexpected exception here", throwable);
            return new MessageResponse.Builder().setThrowable(throwable).build();
        }
    }

}
