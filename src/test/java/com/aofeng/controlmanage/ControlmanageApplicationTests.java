package com.aofeng.controlmanage;

import com.aofeng.controlmanage.entity.Command;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ControlmanageApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        String[] a=" a   b 3  \n 4 ".trim().split("\\s+");
    }

}
