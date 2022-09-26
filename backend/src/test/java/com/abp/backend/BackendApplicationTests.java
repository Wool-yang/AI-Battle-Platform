package com.abp.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123"));
        System.out.println(bCryptPasswordEncoder.matches("123", "$2a$10$bX.di0Tkpw5veoNsbhqGP.qajiLXiwf067wEwcX1PoKx63fh8ETv2"));
    }

}
