package com.kmong.api.order.controller;

import com.kmong.api.order.exception.InconsistentMemberException;
import com.kmong.api.order.request.OrderCreate;
import com.kmong.api.order.request.OrderSearch;
import com.kmong.api.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/create")
    public ResponseEntity createOrder(@RequestBody @Valid OrderCreate orderCreate) {
        return orderService.createOrder(orderCreate);
    }

    @GetMapping("/order/list")
    public ResponseEntity findAllOrder(HttpSession session, @ModelAttribute OrderSearch orderSearch) {
        if (!session.getAttribute("id").equals(orderSearch.getMemberId())) {
            throw new InconsistentMemberException();
        }
        return orderService.findAllOrder(orderSearch);
    }

}
