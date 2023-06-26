package com.juntai.tinder.service;

import com.juntai.tinder.entity.IotdbForce;
import com.juntai.tinder.entity.IotdbForcePosture;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionPool;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.tsfile.utils.BitMap;
import org.apache.iotdb.tsfile.write.record.Tablet;
import org.apache.iotdb.tsfile.write.schema.MeasurementSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @Author: Song
 * @Date 2023/6/26 13:38
 */
public class IotdbSession {

    private static SessionPool sessionPool;
    private static ExecutorService service;
    private static Random random = new Random();


    /** Build a custom SessionPool for this example */
    private static void constructCustomSessionPool() {
        sessionPool =
                new SessionPool.Builder()
                        .host("192.168.3.16")
                        .port(6667)
                        .user("root")
                        .password("root")
                        .maxSize(100)
                        .build();
    }

    public static void main(String[] args)
            throws StatementExecutionException, IoTDBConnectionException, InterruptedException {
        constructCustomSessionPool();
        service = newFixedThreadPool(10);
        List<IotdbForce> forces = new ArrayList<>();
        IotdbForce force1 = new IotdbForce();
        force1.setStage(222);
        force1.setSimTime(333);
        force1.setStepRatio(444.44);

        List<IotdbForcePosture> forcePostures = new ArrayList<>();
        IotdbForcePosture forcePosture1 = new IotdbForcePosture();
        forcePosture1.setForceId(1);
        forcePosture1.setSpeed(100.00);
        forcePosture1.setPitch(100.00);
        forcePosture1.setHeading(0.00);
        forcePosture1.setLon(123.45);
        forcePosture1.setLat(23.45);
        forcePosture1.setAlt(0.00);
        forcePosture1.setRoll(0.00);
        forcePosture1.setSpeed2(100.00);
        forcePosture1.setPitch2(100.00);
        forcePosture1.setHeading2(0.00);
        forcePosture1.setLon2(123.45);
        forcePosture1.setLat2(23.45);
        forcePosture1.setAlt2(0.00);
        forcePosture1.setRoll2(0.00);
        forcePosture1.setRemainingMileage(0.00);
        forcePosture1.setLife(100.00);
        forcePosture1.setLife2(0.00);
        IotdbForcePosture forcePosture2 = new IotdbForcePosture();
        forcePosture2.setForceId(2);
        forcePosture2.setSpeed(100.00);
        forcePosture2.setPitch(100.00);
        forcePosture2.setHeading(0.00);
        forcePosture2.setLon(123.45);
        forcePosture2.setLat(23.45);
        forcePosture2.setAlt(0.00);
        forcePosture2.setRoll(0.00);
        forcePosture2.setSpeed2(100.00);
        forcePosture2.setPitch2(100.00);
        forcePosture2.setHeading2(0.00);
        forcePosture2.setLon2(123.45);
        forcePosture2.setLat2(23.45);
        forcePosture2.setAlt2(0.00);
        forcePosture2.setRoll2(0.00);
        forcePosture2.setRemainingMileage(0.00);
        forcePosture2.setLife(100.00);
        forcePosture2.setLife2(0.00);
        forcePostures.add(forcePosture1);
        forcePostures.add(forcePosture2);
        force1.setForces(forcePostures);

        IotdbForce force2 = new IotdbForce();
        force2.setStage(223);
        force2.setSimTime(334);
        force2.setStepRatio(444.45);

        List<IotdbForcePosture> forcePostures2 = new ArrayList<>();
        IotdbForcePosture forcePosture3 = new IotdbForcePosture();
        forcePosture3.setForceId(3);
        forcePosture3.setSpeed(100.00);
        forcePosture3.setPitch(100.00);
        forcePosture3.setHeading(0.00);
        forcePosture3.setLon(123.45);
        forcePosture3.setLat(23.45);
        forcePosture3.setAlt(0.00);
        forcePosture3.setRoll(0.00);
        forcePosture3.setSpeed2(100.00);
        forcePosture3.setPitch2(100.00);
        forcePosture3.setHeading2(0.00);
        forcePosture3.setLon2(123.45);
        forcePosture3.setLat2(23.45);
        forcePosture3.setAlt2(0.00);
        forcePosture3.setRoll2(0.00);
        forcePosture3.setRemainingMileage(0.00);
        forcePosture3.setLife(100.00);
        forcePosture3.setLife2(0.00);
        IotdbForcePosture forcePosture4 = new IotdbForcePosture();
        forcePosture4.setForceId(4);
        forcePosture4.setSpeed(100.00);
        forcePosture4.setPitch(100.00);
        forcePosture4.setHeading(0.00);
        forcePosture4.setLon(123.45);
        forcePosture4.setLat(23.45);
        forcePosture4.setAlt(0.00);
        forcePosture4.setRoll(0.00);
        forcePosture4.setSpeed2(100.00);
        forcePosture4.setPitch2(100.00);
        forcePosture4.setHeading2(0.00);
        forcePosture4.setLon2(123.45);
        forcePosture4.setLat2(23.45);
        forcePosture4.setAlt2(0.00);
        forcePosture4.setRoll2(0.00);
        forcePosture4.setRemainingMileage(0.00);
        forcePosture4.setLife(100.00);
        forcePosture4.setLife2(0.00);
        forcePostures2.add(forcePosture3);
        forcePostures2.add(forcePosture4);
        force2.setForces(forcePostures2);

        IotdbForce force3 = new IotdbForce();
        force3.setStage(224);
        force3.setSimTime(335);
        force3.setStepRatio(444.46);

        List<IotdbForcePosture> forcePostures3 = new ArrayList<>();
        IotdbForcePosture forcePosture5 = new IotdbForcePosture();
        forcePosture5.setForceId(5);
        forcePosture5.setSpeed(100.00);
        forcePosture5.setPitch(100.00);
        forcePosture5.setHeading(0.00);
        forcePosture5.setLon(123.45);
        forcePosture5.setLat(23.45);
        forcePosture5.setAlt(0.00);
        forcePosture5.setRoll(0.00);
        forcePosture5.setSpeed2(100.00);
        forcePosture5.setPitch2(100.00);
        forcePosture5.setHeading2(0.00);
        forcePosture5.setLon2(123.45);
        forcePosture5.setLat2(23.45);
        forcePosture5.setAlt2(0.00);
        forcePosture5.setRoll2(0.00);
        forcePosture5.setRemainingMileage(0.00);
        forcePosture5.setLife(100.00);
        forcePosture5.setLife2(0.00);
        IotdbForcePosture forcePosture6 = new IotdbForcePosture();
        forcePosture6.setForceId(6);
        forcePosture6.setSpeed(100.00);
        forcePosture6.setPitch(100.00);
        forcePosture6.setHeading(0.00);
        forcePosture6.setLon(123.45);
        forcePosture6.setLat(23.45);
        forcePosture6.setAlt(0.00);
        forcePosture6.setRoll(0.00);
        forcePosture6.setSpeed2(100.00);
        forcePosture6.setPitch2(100.00);
        forcePosture6.setHeading2(0.00);
        forcePosture6.setLon2(123.45);
        forcePosture6.setLat2(23.45);
        forcePosture6.setAlt2(0.00);
        forcePosture6.setRoll2(0.00);
        forcePosture6.setRemainingMileage(0.00);
        forcePosture6.setLife(100.00);
        forcePosture6.setLife2(0.00);
        forcePostures3.add(forcePosture5);
        forcePostures3.add(forcePosture6);
        force3.setForces(forcePostures3);

        forces.add(force1);
        forces.add(force2);
        forces.add(force3);

//        insertTablet("sim123456",200,forces);
//        dataTime = simTime * 1e6
        InsertTablet("sim123",1200,1200000000,forces);

        sessionPool.insertTablet(createTablet("sim123",1200,forces));
        sessionPool.close();
        service.shutdown();
    }

    public static void InsertTablet(String simId,int simTime,int dataTime,List<IotdbForce> forces) throws IoTDBConnectionException, StatementExecutionException {
        Tablet tablet = createTablet(simId,simTime,forces);
        sessionPool.insertTablet(tablet,false);
        sessionPool.close();
    }

    public static Tablet createTablet(String simId, int simTime, List<IotdbForce> forces){
        //想定名称
        String deviceId = "root.tinder." + simId;

        //需要持久化的兵力属性
        List<MeasurementSchema> schemaList = new ArrayList<>();
        schemaList.add(new MeasurementSchema("simTime", TSDataType.INT64));
        schemaList.add(new MeasurementSchema("forceId", TSDataType.INT64));
        schemaList.add(new MeasurementSchema("life", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lon", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lat", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("alt", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("heading", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("pitch", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("roll", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("speed", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("remainingMileage", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("life2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lon2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lat2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("alt2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("heading2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("pitch2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("roll2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("speed2", TSDataType.DOUBLE));

//        Tablet tablet = new Tablet(deviceId, schemaList, forces.size());
        //时间戳timestamps，这里用作别的含义 todo：决定ts
        long[] timestamps = new long[forces.size()];
        //各测点数据列表对应的 BitMap，用于标记空值 没用上
        BitMap[] bitMaps = new BitMap[forces.size()];
        //赋值
        Object[] values = null;

        Tablet tablet = new Tablet(deviceId,schemaList,timestamps,values,bitMaps,forces.size());

        return tablet;
    }

    /**
     * insert the data of a device. For each timestamp, the number of measurements is the same.
     *
     * <p>Users need to control the count of Tablet and write a batch when it reaches the maxBatchSize
     */
    //TODO:没有传值成功，而且只插入了一次
    private static void insertTablet(String simId, int simTime, List<IotdbForce> forces) throws IoTDBConnectionException, StatementExecutionException {
        /*
         * A Tablet example:
         *      device1
         * time s1, s2, s3
         * 1,   1,  1,  1
         * 2,   2,  2,  2
         * 3,   3,  3,  3
         */
        // The schema of measurements of one device
        // only measurementId and data type in MeasurementSchema take effects in Tablet
//        List<MeasurementSchema> schemaList = new ArrayList<>();
//        schemaList.add(new MeasurementSchema("s1", TSDataType.INT64));
//        schemaList.add(new MeasurementSchema("s2", TSDataType.INT64));
//        schemaList.add(new MeasurementSchema("s3", TSDataType.INT64));

        String deviceId = "root.tinder." + simId;

        List<MeasurementSchema> schemaList = new ArrayList<>();
        schemaList.add(new MeasurementSchema("simTime", TSDataType.INT64));
        schemaList.add(new MeasurementSchema("forceId", TSDataType.INT64));
        schemaList.add(new MeasurementSchema("life", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lon", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lat", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("alt", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("heading", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("pitch", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("roll", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("speed", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("remainingMileage", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("life2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lon2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("lat2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("alt2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("heading2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("pitch2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("roll2", TSDataType.DOUBLE));
        schemaList.add(new MeasurementSchema("speed2", TSDataType.DOUBLE));

        Tablet tablet = new Tablet(deviceId, schemaList, forces.size());

        // Method 1 to add tablet data
        long timestamp = System.currentTimeMillis();

        for(int i=0;i<forces.size();i++){
            int rowIndex = tablet.rowSize++;
            for(int j=0;j<forces.get(i).getForces().size();j++){

                tablet.addValue(schemaList.get(0).getMeasurementId(),j,Integer.toUnsignedLong(simTime));
                tablet.addValue("forceId",j,Integer.toUnsignedLong(forces.get(i).getForces().get(j).getForceId()));
                tablet.addValue("life",j,forces.get(i).getForces().get(j).getLife());
                tablet.addValue("lon",j,forces.get(i).getForces().get(j).getLon());
                tablet.addValue("lat",j,forces.get(i).getForces().get(j).getLat());
                tablet.addValue("alt",j,forces.get(i).getForces().get(j).getAlt());
                tablet.addValue("heading",j,forces.get(i).getForces().get(j).getHeading());
                tablet.addValue("pitch",j,forces.get(i).getForces().get(j).getPitch());
                tablet.addValue("roll",j,forces.get(i).getForces().get(j).getRoll());
                tablet.addValue("speed",j,forces.get(i).getForces().get(j).getSpeed());
                tablet.addValue("remainingMileage",j,forces.get(i).getForces().get(j).getRemainingMileage());
                tablet.addValue("life2",j,forces.get(i).getForces().get(j).getLife2());
                tablet.addValue("lon2",j,forces.get(i).getForces().get(j).getLon2());
                tablet.addValue("lat2",j,forces.get(i).getForces().get(j).getLat2());
                tablet.addValue("alt2",j,forces.get(i).getForces().get(j).getAlt2());
                tablet.addValue("heading2",j,forces.get(i).getForces().get(j).getHeading2());
                tablet.addValue("pitch2",j,forces.get(i).getForces().get(j).getPitch2());
                tablet.addValue("roll2",j,forces.get(i).getForces().get(j).getRoll2());
                tablet.addValue("speed2",j,forces.get(i).getForces().get(j).getSpeed2());
            }
            if (tablet.rowSize == tablet.getMaxRowNumber()) {
                sessionPool.insertTablet(tablet, false);
                tablet.reset();
            }
            timestamp++;
        }


        if (tablet.rowSize != 0) {
            sessionPool.insertTablet(tablet);
            tablet.reset();
        }

        // Method 2 to add tablet data
//        long[] timestamps = tablet.timestamps;
//        Object[] values = tablet.values;
//
//        for (long time = 0; time < 100; time++) {
//            int row = tablet.rowSize++;
//            timestamps[row] = time;
//            for (int i = 0; i < 3; i++) {
//                long[] sensor = (long[]) values[i];
//                sensor[row] = i;
//            }
//            if (tablet.rowSize == tablet.getMaxRowNumber()) {
//                sessionPool.insertTablet(tablet, true);
//                tablet.reset();
//            }
//        }
//
//        if (tablet.rowSize != 0) {
//            sessionPool.insertTablet(tablet);
//            tablet.reset();
//        }
    }


}
