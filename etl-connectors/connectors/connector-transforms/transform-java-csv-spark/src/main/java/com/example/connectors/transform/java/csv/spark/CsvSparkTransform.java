package com.example.connectors.transform.java.csv.spark;

import com.example.connectors.commons.bases.base.spark.BaseSparkConnector;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;

public class CsvSparkTransform extends BaseSparkConnector {

    public CsvSparkTransform(SparkSession sparkSession, HashMap<String, String> configuration, Object previous) {
        super(sparkSession, configuration, previous);

    }

    public Dataset<Row> read(String path) {
        var df = this.sparkSession.read()
                .option("header", true)
                .option("delimiter", "|")
                .option("inferSchema", true)
                .csv(path);
        return df;
    }

    public Dataset<Row> transform(RDD<String> lines) {
        return null;
    }

}
