package com.goldmund.demo1;

import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {

        String name = "홍길동";

        return String.format("안녕하세요 %s님! 스프링 서버에 오신 것을 환영합니다.", name);
        // %s 자리에 visitorName이, %d 자리에 visitCount가 쏙 들어갑니다.
        //return String.format("환영합니다, %s님! %d번째 방문이시네요.", visitorName, visitCount);

            /* int total_sum=0, even_num=0, odd_num=0;

            for(int i=1; i<=100; i++) {
                total_sum += i;
                if( (i%2) == 0 ) {
                    even_num += i;
                } else { odd_num += i; }
            }

            return String.format("총합 = %d, 홀수값 합 = %d, 짝수값 합=%d", total_sum, odd_num, even_num );
            } */

            /* int row;
            int height;
            double area1;

            row = 5;
            height = 7;
            area1 = (row*height)/2.0;

            // 문자열과 변수를 + 기호로 조립해서 반환
            //return "밑변이 " + row + "이고 높이가 " + height + "인 삼각형의 면적은 " + area1 + "입니다"; */

    }
}