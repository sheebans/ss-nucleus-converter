package org.gooru.converter.constants;

/**
 * It contains the definition for the "Message Bus End Points" which are
 * addresses on which the consumers are listening. Note that these definitions
 * are for gateway, and each end point would be defined in their own component
 * as well. This means that if there is any change here, there must be a
 * corresponding change in the consumer as well.
 */
public class MessagebusEndpoints {
  public static final String MBEP_AUTH = "org.gooru.converter.message.bus.auth";
  public static final String MBEP_METRICS = "org.gooru.converter.message.bus.metrics";
  public static final String MBEP_CONVERTER = "org.gooru.converter.message.bus.converter";
  
  private MessagebusEndpoints() { 
    throw new AssertionError();
  }
}
