package com.example.dosirakbe.domain.user.generator;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * packageName    : com.example.dosirakbe.domain.user.generator<br>
 * fileName       : NickNameGenerator<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 사용자 닉네임을 생성하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */



@Getter
@Component
public class NickNameGenerator {

    /**
     * 랜덤 닉네임을 생성합니다.
     * <p>
     * 형용사와 동물 이름을 랜덤으로 선택한 후, 1부터 99 사이의 랜덤 숫자를 결합하여 닉네임을 생성합니다.
     * </p>
     *
     * @return 생성된 닉네임 문자열
     */

    private String nickName;

    List<String> adjective = Arrays.asList("용기있는", "노력하는", "친환경적인", "에너지를절약하는", "지속가능한", "실천하는");
    List<String> object = Arrays.asList("북극곰" ,"바다물범", "사자", "판다", "호랑이", "표범", "강아지", "고양이", "햄스터", "오리", "펭귄", "백호");
    public NickNameGenerator(){

        String number = (int)(Math.random() * 99)+1 +"";

        Collections.shuffle(adjective);
        Collections.shuffle(object);

        String adj = adjective.get(0);
        String OName = object.get(0);
        this.nickName = adj+OName+number;

    }

}
