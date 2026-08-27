package com.goldmund.demo1;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Random;

@Service
public class MarketSimulator {

    private final StockRepository stockRepository;

    public MarketSimulator(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // 1. 전원 스위치 역할을 할 변수 (기본값: ON)
    private boolean isRunning = true;

    // @PostConstruct: 스프링 서버가 켜지면 이 메서드를 자동으로 실행하라는 뜻
    //@PostConstruct
    public void startSimulation() {

        // 💡 [배열과 반복문 활용] 1. 서버가 켜질 때 DB가 비어있으면 초기 데이터 자동 세팅
        if (stockRepository.count() == 0) {

            // 배열(Array): 여러 개의 데이터를 하나의 변수 기차에 칸칸이 담습니다.
            //String[] defaultNames = {"애플", "테슬라", "엔비디아", "마이크로소프트", "구글"};
            //Double[] defaultPrices = {230.0, 250.0, 120.0, 420.0, 180.0};

            // 💡 객체 배열(Object Array): 문자와 숫자가 한 쌍으로 안전하게 관리됩니다!
            Stock[] defaultStocks = {
                    new Stock("애플", 230.0, "USD"),
                    new Stock("테슬라", 250.0, "USD"),
                    new Stock("엔비디아", 120.0, "USD"),
                    new Stock("마이크로소프트", 420.0, "USD"),
                    new Stock("삼성전자", 80000.0, "KRW"), // 🇰🇷 한국 주식 추가!
                    new Stock("구글", 180.0, "USD")
            };

            // 반복문(for문): 배열의 길이(5번)만큼 똑같은 작업을 반복합니다.
            /* for (int i = 0; i < defaultNames.length; i++) {
                // i가 0부터 4까지 변하면서 배열의 데이터를 하나씩 쏙쏙 빼옵니다.
                // ('이름과 가격만 받는 오버로딩 생성자'가 여기서 빛을 발합니다!)
                Stock defaultStock = new Stock(defaultNames[i], defaultPrices[i]);
                stockRepository.save(defaultStock);
            } */

            // 향상된 for문 (for-each): defaultStocks 배열 안의 Stock들을 처음부터 끝까지 하나씩 쏙쏙 빼옵니다.
            for (Stock defaultStock : defaultStocks) {
                stockRepository.save(defaultStock);
            }
            System.out.println("🚀 배열과 반복문을 이용해 " + defaultStocks.length + "개의 초기 주식이 자동 등록되었습니다!");
        }

        // 💡 1. 새로운 직원(Thread)을 고용합니다.
        Thread marketThread = new Thread(() -> {
            Random random = new Random();

            // 2. 무한 루프 (주식 시장은 계속 돌아갑니다)
            while (isRunning) {
                try {
                    Thread.sleep(5000); // 💡 5초마다 한 번씩 일함

                    // 3. DB에서 모든 주식을 꺼내서 가격을 ±2% 랜덤하게 변동시킴
                    List<Stock> stocks = stockRepository.findAll();
                    for (Stock stock : stocks) {
                        if (stock.getCurrentPrice() != null) {
                            double fluctuation = 1.0 + (random.nextDouble() * 0.04 - 0.02); // -2% ~ +2%
                            double newPrice = Math.round(stock.getCurrentPrice() * fluctuation);
                            stock.setCurrentPrice(newPrice);
                            stockRepository.save(stock); // 변경된 가격 DB에 덮어쓰기
                        }
                    }
                    if(!stocks.isEmpty()){
                        System.out.println("📈 [스레드] 시장 가격이 업데이트되었습니다!");
                    }

                } catch (InterruptedException e) {
                    System.out.println("주식 시장 시뮬레이터가 예기치 않은 오류로 중지되었습니다.");
                    break;
                }
            }
            System.out.println("시뮬레이터가 안전하게 종료되었습니다.");
        });

        // 💡 4. 직원에게 업무 시작 지시! (메인 서버 동작과 상관없이 뒤에서 돕니다)
        marketThread.start();
    }

    // 💡 3. 외부에서 스위치를 끄는 전용 메서드
    public void stopSimulator() {
        this.isRunning = false;
        //startSimulation();
    }

    // 💡 4. 외부에서 스위치를 켜는 전용 메서드
    public void startSimulator() {
        this.isRunning = true;
        startSimulation();

    }


}