package org.gooru.converter.constants;

/**
 * Constant definition that are used to read configuration
 */
public class ConfigConstants {

  public static final String PORT = "port";
  public static final String HOST = "host";
  public static final String REDIS = "redis";
  public static final String VERTICLES_DEPLOY_LIST = "verticles.deploy.list";
  public static final String HTTP_PORT = "http.port";
  public static final String METRICS_PERIODICITY = "metrics.periodicity.seconds";
  public static final String MBUS_TIMEOUT = "message.bus.send.timeout.milliseconds";
  public static final String MAX_REQ_BODY_SIZE = "request.body.size.max.mb";

  private ConfigConstants() {
    throw new AssertionError();
  }
}
