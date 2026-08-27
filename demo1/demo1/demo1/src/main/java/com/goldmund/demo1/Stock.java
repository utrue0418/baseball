package com.goldmund.demo1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import com.fasterxml.jackson.annotation.JsonIgnore;
// 💡 Table 어노테이션 임포트 추가
import jakarta.persistence.Table;

@Entity // 💡 1. "이 클래스는 DB 테이블과 똑같이 생겼어!" 라고 알려주는 이름표
// 💡 직접 만드신 "stocks" 테이블 이름과 연결해줍니다. (기본값은 클래스명인 stock)
@Table(name = "stocks")
public class Stock {
    // 웹에서 변수에 접근할 수 있도록 public을 붙여줍니다.

    @Id // 💡 2. 데이터들을 구분할 고유한 '주민번호(Primary Key)' 역할
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 1, 2, 3... 자동으로 번호표 발급
    public Long id;
    public String name;
    public Double currentPrice;
    // 💡 1. 통화(Currency) 정보를 담을 새로운 필드 추가 ("KRW" 또는 "USD")
    public String currency;
    public Double epsGrowthRate;
    public Double pegRatio;


    // 💡 1. 텅 빈 기본 생성자가 있어야 담을 수 있음
    public Stock() {}

    // 2. 모든 데이터를 다 넣는 생성자 (기존)
    // 💡 참고: 데이터 입력용 생성자에는 id를 넣지 않습니다. DB가 알아서 번호를 매겨주니까요!
    public Stock(String name, Double price, String currency, Double eps, Double peg) {
        this.name = name;
        this.currentPrice = price;
        this.currency = currency;
        this.epsGrowthRate = eps;
        this.pegRatio = peg;
    }

    // 💡 3. 이름과 가격만 아는 경우를 위한 생성자 (오버로딩!)
    // EPS와 PEG는 0.0으로 기본값을 세팅해 줍니다.
    public Stock(String name, Double price, String currency) {
        this.name = name;
        this.currentPrice = price;
        this.currency = currency;
        this.epsGrowthRate = 0.0;
        this.pegRatio = 0.0;
    }


    // 💡 3. 새로 추가된 id에 대한 정식 출입문(Getter/Setter)도 추가해 줍니다.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; } // 수정(Update) 시 꼭 필요함!

    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }

    // 💡 3. currency에 대한 Getter와 Setter 추가
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getEpsGrowthRate() { return epsGrowthRate; }
    public void setEpsGrowthRate(Double epsGrowthRate) { this.epsGrowthRate = epsGrowthRate; }

    public Double getPegRatio() { return pegRatio; }
    public void setPegRatio(Double pegRatio) { this.pegRatio = pegRatio; }


    // 핵심: System.out.println 대신 문자열(String)을 반환(return)하도록 바꿨습니다.
    //@JsonIgnore
    // 💡 @JsonIgnore 삭제됨! 이제 프론트엔드로 데이터가 넘어갑니다.
    public String getAnalysis() {
        // null 방어막 (값이 하나라도 비어있으면 계산하지 않음)
        if (pegRatio != null && epsGrowthRate != null) {
            if (pegRatio < 1.0 && epsGrowthRate >= 10.0) {
                return "저평가 우량주 (매수 추천)"; // HTML 코드에서 '매수'라는 단어를 찾아서 빨간색으로 칠해줍니다!
            }
        }
        return "관망을 추천합니다.";
    }

    /* public String getAnalysis() {
        if (pegRatio < 1.0 && epsGrowthRate >= 10.0) {
            return "저평가 우량주입니다. 매수를 적극 고려해 볼 수 있습니다.";
        } else {
            return "현재 가격이 적정하거나 고평가되었을 수 있습니다. 관망을 추천합니다.";
        }
    } */
}
