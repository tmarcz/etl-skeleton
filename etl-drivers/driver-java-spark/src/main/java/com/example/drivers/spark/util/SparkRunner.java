package com.example.drivers.spark.util;

import com.example.drivers.spark.client.PipelineClient;
import com.example.drivers.spark.model.ApplicationSuiteModel;
import com.example.drivers.spark.model.PipelineStepModel;
import org.apache.spark.sql.SparkSession;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SparkRunner {
    private ApplicationSuiteModel applicationSuite;
    private PipelineClient pipelineClient;

    public static final String SPARK_MASTER = "spark.master";
    public static final String SPARK_APP_NAME = "spark.appName";

    public SparkRunner(ApplicationSuiteModel applicationSuite, PipelineClient pipelineClient) {
        this.applicationSuite = applicationSuite;
        this.pipelineClient = pipelineClient;
    }

    public void run() {
        SparkSession sparkSession = this.getSparkSession();
        this.runSteps(sparkSession);
    }

    private void runSteps(SparkSession sparkSession) {
        Object previous = null;
        for (var step : applicationSuite.getPipeline()) {
            previous = this.runStep(step, sparkSession, previous);
        }
    }

    private Object runStep(PipelineStepModel step, SparkSession sparkSession, Object previous) {
        Object result;
        try {
            var clazz = Class.forName(step.getClassPath());
            var ctor = clazz.getConstructors()[0];
            var object = ctor.newInstance(sparkSession, step.getConfiguration(), previous);
            var method = Arrays.stream(clazz.getMethods())
                    .filter(p -> p.getName().equals(step.getMethodName()))
                    .findFirst()
                    .get();
            result = previous == null ? method.invoke(object) : method.invoke(object, previous);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return result;
    }

    private SparkSession getSparkSession()  {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException exception) {
            throw new RuntimeException(exception);
        }
        var master = applicationSuite.getResource().getConfiguration().get(SPARK_MASTER);
        var appName = applicationSuite.getResource().getConfiguration().get(SPARK_APP_NAME);
        var result = SparkSession
                .builder()
                .master(master)
                .config("spark.driver.host", ip)
                .appName(appName)
                .getOrCreate();
        return result;
    }
}