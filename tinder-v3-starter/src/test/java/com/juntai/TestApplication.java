package com.juntai;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.juntai.tinder.entity.Artillery;
import com.juntai.tinder.entity.User;
import com.juntai.tinder.entity.enums.GenderType;
import com.juntai.tinder.entity.enums.UserType;
import com.juntai.soulboot.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.ResourceBundle;


/**
 * @Description: test$
 * @Author: nemo
 * @Date: 2023/4/9 21:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestApplication {

    @Autowired
    public RedisTemplate redisTemplate;
    @Test
    public void redisTest(){

        User user = new User();
        user.setId(1L);
        user.setName("cww");
        user.setPassword("123456");
        user.setType(UserType.ADMIN);
        redisTemplate.opsForHash().put("test:cww:hash","001", JsonUtils.write(user));
        String o = (String)redisTemplate.opsForHash().get("test:cww:hash", "001");
        System.out.println(o);
        User user1 = JsonUtils.read(o,User.class);
        System.out.println(user1);

        Artillery artillery = new Artillery();
        artillery.setId("1");
        artillery.setName("cww");
        artillery.setCode("111");
        redisTemplate.opsForHash().put("test:cww:hash","002", JsonUtils.write(artillery));
        o = (String)redisTemplate.opsForHash().get("test:cww:hash", "002");
        System.out.println(o);
        Artillery artillery1 = JsonUtils.read(o,Artillery.class);
        System.out.println(artillery1);

        //redisTemplate.boundValueOps("test:cww").append("hello");
    }
    @Test
    public void initMyBatis() {
        //创建一个代码生成器
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/rocket?serverTimezone=Asia/Shanghai&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=utf8",
                        "root", "123456")
                //全局配置(GlobalConfig)
                .globalConfig(builder -> {
                    builder.author("nemo") // 设置作者，可以写自己名字
                            //.enableSwagger() // 开启 swagger 模式，这个是接口文档生成器，如果开启的话，就还需要导入swagger依赖
                            .fileOverride() // 覆盖已生成文件
                            .dateType(DateType.TIME_PACK) //时间策略
                            .commentDate("yyyy-MM-dd") //注释日期
                            .outputDir("/Users/nemo/work/project/tinder/tinder-rocket/tinder-rocket-starter/src/main/java"); // 指定输出目录，一般指定到java目录
                })
                //包配置(PackageConfig)
                .packageConfig(builder -> {
                    builder.parent("com.juntai") // 设置父包名
                            .moduleName("") // 设置父包模块名，这里一般不设置
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/nemo/work/project/tinder/tinder-rocket/tinder-rocket-starter/src/main/java/mapper")); // 设置mapperXml生成路径，这里是Mapper配置文件的路径，建议使用绝对路径
                })
                //策略配置(StrategyConfig)
                .strategyConfig(builder -> {
                    builder.addInclude("t_notice") ; // 设置过滤表前缀
                    builder.entityBuilder()
                            .enableLombok();

                    builder.serviceBuilder()
                            .formatServiceFileName("%sService") //设置service的命名策略,没有这个配置的话，生成的service和serviceImpl类前面会有一个I，比如IUserService和IUserServiceImpl
                            .formatServiceImplFileName("%sServiceImpl"); //设置serviceImpl的命名策略
                    builder.controllerBuilder()
                            .enableRestStyle(); // 开启生成@RestController 控制器，不配置这个默认是Controller注解，RestController是返回Json字符串的，多用于前后端分离项目。
                    builder.mapperBuilder()
                            .enableMapperAnnotation() ;//开启 @Mapper 注解，也就是在dao接口上添加一个@Mapper注解，这个注解的作用是开启注解模式，就可以在接口的抽象方法上面直接使用@Select和@Insert和@Update和@Delete注解。
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new VelocityTemplateEngine())
                .execute(); //执行以上配置
    }

}
