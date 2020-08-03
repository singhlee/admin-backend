package org.singhlee.admin.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: admin-backend
 * @description:
 * @author: singhlee
 * @date: 2020-07-02 09:34
 **/
public class EncodePass {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        System.out.print(bCryptPasswordEncoder.encode("123456"));
    }
}
