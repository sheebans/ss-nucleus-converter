package org.gooru.nucleus.converter.infra;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.gooru.nucleus.converter.bootstrap.startup.Initializer;
import org.gooru.nucleus.converter.constants.ConfigConstants;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class S3Client implements Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(S3Client.class);
    private static final Logger S3_LOG = LoggerFactory.getLogger("log.s3");
    private JsonObject config;
    protected Context context;
    private RestS3Service restS3Service;
    private static final String FILE_NAME = "filename";

    @Override
    public void initializeComponent(Vertx vertx, JsonObject config) {
        synchronized (Holder.INSTANCE) {
            try {
                this.config = config.getJsonObject(ConfigConstants.S3_CONFIG);
                AWSCredentials awsCredentials =
                    new AWSCredentials(this.config.getString(ConfigConstants.S3_ACCESS_KEY),
                        this.config.getString(ConfigConstants.S3_SECRET));
                restS3Service = new RestS3Service(awsCredentials);
            } catch (Exception e) {
                LOG.error("S3 rest service start failed ! ", e);
            }
        }
    }

    public boolean uploadFileS3(final String fileLocation, final String filename, final String s3BucketKey) {
        boolean isUploaded = false;
        try {
            final String s3Bucket = this.config.getString(s3BucketKey);
            Path path = Paths.get(fileLocation + filename);
            byte[] data = Files.readAllBytes(path);

            // Upload file to s3
            long start = System.currentTimeMillis();
            S3Object fileObject = new S3Object(filename, data);
            S3Object uploadedObject = restS3Service.putObject(s3Bucket, fileObject);

            if (uploadedObject != null) {
                LOG.debug("File uploaded to s3 succeeded :   key {} ", uploadedObject.getKey());
                LOG.debug("Elapsed time to complete upload file to s3 in service :"
                    + (System.currentTimeMillis() - start) + " ms");
                JsonObject res = new JsonObject();
                res.put(FILE_NAME, uploadedObject.getKey());
                S3_LOG.info("S3 Uploaded Id : " + uploadedObject.getKey());
                // Delete temp file after the s3 upload
                boolean fileDeleted = Files.deleteIfExists(path);
                if (fileDeleted) {
                    LOG.debug("Temp file have been deleted from local file system : File name {} ", path.getFileName());
                } else {
                    LOG.error("File delete from local file system failed : File name {} ", path.getFileName());
                }
                isUploaded = true;
            }
        } catch (Exception e) {
            LOG.error("Upload failed : {} ", e);
        }
        return isUploaded;
    }

    public static S3Client instance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final S3Client INSTANCE = new S3Client();
    }
}
