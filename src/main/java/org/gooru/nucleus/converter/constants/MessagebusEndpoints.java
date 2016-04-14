package org.gooru.nucleus.converter.constants;

/**
 * It contains the definition for the "Message Bus End Points" which are
 * addresses on which the consumers are listening.
 */
public class MessagebusEndpoints {
    public static final String MBEP_AUTH = "org.gooru.nucleus.converter.message.bus.auth";
    public static final String MBEP_METRICS = "org.gooru.nucleus.converter.message.bus.metrics";
    public static final String MBEP_CONVERTER = "org.gooru.nucleus.converter.message.bus.converter";

    private MessagebusEndpoints() {
        throw new AssertionError();
    }
}
