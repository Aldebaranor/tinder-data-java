package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationTemRadar implements Serializable {


    private static final long serialVersionUID = -32012917943971060L;
    private String id;
    private String instId;
    /**
     * 0雷达，1红外，2，无源，3干扰，4通信覆盖范围
     * 扇形的雷达填充到sector,包络线的填充到points
     */
    private Integer type;
    /**
     * [[{起始角度}-{终止角度}#距离,{起始角度}-{终止角度}#距离,高度]]
     * //兼容空间三维包络，角度按顺时针增加，330-30代表330度到360度加上0度到30度
     * //最后一位代表高度，如果为0代表水平面，如果为-1代表纵面，其他代表高度，单位米
     * //示例：扇形[[330_30#2000,0],[330_30#2000,-1]] 二维数据，第一组数据是水平的夹角，第二个数据是高低夹角
     * //示例：圆形[[0_360#2000,0]]
     * //示例：球形[[0_360#2000,0],[0_360#2000,-1]]
     * //示例：包络线[[0#1000,1#1020,3_360#2000,1000]]
     */
    private String[][] points;

    private Long time;


}
