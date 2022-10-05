package com.kmong.api.order.service;

import com.kmong.api.order.request.OrderCreate;
import com.kmong.api.order.request.OrderSearch;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    /**
     * 주문 생성
     * @param orderCreate 생성할 주문정보
     * @return 주문 생성 결과
     */
    ResponseEntity createOrder(OrderCreate orderCreate);

    /**
     * 주문내역 전체 조회
     * @param id 주문내역을 조회할 회원 아이디
     * @return 주문내역 조회 결과
     */
    ResponseEntity findAllOrder(OrderSearch orderSearch);
}
