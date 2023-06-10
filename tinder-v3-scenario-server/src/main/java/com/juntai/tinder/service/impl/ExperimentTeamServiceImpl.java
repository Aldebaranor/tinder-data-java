package com.juntai.tinder.service.impl;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ExperimentTeamCondition;
import com.juntai.tinder.entity.ExperimentTeam;
import com.juntai.tinder.mapper.ExperimentTeamMapper;
import com.juntai.tinder.service.ExperimentTeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ExperimentTeamServiceImpl  implements ExperimentTeamService {

    @Override
    public ExperimentTeam getById(String id) {
        return null;
    }

    @Override
    public String insert(ExperimentTeam experimentTeam) {
        experimentTeam.setCreator(UserContext.username());
        experimentTeam.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(experimentTeam);
    }

    @Override
    public void insertList(List<ExperimentTeam> list) {
        for (ExperimentTeam experimentTeam : list) {
            experimentTeam.setCreator(UserContext.username());
            experimentTeam.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        super.insertList(list);
    }

    @Override
    public void update(ExperimentTeam experimentTeam) {

    }

    @Override
    public void updateListWithExperimentId(String experimentId, List<ExperimentTeam> list) {
        experimentTeamRepository.delete(SingleClause.equal("experimentId", experimentId));
        super.insertList(list);
    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public Pagination<ExperimentTeam> page(Query<ExperimentTeamCondition, ExperimentTeam> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return 0;
    }

    @Override
    public List<ExperimentTeam> list(ExperimentTeamCondition condition) {
        //降序 等级高的数值大
        return seek(condition).stream().collect(Collectors.toList());
    }

    @Override
    public boolean checkAuthorization(String experimentId) {
        Experiment experiment = Optional.ofNullable(experimentRepository.getById(experimentId))
                .orElseThrow(() -> ExceptionUtils.api("试验不存在"));
        // 试验创建者
        if (UserContext.username().equals(experiment.getCreator())) {
            return true;
        }

        String personId = UserContext.personId();
        if (StringUtils.isBlank(personId)) {
            throw ExceptionUtils.api("当前账号未绑定用户");
        }
        List<ExperimentTeam> teams = super.query(SingleClause.equal("personId", personId));
        if (CollectionUtils.isEmpty(teams)) {
            return false;
        }
        ExperimentTeam experimentTeam = teams.stream().filter(q -> StringUtils.equals(q.getExperimentId(), experimentId)).findFirst().orElse(null);
        if (experimentTeam == null) {
            return true;
        }
        return false;
    }

    @Override
    public String getTeam(String experimentId) {
        Experiment experiment = Optional.ofNullable(experimentRepository.getById(experimentId))
                .orElseThrow(() -> ExceptionUtils.api("试验不存在"));
        // 试验创建者
        if (UserContext.username().equals(experiment.getCreator())) {
            return TeamType.WHITE.getValue();
        }
        String personId = UserContext.personId();
        if (StringUtils.isBlank(personId)) {
            throw ExceptionUtils.api("当前账号未绑定用户");
        }
        ExperimentTeamCondition condition = new ExperimentTeamCondition();
        condition.setExperimentId(experimentId);
        condition.setPersonId(personId);
        List<ExperimentTeam> query = super.query(condition);
        if (CollectionUtils.isEmpty(query)) {
            throw ExceptionUtils.api("当前账号没有访问权限");
        }
        String team = query.get(0).getTeam();
        return StringUtils.isEmpty(team) ? TeamType.WHITE.getValue() : team;
    }
}
