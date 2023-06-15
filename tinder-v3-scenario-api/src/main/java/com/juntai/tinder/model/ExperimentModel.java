package com.juntai.tinder.model;

import com.juntai.tinder.entity.Experiment;
import com.juntai.tinder.entity.MapPoint;
import com.juntai.tinder.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nemo
 * @date: 2022/05/20/5:24 下午
 * @description:
 */
@Data
public class ExperimentModel implements Serializable {

    private static final long serialVersionUID = 3775073762141983451L;

    private Experiment experiment;

    private List<User> personsRed = new ArrayList<>();

    private List<User> personsBlue = new ArrayList<>();

    private List<User> personsWhite = new ArrayList<>();

    private MapPoint point;

}
