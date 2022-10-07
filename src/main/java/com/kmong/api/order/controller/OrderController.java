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

import static com.kmong.api.common.constant.Constant.SESSION_ATTRIBUTE_MEMBER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/create")
    public ResponseEntity createOrder(HttpSession session, @RequestBody @Valid OrderCreate orderCreate) {
        if (!isSameMember(session, orderCreate.getMemberId())) {
            throw new InconsistentMemberException();
        }
        return orderService.createOrder(orderCreate);
    }

    @GetMapping("/order/list")
    public ResponseEntity findAllOrder(HttpSession session, @ModelAttribute OrderSearch orderSearch) {
        if (!isSameMember(session, orderSearch.getMemberId())) {
            throw new InconsistentMemberException();
        }
        return orderService.findAllOrder(orderSearch);
    }

    private boolean isSameMember(HttpSession session, String memberId) {
        return session.getAttribute(SESSION_ATTRIBUTE_MEMBER_ID).equals(memberId);
    }

}
