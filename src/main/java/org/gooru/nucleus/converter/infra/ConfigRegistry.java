package org.gooru.nucleus.converter.infra;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import org.gooru.nucleus.converter.bootstrap.startup.Initializer;
import org.gooru.nucleus.converter.constants.ConfigConstants;

public class ConfigRegistry implements Initializer {

    private JsonObject config;
    private JsonObject s3Config;

    private static final String CONVERTER_FILE_LOCATION = "converter.file.location";

    @Override
    public void initializeComponent(Vertx vertx, JsonObject config) {
        synchronized (Holder.INSTANCE) {
            this.config = config;
            this.s3Config = config.getJsonObject(ConfigConstants.S3_CONFIG);
        }
    }

    public String getConverterFileLocation() {
        return config.getString(CONVERTER_FILE_LOCATION);
    }

    public String getReportCdnUrl() {
        return this.s3Config.getString(ConfigConstants.S3_REPORT_CDN_URL);
    }

    public static ConfigRegistry instance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ConfigRegistry INSTANCE = new ConfigRegistry();
    }
}
