package com.goldmund.demo1;

import org.springframework.data.jpa.repository.JpaRepository;

// 💡 JpaRepository<엔티티이름, ID타입>을 상속받으면 모든 준비 끝!
public interface StockRepository extends JpaRepository<Stock, Long> {
}
