package com.example.dosirakbe.annotations;

import com.example.dosirakbe.factory.WithMockCustomUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "kakao 3757381275";
    String name() default "이유진";
    String email() default "leeyuin1231@naver.com";

    String profileImg() default "https://example.com/profile.jpg";



}
