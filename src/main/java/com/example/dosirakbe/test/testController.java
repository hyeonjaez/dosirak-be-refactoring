package com.example.dosirakbe.test;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class testController {

    @ResponseBody
    @GetMapping("/builid/test")
    public String test(){
        return "ci/cd test";
    }
}
