package com.juntai.tinder.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 距离计算工具类
 *
 * @author 刘龙海
 * @version 1.0.0
 */
public final class GeometryUtils {
    /**
     * 180°
     **/
    private static final DecimalFormat DF = new DecimalFormat("0.000000");
    private static double EARTH_RADIUS = 6378.137;
    private static Double INFINITY = 1e10;
    private static Double DELTA = 1e-10;

    private GeometryUtils() {
        throw new IllegalStateException("Utils");
    }

    public static double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }

    /**
     * 根据一点的坐标与距离，以及方向，计算另外一点的位置
     *
     * @param angle     角度，从正北顺时针方向开始计算
     * @param startLong 起始点经度
     * @param startLat  起始点纬度
     * @param distance  距离，单位km
     * @return
     */
    public static Point calLocationByDistanceAndLocationAndDirection(double angle, double startLong, double startLat, double distance) {
        //将距离转换成经度的计算公式
        double r = distance / EARTH_RADIUS;
        // 转换为radian，否则结果会不正确
        angle = Math.toRadians(angle);
        startLong = Math.toRadians(startLong);
        startLat = Math.toRadians(startLat);
        double lat = Math.asin(Math.sin(startLat) * Math.cos(r) + Math.cos(startLat) * Math.sin(r) * Math.cos(angle));
        double lon = startLong + Math.atan2(Math.sin(angle) * Math.sin(r) * Math.cos(startLat), Math.cos(r) - Math.sin(startLat) * Math.sin(lat));
        // 转为正常的10进制经纬度
        lon = Math.toDegrees(lon);
        lat = Math.toDegrees(lat);
        Point point = new Point(Double.valueOf(DF.format(lon)), Double.valueOf(DF.format(lat)));
        return point;
    }

    /**
     * 计算两点之间距离 参数为经纬度
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return 距离
     */
    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        /**
         * 两点纬度差
         */
        double a = radLat1 - radLat2;
        /**
         * 两点的经度差
         */
        double b = getRadian(lon1) - getRadian(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s * 1000;
    }

    /**
     * 得到lon2, lat2相对于 lon1, lat1 的XY向量距离（米）
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public static Point getXyDistance(double lon1, double lat1, double lon2, double lat2) {

        double x = getDistance(lon1, lat1, lon2, lat1);
        double y = getDistance(lon1, lat1, lon1, lat2);
        x = Math.abs(x) * ((lon2 < lon1) ? -1 : 1);
        y = Math.abs(y) * ((lat2 < lat1) ? -1 : 1);
        return new Point(x, y);

    }


    /**
     * 计算两点之间距离, 参数为 Point
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double getDistanceBetweenPoint(Point p1, Point p2) {
        return getDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    /**
     * 计算笛卡尔坐标系下两点坐标
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getCartesianDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 射线法判断点是否在多边形内
     *
     * @param polygon
     * @param point
     * @return
     */
    public static Boolean isInPolygon(List<Point> polygon, Point point) {
        /**
         * 顶点数
         */
        int n = polygon.size();
        /**
         * 射线与多边形交点数
         */
        int count = 0;
        /**
         * 定义射线
         */
        Line radial = new Line(point, new Point(-INFINITY, point.y));
        for (int i = 0; i < n; i++) {
            Line side = new Line(polygon.get(i), polygon.get((i + 1) % n));
            /**
             * 点在边上
             */
            if (isOnline(point, side)) {
                return true;
            }
            //如果radial平行x轴则不考虑
            if (Math.abs(side.p1.y - side.p2.y) < DELTA) {
                continue;
            }
            //计算射线与边相交点数
            if (isOnline(side.p1, radial)) {
                if (side.p1.y > side.p2.y) {
                    count++;
                }
            } else if (isOnline(side.p2, radial)) {
                if (side.p2.y > side.p1.y) {
                    count++;
                }
            } else if (isIntersect(radial, side)) {
                count++;
            }
        }
        //相交点数为奇数，点在多边形内部
        if (count % 2 == 1) {
            return true;
        }
        return false;
    }

    /**
     * 计算叉乘|P0P1|x|P0P2|
     *
     * @param p1
     * @param p2
     * @param p0
     * @return
     */
    public static Double multiply(Point p1, Point p2, Point p0) {
        return (p1.x - p0.x) * (p2.y - p0.y) - (p2.x - p0.x) * (p1.y - p0.y);
    }

    /**
     * 判断线段是否包含点point
     *
     * @param point
     * @param line
     * @return
     */
    public static Boolean isOnline(Point point, Line line) {
        return Math.abs(multiply(line.p1, line.p2, point)) < DELTA
                && (point.x - line.p1.x) * (point.x - line.p2.x) <= 0
                && (point.y - line.p1.y) * (point.y - line.p2.y) <= 0;
    }

    /**
     * 判断线段是否相交
     *
     * @param line1
     * @param line2
     * @return
     */
    public static Boolean isIntersect(Line line1, Line line2) {
        return Math.max(line1.p1.x, line1.p2.x) >= Math.min(line2.p1.x, line2.p2.x)
                && Math.max(line2.p1.x, line2.p2.x) >= Math.min(line1.p1.x, line1.p2.x)
                && Math.max(line1.p1.y, line1.p2.y) >= Math.min(line2.p1.y, line2.p2.y)
                && Math.max(line2.p1.y, line2.p2.y) >= Math.min(line1.p1.y, line1.p2.y)
                && multiply(line2.p1, line1.p2, line1.p1) * multiply(line1.p2, line2.p2, line1.p1) >= 0
                && multiply(line1.p1, line2.p2, line2.p1) * multiply(line2.p2, line1.p2, line2.p1) >= 0;
    }

    /**
     * minimum distance from targetPoint to the polygon
     *
     * @param polygon     polygon，the outline
     * @param targetPoint targetPoint to judge
     * @return the minimumLen, if in polygon, return 0
     */
    public static Double minDistanceFromPointToPolygonUsingPoint(List<Point> polygon, Point targetPoint) {
        double minDis = Double.MAX_VALUE;

        if (polygon.size() < 3) {
            return -1d;
        }

        for (int i = 0; i < polygon.size(); ++i) {
            minDis = Math.min((minDistanceFromPointToLine(polygon.get(i), polygon.get((i + 1) % polygon.size()), targetPoint)), minDis);
        }
        return minDis;
    }

    /**
     * calculate min distance form point to line
     *
     * @param leftPoint   line left, name as A
     * @param rightPoint  line right, name as B
     * @param targetPoint target, name as P
     * @return min distance
     */
    public static Double minDistanceFromPointToLine(Point leftPoint, Point rightPoint, Point targetPoint) {
        double lineLen = getDistanceBetweenPoint(leftPoint, rightPoint);

        // tarPoint on the line
        if (isOnline(targetPoint, new Line(leftPoint, rightPoint))) {
            return 0d;
        }

        // line is one Point
        if (lineLen < DELTA) {
            return getDistanceBetweenPoint(targetPoint, leftPoint);
        }

        // ordinary case 1
        // @ P
        //
        //           A ------------------ B
        // AP_AB: ap's projection on ab
        double ap2ab = getPointMultiply(
                new Point(targetPoint.x - leftPoint.x, targetPoint.y - leftPoint.y),
                new Point(rightPoint.x - leftPoint.x, rightPoint.y - leftPoint.y)
        );
        if (ap2ab <= 0) {
            return getDistanceBetweenPoint(targetPoint, leftPoint);
        }

        double ab = getPointMultiply(
                new Point(rightPoint.x - leftPoint.x, rightPoint.y - leftPoint.y),
                new Point(rightPoint.x - leftPoint.x, rightPoint.y - leftPoint.y)
        );

        // ordinary case 2
        //                                       @ P
        //
        //           A ------------------ B
        if (ap2ab >= ab) {
            return getDistanceBetweenPoint(targetPoint, rightPoint);
        }

        // ordinary case 3
        //                     @ P
        //
        //           A -------- f --------- B
        // partition = af/ab
        double partition = ap2ab / ab;
        Point footPoint = new Point();
        footPoint.setX(leftPoint.x + (rightPoint.x - leftPoint.x) * partition);
        footPoint.setY(leftPoint.y + (rightPoint.y - leftPoint.y) * partition);

        return getDistanceBetweenPoint(footPoint, targetPoint);
    }

    public static Double getPointMultiply(Point vecA, Point vecB) {
        return vecA.x * vecB.x + vecA.y * vecB.y;
    }

//    /**
//     *
//     * minimum distance from targetPoint to the polygon
//     * @param polygon polygon，the outline
//     * @param targetPoint targetPoint to judge
//     * @return the minimumLen, if in polygon, return 0
//     */
//    public static Double minDistanceFromPointToPolygonUsingLine(List<Line> polygon, Point targetPoint) {
//        double minDis = 0;
//
//        if(polygon.size() < 3) {
//            return -1d;
//        }
//
//        for(int i = 1; i < polygon.size(); ++i) {
//            minDis = Math.min((minDistanceFromPointToLine(polygon.get(i).p1, polygon.get(i + 1).p2, targetPoint)), minDis);
//        }
//        return minDis;
//    }

    public static int rgba2int(String s) {
        if (StringUtils.isBlank(s)) {
            return 0;
        }
        if (!s.startsWith("rgba(") || !s.endsWith(")")) {
            return 0;
        }
        s = s.replace("rgba(", "").replace(")", "");
        String[] split = s.split(",");
        if (split.length != 4) {
            return 0;
        }
        int r = Integer.valueOf(split[0].trim());
        int g = Integer.valueOf(split[1].trim());
        int b = Integer.valueOf(split[2].trim());
        int a = (int) (255 * Float.valueOf(split[3].trim()));
        return ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    public static int rgba2int(int r, int g, int b, int a) {
        return ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point implements Serializable {

        private Double x;
        private Double y;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Line implements Serializable {

        private Point p1 = new Point();
        private Point p2 = new Point();

    }


}
