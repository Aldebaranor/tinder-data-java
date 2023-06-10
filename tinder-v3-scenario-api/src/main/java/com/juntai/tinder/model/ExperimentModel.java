package com.juntai.tinder.model;

import com.egova.entity.Person;
import com.soul.tinder.entity.Experiment;
import com.soul.tinder.entity.MapPoint;
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

    private List<Person> personsRed = new ArrayList<>();

    private List<Person> personsBlue = new ArrayList<>();

    private List<Person> personsWhite = new ArrayList<>();

    private MapPoint point;

}
