package com.example.connectors.destination.java.jdbc.spark;

import com.example.connectors.commons.bases.base.spark.BaseSparkConnector;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;

public class JdbcSparkDestination extends BaseSparkConnector {

    public JdbcSparkDestination(SparkSession sparkSession, HashMap<String, String> configuration, Object previous) {
        super(sparkSession, configuration, previous);
    }

    public Object writeLines(RDD<String> lines) {
        return null;
    }

    public Object writeRows(Dataset<Row> rows) {
        var driver = "org.postgresql.Driver";
        var url = "jdbc:postgresql://localhost:5432/example";
        var user = "postgres";
        var password = "test001";
        var schema = "demo";
        var table = "s3_csv_spark_test";
        var fetchSize = 1000;
        var mode = "append";

        var dbtable = schema + "." + table;

        rows.write()
                .format("jdbc")
                .option("driver", driver)
                .option("url", url)
                .option("user", user)
                .option("password", password)
                .option("dbtable", dbtable)
                .option("fetchSize", fetchSize)
                .mode(mode)
                .save();

        return null;
    }
}
