package com.example.dosirakbe.test;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

    @GetMapping("/builid/test")
    public String test(){
        return "ci/cd test";
    }
}
