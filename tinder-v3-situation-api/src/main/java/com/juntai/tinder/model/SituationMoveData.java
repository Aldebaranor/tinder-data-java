package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description:兵力运动态势
 * @Author: nemo
 * @Date: 2022/3/25
 */
@Data
public class SituationMoveData implements Serializable {

    private static final long serialVersionUID = 8914261778188430685L;
    /**
     * 兵力ID
     */
    private String id;
    /**
     * 态势时间
     */
    private Long time;
    private MoveData move;
    private MoveData moveDetect;

    @Override
    public String toString() {
        return String.format("%s@%s@%s@%s", id, time,
                move == null ? "" : move.toString(),
                moveDetect == null ? "" : moveDetect.toString());
    }
}
