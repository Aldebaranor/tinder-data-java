package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.RcsDataCondition;
import com.juntai.tinder.entity.RcsData;
import com.juntai.tinder.service.RcsDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class RcsDataServiceImpl  implements RcsDataService {

    @Override
    public RcsData getById(String id) {
        return null;
    }

    @Override
    public String insert(RcsData entity) {
        return null;
    }

    @Override
    public void insertList(List<RcsData> list) {

    }

    @Override
    public void update(RcsData entity) {

    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public Pagination<RcsData> page(Query<RcsDataCondition, RcsData> model) {
        return null;
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return 0;
    }

    @Override
    public List<RcsData> list(RcsDataCondition condition) {
        return null;
    }
}
