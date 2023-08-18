package com.unitechs.biz;

import com.unitechs.framework.init.UniosApplication;
import com.unitechs.framework.scan.UniOSCloud;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liujie
 * @description 四川移动运维项目启动类
 * @date 2022-06-06 15:41
 **/
@UniOSCloud
@SpringBootApplication
@EnableSwagger2
public class BizRestServiceBootStrap {

    public static void main(String[] args) {
        UniosApplication.run("BizRestServiceBootStrap", BizRestServiceBootStrap.class, args);
    }
}
