package com.example.drivers.spark.util;

import com.example.commons.client.JobPipelineClient;
import com.example.commons.model.ApplicationStepPipelineModel;
import com.example.commons.model.ApplicationSuiteModel;
import com.example.commons.model.ExecutionStepPipelineModel;
//import com.example.drivers.spark.model.PipelineStepModel;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SparkRunner {
    private ApplicationSuiteModel applicationSuite;
    private JobPipelineClient pipelineClient;
    private Long jobId;

    public static final String SPARK_MASTER = "spark.master";
    public static final String SPARK_APP_NAME = "spark.appName";

    public SparkRunner(ApplicationSuiteModel applicationSuite, JobPipelineClient pipelineClient) {
        this.applicationSuite = applicationSuite;
        this.pipelineClient = pipelineClient;
        this.jobId = applicationSuite.getJobId();
    }

    public void run() {
        pipelineClient.updateStatus(getStatus("Runner", "Preparing Spark"));
        SparkSession sparkSession = this.getSparkSession();
        pipelineClient.updateStatus(getStatus("Runner", "Starting pipelines steps"));
        this.runSteps(sparkSession);
        pipelineClient.updateStatus(getStatus("Runner", "Steps completed"));
    }

    private void runSteps(SparkSession sparkSession) {
        Object previous = null;
        for (var step : applicationSuite.getPipeline()) {
            pipelineClient.updateStatus(getStatus("Step", "Starting step: '" + step.getName() +"'"));
            previous = this.runStep(step, sparkSession, previous);
        }
    }

    private Object runStep(ApplicationStepPipelineModel step, SparkSession sparkSession, Object previous) {
        Object result;
        try {
            // TODO: files from Nexus or shared persistent storage (e.g. connected as docker Nexus volume)
            var file = new File(step.getUrl());
            var url = file.toURI().toURL();
            var cl = new URLClassLoader(new URL[]{url});
            var clazz = cl.loadClass(step.getClassPath());
//            var clazz = Class.forName(step.getClassPath());
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

    private ExecutionStepPipelineModel getStatus(String title, String message){
        return new ExecutionStepPipelineModel(jobId,"Driver", title, message);
    }
}