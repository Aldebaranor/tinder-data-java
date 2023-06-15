package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.juntai.tinder.entity.EquipmentDetail;
import com.juntai.tinder.mapper.EquipmentDetailMapper;
import com.juntai.tinder.service.EquipmentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 装备模型库用来显示的 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class EquipmentDetailServiceImpl implements EquipmentDetailService {
    @Autowired
    private EquipmentDetailMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public EquipmentDetail getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public int update(EquipmentDetail entity) {
        return mapper.updateById(entity);
    }

    @Override
    public String insert(EquipmentDetail entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }


    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public void updateAttributeInfo(String id, String attributeInfo) {

        LambdaUpdateWrapper<EquipmentDetail> wrapper = new LambdaUpdateWrapper<EquipmentDetail>().eq(EquipmentDetail::getId, id)
                .set(EquipmentDetail::getAttributeInfo, attributeInfo);
        mapper.update(null, wrapper);
    }
}
