package ru.starashchuk.shopping.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String redirectForFrontend(){
        return "redirect:/index.html";
    }

}
