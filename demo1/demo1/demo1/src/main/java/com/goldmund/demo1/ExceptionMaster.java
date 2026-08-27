package com.goldmund.demo1;

// 💡 3번 실습을 위한 가짜 네트워크 연결 클래스입니다.
// AutoCloseable 이라는 것을 구현(implements)해야 자동으로 close() 마법이 발동합니다.
class DummyNetwork implements AutoCloseable {
    public void connect() {
        System.out.println("🔌 [네트워크] 증권거래소에 연결되었습니다.");
    }

    // 이 메서드는 우리가 직접 호출하지 않아도 자바가 알아서 호출해 줄 것입니다!
    @Override
    public void close() {
        System.out.println("🔒 [네트워크] 증권거래소 연결이 안전하게 해제(Close)되었습니다.");
    }
}

public class ExceptionMaster {

    public static void main(String[] args) {
        System.out.println("=== 🚀 예외 처리 실습을 시작합니다 ===\n");

        // 실습 1 & 2: try-catch-finally 와 throw
        System.out.println("--- [실습 1] 정상적인 주가 입력 ---");
        updateStockPrice(50000);

        System.out.println("\n--- [실습 2] 마이너스 주가 입력 (일부러 에러 발생) ---");
        updateStockPrice(-1000);

        System.out.println("\n--- [실습 3] 자원 자동 반납 (try-with-resources) ---");
        autoCloseResourcePractice();

        System.out.println("\n=== 🎉 모든 프로그램이 멈추지 않고 무사히 종료되었습니다 ===");
    }

    // 💡 가격을 업데이트하는 메서드
    public static void updateStockPrice(int price) {
        try {
            System.out.println("1. 가격 업데이트를 시도합니다. (입력값: " + price + ")");

            // 💡 throw 실습: 가격이 음수면 내가 직접 폭탄(에러)을 던집니다!
            if (price < 0) {
                throw new IllegalArgumentException("주식 가격은 마이너스가 될 수 없습니다!");
            }

            // 에러가 안 났다면 아래 코드가 정상 실행됩니다.
            System.out.println("2. DB에 가격(" + price + "원)을 성공적으로 저장했습니다.");

        } catch (IllegalArgumentException e) {
            // 💡 catch: 위에서 던진 폭탄(IllegalArgumentException)을 여기서 받아서 수습합니다.
            System.out.println("🚨 [에러 발생 수습 중] " + e.getMessage());

        } finally {
            // 💡 finally: 에러가 났든, 정상 저장되었든 무조건 마지막에 실행됩니다.
            System.out.println("3. [Finally] 가격 업데이트 작업(메서드)을 종료합니다.");
        }
    }

    // 💡 try-with-resources (자동 close) 실습 메서드
    public static void autoCloseResourcePractice() {
        // try 옆의 소괄호 ( ) 안에 자원(네트워크, 파일 등)을 엽니다.
        try (DummyNetwork network = new DummyNetwork()) {
            network.connect();
            System.out.println("데이터를 다운로드하는 중...");

            // 여기서 통신 중 에러가 발생한다고 가정해봅시다!
            throw new RuntimeException("통신 중 갑작스러운 인터넷 끊김!");

        } catch (Exception e) {
            System.out.println("🚨 통신 에러 수습: " + e.getMessage());
        }
        // 💡 중요: finally 구문이 없고 network.close()를 직접 안 썼지만,
        // 이 블록을 빠져나갈 때 콘솔 창을 보면 알아서 close()가 실행된 것을 볼 수 있습니다!
    }
}