package com.goldmund.demo1;

import org.springframework.stereotype.Component;

@Component
public class BasicAnalyzer implements StockAnalyzer {
    @Override
    public String analyze(Stock stock) {
        if (stock.getPegRatio() != null && stock.getPegRatio() < 1.0) {
            return "저평가 매수 추천 (Basic)";
        }
        return "관망 추천 (Basic)";
    }
}
