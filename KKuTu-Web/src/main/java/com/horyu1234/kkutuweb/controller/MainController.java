package com.horyu1234.kkutuweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by horyu on 2018-01-12
 */
@Controller
public class MainController {
    @RequestMapping(value = "/")
    public String main() {
        return "test";
    }
}
