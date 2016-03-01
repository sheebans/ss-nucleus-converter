package org.gooru.converter.constants;

/**
 * It contains the definition for the "Message Bus End Points" which are
 * addresses on which the consumers are listening.
 */
public class MessagebusEndpoints {
  public static final String MBEP_AUTH = "org.gooru.converter.message.bus.auth";
  public static final String MBEP_METRICS = "org.gooru.converter.message.bus.metrics";
  public static final String MBEP_CONVERTER = "org.gooru.converter.message.bus.converter";
  
  private MessagebusEndpoints() { 
    throw new AssertionError();
  }
}
