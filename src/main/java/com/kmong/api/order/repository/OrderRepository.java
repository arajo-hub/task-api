package com.kmong.api.order.repository;

import com.kmong.api.order.domain.Order;
import com.kmong.api.order.domain.OrderProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

import static com.kmong.api.order.domain.QOrder.order;
import static com.kmong.api.product.domain.QProduct.product;

@Slf4j
@Repository
@Transactional
public class OrderRepository {

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 주문 생성
     * @param order 생성할 주문 정보
     */
    public void createOrder(Order order) {
        em.persist(order);
    }

    /**
     * 주문 전체 삭제
     */
    public void deleteAll() {
        queryFactory.delete(order).execute();
    }

    /**
     * 주문내역 전체 조회
     * @param id 주문내역을 조회할 회원 아이디
     * @return 주문내역 리스트
     */
    public List<Order> findAllOrder(String id) {
        log.info("주문수" + countOrder());
        return queryFactory.selectFrom(order)
                .where(isEqualsToMemberId(id))
                .orderBy(order.id.asc())
                .fetch();
    }

    private BooleanExpression isEqualsToMemberId(String id) {
        return StringUtils.isNullOrEmpty(id) ? null : order.member.id.equalsIgnoreCase(id);
    }

    public int countOrder() {
        Long cnt = queryFactory.select(order.id.count())
                .from(order)
                .fetchOne();
        return cnt.intValue();
    }

}
