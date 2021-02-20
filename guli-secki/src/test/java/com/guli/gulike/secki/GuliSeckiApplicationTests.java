package com.guli.gulike.secki;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;

//@SpringBootTest
class GuliSeckiApplicationTests {

    @Test
    void contextLoads() {
    }
        // 查看springBoot 和spring的版本

    @Test
    public void TestspringVersionAndspringBootVersion (){
        String springVersion = SpringVersion.getVersion();
        String springBootVersion = SpringBootVersion.getVersion();
        System.out.println(springVersion+"----------"+springBootVersion);
    }

}
