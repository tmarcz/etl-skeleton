package com.example.scheduler.service;

import com.example.scheduler.client.WorkflowService;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;

@Primary
@Singleton
public class DefaultSchedulerService implements SchedulerService {

    @Inject
    private WorkflowService workflowService;

    @Override
    public boolean runFirst() {
        boolean result = true;

        String db_url = "jdbc:postgresql://localhost/test";
        String user = "postgres";
        String pass = "test001";
        String query =
                "select * from Scheduler.Jobs " +
                "where active = true and running = false and next_run_date < ? " +
                "ORDER BY next_run_date asc " +
                "limit 1 " +
                "for update skip locked";

        try(Connection con = DriverManager.getConnection(db_url, user, pass);
            PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ) {
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            con.setAutoCommit(false);

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(2000,1,4, 0, 0, 0);
            ps.setTimestamp(1, new Timestamp(calendar.getTimeInMillis()));

            ResultSet rs = ps.executeQuery();
            result = rs.next();
            var threadId = Thread.currentThread().getId();
            System.out.println("# CHECK Thread: " + threadId);
            if (result) {
                var id = rs.getInt("id");
                System.out.println("# RESULT Thread: " + threadId + "\t # Running job: "+ id);

                rs.updateBoolean("running",true);
                rs.updateRow();

                workflowService.startJob(id); // TODO: mock | request to workflow service to propagate to workers

                System.out.println("# end #");
            }

            con.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void runAll() {
        boolean result;
        do {
            result = runFirst();
        } while (result);
    }

    @Override
    public void runAllAsync(int threadCount) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        Arrays.setAll(threads, i -> new Thread(this::runAll));
        Arrays.stream(threads).forEach(Thread::start);
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
    }
}

