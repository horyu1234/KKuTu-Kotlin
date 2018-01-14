package com.horyu1234.kkutuweb.controller;

import com.horyu1234.kkutuweb.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by horyu on 2018-01-12
 */
@Controller
public class LobbyController {
    @RequestMapping(value = "/")
    public String lobby(Model model) {
        model.addAttribute("locale", Locale.getWebLocales());
        model.addAttribute("viewName", "lobby");

        return "layout";
    }
}
