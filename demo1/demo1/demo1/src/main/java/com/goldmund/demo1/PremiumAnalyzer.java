package com.goldmund.demo1;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

@Component
@Primary // 💡 스프링에게 "분석기 리모컨을 누르면 기본적으로 얘를 실행해!" 라고 알려줌
public class PremiumAnalyzer implements StockAnalyzer {
    @Override
    public String analyze(Stock stock) {
        // 나중에는 여기에 구글 Gemini API를 호출하는 코드가 들어갑니다!
        return "🔥 [프리미엄 AI 분석] EPS 성장률 " + stock.getEpsGrowthRate() + "% 기준, 강력 매수 추천!";
    }
}