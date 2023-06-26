package com.juntai.tinder.service;

import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.SessionDataSet.DataIterator;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @Author: Song
 * @Date 2023/6/26 10:16
 */
public class SessionPoolExample {

    private static SessionPool sessionPool;
    private static ExecutorService service;

    /** Build a custom SessionPool for this example */
    private static void constructCustomSessionPool() {
        sessionPool =
                new SessionPool.Builder()
                        .host("192.168.3.16")
                        .port(6667)
                        .user("root")
                        .password("root")
                        .maxSize(3)
                        .build();
    }

    /** Build a redirect-able SessionPool for this example */
    private static void constructRedirectSessionPool() {
        List<String> nodeUrls = new ArrayList<>();
        nodeUrls.add("127.0.0.1:6667");
        nodeUrls.add("127.0.0.1:6668");
        sessionPool =
                new SessionPool.Builder()
                        .nodeUrls(nodeUrls)
                        .user("root")
                        .password("root")
                        .maxSize(3)
                        .build();
    }

    public static void main(String[] args)
            throws StatementExecutionException, IoTDBConnectionException, InterruptedException {
        // Choose the SessionPool you going to use
//        constructRedirectSessionPool();
        constructCustomSessionPool();
//        service = Executors.newFixedThreadPool(10);
        service = newFixedThreadPool(10);

        insertRecord();
//        queryByRowRecord();
//        Thread.sleep(1000);
//        queryByIterator();
        sessionPool.close();
        service.shutdown();
    }

    // more insert example, see SessionExample.java
    private static void insertRecord() throws StatementExecutionException, IoTDBConnectionException {
        String deviceId = "root.sg1.d1";
        List<String> measurements = new ArrayList<>();
        List<TSDataType> types = new ArrayList<>();
        measurements.add("s1");
        measurements.add("s2");
        measurements.add("s3");
        types.add(TSDataType.INT64);
        types.add(TSDataType.INT64);
        types.add(TSDataType.INT64);

        for (long time = 0; time < 10; time++) {
            List<Object> values = new ArrayList<>();
            values.add(1L);
            values.add(2L);
            values.add(3L);
            sessionPool.insertRecord(deviceId, time, measurements, types, values);
        }
    }

    private static void queryByRowRecord() {
        for (int i = 0; i < 1; i++) {
            service.submit(
                    () -> {
                        SessionDataSetWrapper wrapper = null;
                        try {
                            wrapper = sessionPool.executeQueryStatement("select * from root.sg1.d1");
                            System.out.println(wrapper.getColumnNames());
                            System.out.println(wrapper.getColumnTypes());
                            while (wrapper.hasNext()) {
                                System.out.println(wrapper.next());
                            }
                        } catch (IoTDBConnectionException | StatementExecutionException e) {
                            e.printStackTrace();
                        } finally {
                            // remember to close data set finally!
                            sessionPool.closeResultSet(wrapper);
                        }
                    });
        }
    }

    private static void queryByIterator() {
        for (int i = 0; i < 1; i++) {
            service.submit(
                    () -> {
                        SessionDataSetWrapper wrapper = null;
                        try {
                            wrapper = sessionPool.executeQueryStatement("select * from root.sg1.d1");
                            // get DataIterator like JDBC
                            DataIterator dataIterator = wrapper.iterator();
                            System.out.println(wrapper.getColumnNames());
                            System.out.println(wrapper.getColumnTypes());
                            while (dataIterator.next()) {
                                StringBuilder builder = new StringBuilder();
                                for (String columnName : wrapper.getColumnNames()) {
                                    builder.append(dataIterator.getString(columnName) + " ");
                                }
                                System.out.println(builder);
                            }
                        } catch (IoTDBConnectionException | StatementExecutionException e) {
                            e.printStackTrace();
                        } finally {
                            // remember to close data set finally!
                            sessionPool.closeResultSet(wrapper);
                        }
                    });
        }
    }
}