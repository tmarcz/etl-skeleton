package com.example.connectors.source.java.jdbc.spark;

import com.example.connectors.commons.bases.base.spark.BaseSparkConnector;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;

public class S3SparkSource extends BaseSparkConnector {

    public S3SparkSource(SparkSession sparkSession, HashMap<String, String> configuration, Object previous) {
        super(sparkSession, configuration, previous);

        setup();
    }

    public void setup(){
        var accessKey = "test";
        var secretKey = "TeSt1234";
        var endpoint = "http://127.0.0.1:9000";
        this.sparkSession.sparkContext().hadoopConfiguration().set("fs.s3a.endpoint", endpoint);
        this.sparkSession.sparkContext().hadoopConfiguration().set("fs.s3a.access.key", accessKey);
        this.sparkSession.sparkContext().hadoopConfiguration().set("fs.s3a.secret.key", secretKey);
    }


    public String pass() {
        var bucket = "default";
        var fileRelativePath = "test.csv";
        var fileFullPath = "s3a://" + bucket + "/" + fileRelativePath;
        return fileFullPath;
    }


    public RDD<String> readLines() {
        return null;
    }


    public Dataset<Row> csvDefaultRead() {
        return null;
    }
}
