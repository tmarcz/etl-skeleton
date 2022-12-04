package com.example.connectors.commons.bases.base.spark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;

@Data
@AllArgsConstructor
public abstract class BaseSparkConnector {
    protected SparkSession sparkSession;
    protected HashMap<String, String> configuration;
    protected Object previous;


}
