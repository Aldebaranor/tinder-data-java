package com.juntai.tinder.service;

import com.juntai.tinder.entity.IotdbForce;
import com.juntai.tinder.entity.IotdbForcePosture;
import com.juntai.tinder.protobuf.proto.TinderProto;
import io.vertx.core.impl.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Song
 * @Date 2023/6/21 17:09
 */

@Service
@Slf4j
public class SaveMessage {

    private final ConcurrentHashMap<String,IotdbForce> msgCache = new ConcurrentHashMap();
    private final ConcurrentHashSet<String> idCache = new ConcurrentHashSet<>();

    public void SaveAsCache(String simId, TinderProto.NextStepSync msg){
        String key = simId + "@" + msg.getSimTime();
        //这里是维护一个所有想定Id的缓存
        //IdCache存在simId的key？
        Boolean containId = idCache.contains(simId);
        //simId存入IdCache
        idCache.add(simId);

        //对zmq可能把数据分两个包发送进行合并处理
//        contain, _ := MsgCache.Contains(ctx, fKey)
        Boolean contain = msgCache.containsKey(key);
        if(contain){
            //取出数据
            IotdbForce half = msgCache.get(key);

            msg.getForcesList();
        }else {

        }
//        if contain {
//            halfVar, _ := MsgCache.Get(ctx, fKey)
//            msgBytes, _ := json.Marshal(halfVar)
//            var forces entity.IotDbForce
//            json.Unmarshal(msgBytes, &forces)
//            //msg.Data转Force
//            //合并forces数据存msgMache
//            forces = addForce(msg, forces)
//            //fKey存入MsgCache，60s超期，用于进行持久化
//            MsgCache.Set(ctx, fKey, forces, time.Second*60)
//        } else {
//
//            forces := msgToCache(msg)
//            //MsgCache存入第一批数据
//            MsgCache.Set(ctx, fKey, forces, time.Second*60)
//        }



    }

    public IotdbForce addForce(TinderProto.NextStepSync data,IotdbForce msg){
        List<TinderProto.ForceProperty> forcesList = data.getForcesList();
        for(TinderProto.ForceProperty force:forcesList){
            for(IotdbForcePosture m:msg.getForces()){
                if(force.getForceId()==m.ForceId){
                    m.setLon(force.getLon());
                    m.setAlt(force.getAlt());
                    m.setLat(force.getLat());
                    m.setHeading(force.getHeading());
                    m.setPitch(force.getPitch());
                    m.setSpeed(force.getSpeed());
                }
            }
        }
        //TODO:确认protobuf
//        msg.setStage(data.);
        return msg;
    }
}
