package com.example.dosirakbe.domain.user.generator;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Component
public class NickNameGenerator {

    private String nickName;

    List<String> adjective = Arrays.asList("용기있는", "노력하는", "친환경적인", "에너지를절약하는", "지속가능한");
    List<String> object = Arrays.asList("북극곰" ,"바다물범", "사자", "판다", "호랑이", "표범", "강아지", "고양이");
    public NickNameGenerator(){

        String number = (int)(Math.random() * 99)+1 +"";

        Collections.shuffle(adjective);
        Collections.shuffle(object);

        String adj = adjective.get(0);
        String OName = object.get(0);
        this.nickName = adj+OName+number;

    }

}
