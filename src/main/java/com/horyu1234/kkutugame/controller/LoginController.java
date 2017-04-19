package com.horyu1234.kkutugame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by horyu on 2017-04-18.
 */
@Controller
public class LoginController {
    @RequestMapping("/login/naver")
    public String naver(String code, String state) {
        System.out.println("naver");
        System.out.println("code: " + code);
        System.out.println("state: " + state);

        return "redirect:http://kkutu-dev.horyu.me";
    }

    @RequestMapping("/login/google")
    public String google(String state, String code) {
        System.out.println("google");
        System.out.println("state: " + state);
        System.out.println("code: " + code);

        return "redirect:http://kkutu-dev.horyu.me";
    }

    @RequestMapping("/login/facebook")
    public String facebook(String code, String state) {
        System.out.println("facebook");
        System.out.println("code: " + code);
        System.out.println("state: " + state);

        return "redirect:http://kkutu-dev.horyu.me";
    }
}
