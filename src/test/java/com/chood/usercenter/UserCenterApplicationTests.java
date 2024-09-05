package com.chood.usercenter;

import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void testDigest() {
        String password = DigestUtil.md5Hex("123456", StandardCharsets.UTF_8);
        Assertions.assertNotNull(password);

        System.out.println(password);
    }

}
