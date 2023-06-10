package com.juntai.tinder.task;

import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.EquipmentTask;
import com.juntai.tinder.entity.enums.CategoryType;
import com.juntai.tinder.facade.EquipmentFacade;
import com.juntai.tinder.service.EquipmentTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
* @Description:
* @Author: nemo
* @Date: 2023/2/9
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentTaskInitTask {

    private final EquipmentFacade equipmentFacade;

    private final EquipmentTaskService equipmentTaskService;

    @Scheduled(fixedRateString = "PT10M")
    public void init() {
        log.info("刷新装备可执行任务信息开始");
        try {
            List<Equipment> all = equipmentFacade.getAll();
            List<EquipmentTask> tasks = new ArrayList<>();
            for(Equipment equipment : all) {
                if (equipment.getType() != CategoryType.ARM) {
                    continue;
                }
                EquipmentTask byId = equipmentTaskService.getById(equipment.getId());
                if (byId == null) {
                    EquipmentTask newTask = new EquipmentTask();
                    newTask.setEquipmentId(equipment.getId());
                    newTask.setEquipmentName(equipment.getName());
                    newTask.setTasks("[]");
                    tasks.add(newTask);
                } else {
                    if (!StringUtils.equals(equipment.getName(), byId.getEquipmentName())) {
                        HashMap<String, Object> map = new HashMap<>(16);
                        map.put("equipmentName", equipment.getName());
                        equipmentTaskService.modify(byId.getId(), map);
                    }
                }
            }
            equipmentTaskService.insertList(tasks);
        } catch (Exception e) {
            log.error("刷新装备可执行任务信息出错", e);
        }
        log.info("刷新装备可执行任务信息结束");
    }
}
