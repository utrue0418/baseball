package com.goldmund.demo1;

import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.charset.StandardCharsets;


@RestController
public class StockController {

    private final StockRepository stockRepository;
    private final StockAnalyzer stockAnalyzer; // 💡 구체적인 Premium/Basic 대신 인터페이스(부모)를 선언! (다형성)
    // 💡 시뮬레이터를 켜고, 끄기 위한 의존성(Dependency Injection) 주입
    private final MarketSimulator marketSimulator;

    // 💡 생성자를 통해 금고지기(Repository)를 주입받습니다.
    public StockController(StockRepository stockRepository, StockAnalyzer stockAnalyzer, MarketSimulator marketSimulator) {
        this.stockRepository = stockRepository;
        this.stockAnalyzer = stockAnalyzer;
        this.marketSimulator = marketSimulator;
    }

        // 1. 모든 주식 목록 조회 (DB에서 다 꺼내오기)
        @GetMapping("/stock")
        public String showStockInfo() {
            // 1. 방금 만든 설계도를 바탕으로 객체 생성 (예: 삼성증권)
            Stock myStock = new Stock("삼성증권", 40000.0, "KRW",15.0, 0.7);

            // 2. 객체 스스로 분석 결과를 내놓도록 명령
            String analysisResult = myStock.getAnalysis();

            // 3. HTML 태그를 사용해 웹페이지를 예쁘게 꾸며서 브라우저로 반환 (return)
            // %.0f는 소수점 없이, %.1f는 소수점 첫째 자리까지만 출력하라는 뜻입니다.
            return String.format(
                    "<h2>📊 %s 종목 분석 결과</h2>" +
                            "<p><b>현재가:</b> %.0f</p>" +"<b>%s</b>"+
                            "<p><b>EPS 성장률:</b> %.1f%%</p>" +
                            "<p><b>PEG 비율:</b> %.1f</p>" +
                            "<hr>" +
                            "<p style='color:blue;'><b>💡 AI 의견:</b> %s</p>",
                    myStock.name, myStock.currentPrice, myStock.currency, myStock.epsGrowthRate, myStock.pegRatio, analysisResult
            );
        }

    // 1. 모든 주식 목록 조회 (DB에서 다 꺼내오기)
    @GetMapping("/api/stocks")
    public List<Stock> getAllStocks() {

        List<Stock> stocks = stockRepository.findAll();
        // 각 주식마다 AI 분석 결과를 동적으로 덮어씌워서 반환합니다.
        for (Stock s : stocks) {
            // 💡 다형성: 현재 @Primary가 붙은 PremiumAnalyzer가 알아서 작동합니다!
            // 나중에 코드를 안 고쳐도 @Primary 위치만 바꾸면 전체 분석 로직이 통째로 바뀝니다.
            String result = stockAnalyzer.analyze(s);
            // (참고: 이를 프론트엔드로 보내려면 Stock 객체에 setAnalysis() 메서드 등 추가 필요)
        }
        return stocks;
    }

    // 2. 새로운 주식 등록 (DB에 저장하기)
    @PostMapping("/api/stock/add")
    public String addNewStock(@RequestBody Stock newStock) {
        // 💡 금고지기에게 저장하라고 시킵니다.
        Stock savedStock = stockRepository.save(newStock);
        return savedStock.getName() + " 종목이 DB에 ID " + savedStock.getId() + "번으로 저장되었습니다!";
    }

        /* @GetMapping("/api/stock")
        public List<Stock> getAllStocks() {
            return stockRepository.findAll();
        }
        public Stock getStockJson() {
            // 1. 객체 생성
            Stock myStock = new Stock("네이버", 190000.0, 12.5, 1.2);

            // 2. 글자(String)가 아니라 객체(Stock)를 그대로 return!
            return myStock;
        }

        @PostMapping("/api/stock/add")
        // @RequestBody: "들어오는 JSON 데이터를 자바 Stock 객체로 조립해 줘!" 라는 뜻
        public String addNewStock(@RequestBody Stock newStock) {

            // JSON으로 들어온 데이터가 이미 newStock 상자 안에 예쁘게 담겨 있습니다.
            System.out.println("새로운 종목이 수신되었습니다: " + newStock.name);
            System.out.println("입력된 주가: " + newStock.currentPrice);

            return newStock.name + " 종목 데이터가 서버에 성공적으로 등록되었습니다!";
        } */

    // 3. 주식 정보 수정 (Update - PUT)
    @PutMapping("/api/stock/update/{id}")
    public String updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
        // 금고에서 기존 데이터를 찾아서 없으면 에러를 냅니다.
        return stockRepository.findById(id)
                /* .map과 .orElseThrow()는 데이터가 없을 때 서버가 갑자기 뻗어버리는 대참사를 막고,
                코드를 읽기 쉽고 간결하게 만들어주는 아주 강력하고 현대적인 프로그래밍 기법 */
                .map(Stock -> {
                    Stock.setName(updatedStock.getName());
                    Stock.setCurrentPrice(updatedStock.getCurrentPrice());
                    // 💡 5. 수정할 때 통화 정보도 업데이트 되도록 추가
                    Stock.setCurrency(updatedStock.getCurrency());
                    Stock.setEpsGrowthRate(updatedStock.getEpsGrowthRate());
                    Stock.setPegRatio(updatedStock.getPegRatio());
                    stockRepository.save(Stock); // 수정된 내용 저장
                    return "ID " + id + "번 종목이 성공적으로 수정되었습니다!";
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 종목이 존재하지 않습니다."));
    }

    // 4. 주식 정보 삭제 (Delete - DELETE)
    @DeleteMapping("/api/stock/delete/{id}")
    public String deleteStock(@PathVariable Long id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return "ID " + id + "번 종목이 삭제되었습니다.";
        } else {
            throw new IllegalArgumentException("해당 종목이 존재하지 않습니다.");
        }
    }

    // 5. 전체 주식 데이터 CSV 파일로 다운로드 (파일 입출력)
    @GetMapping("/api/stocks/download")
    public ResponseEntity<byte[]> downloadStocksCsv() {

        // 1) DB에서 모든 주식 데이터를 꺼내옵니다.
        List<Stock> stockList = stockRepository.findAll();

        // 2) CSV 형식의 텍스트로 조립할 준비를 합니다. (메모리 버퍼)
        StringBuilder csvContent = new StringBuilder();

        // 엑셀에서 한글이 깨지지 않도록 하는 마법의 코드 (BOM 추가)
        csvContent.append('\ufeff');

        // 3) CSV 헤더(첫 줄) 작성
        csvContent.append("ID,종목명,현재가,EPS성장률,PEG비율,AI분석결과\n");

        // 4) 데이터 반복문을 돌면서 한 줄씩 텍스트로 이어 붙입니다.
        for (Stock stock : stockList) {
            csvContent.append(stock.getId()).append(",")
                    .append(stock.getName()).append(",")
                    .append(stock.getCurrentPrice()).append(",")
                    .append(stock.getEpsGrowthRate()).append(",")
                    .append(stock.getPegRatio()).append(",")
                    .append(stock.getAnalysis()).append("\n");
        }

        // 5) 만들어진 텍스트를 진짜 '파일(바이트 덩어리)' 형태로 변환합니다.
        byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);

        // 6) 브라우저에게 "이건 화면에 띄우지 말고 파일로 다운로드해!"라고 명령하는 봉투(헤더)를 만듭니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        // 다운로드될 파일 이름을 지정합니다.
        headers.setContentDispositionFormData("attachment", "stock_analysis_report.csv");

        // 7) 최종적으로 파일 데이터와 봉투를 함께 전송합니다.
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(csvBytes);
    }

    // 3. 외부(컨트롤러 등)에서 스위치를 끌 수 있는 버튼(메서드) 제공
    @PostMapping("/api/simulator/stop")
    public String stopSimulator() {
        //this.isRunning = false;
        marketSimulator.stopSimulator();
        return "시뮬레이터가 안전하게 종료되었습니다.";

    }

    // 4. 외부(컨트롤러 등)에서 스위치를 켤 수 있는 버튼(메서드) 제공
    @PostMapping("/api/simulator/start")
    public String startSimulator() {
        marketSimulator.startSimulator();
        return "시뮬레이터가 시작되었습니다.";

    }


    }
