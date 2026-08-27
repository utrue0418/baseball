package com.goldmund.demo1;

// 💡 다형성을 위한 부모 리모컨 (인터페이스)
public interface StockAnalyzer {
    String analyze(Stock stock); // "분석해라!" 라는 버튼 하나만 뚫어놓습니다.
}