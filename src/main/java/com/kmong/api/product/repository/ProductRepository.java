package com.kmong.api.product.repository;

import com.kmong.api.product.domain.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.kmong.api.product.domain.QProduct.product;

@Repository
@Transactional
public class ProductRepository {

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @Autowired
    public ProductRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 상품 등록
     * @param product 등록할 상품정보
     * @return 등록한 상품정보
     */
    public Optional<Product> save(Product product) {
        em.persist(product);
        return Optional.ofNullable(product);
    }

    /**
     * 상품 복수 등록
     * @param products 등록할 상품정보 리스트
     * @return 등록한 상품정보 리스트
     */
    public List<Product> saveAll(List<Product> products) {
        if (!CollectionUtils.isEmpty(products)) {
            for (Product product : products) {
                em.persist(product);
            }
        }
        return products;
    }

    /**
     * 상품 전체 삭제
     */
    public void deleteAll() {
        queryFactory.delete(product).execute();
    }

    /**
     * 아이디로 상품 검색
     * @param id 검색할 상품아이디
     * @return 검색한 상품정보
     */
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    /**
     * 상품명으로 상품 검색
     * @param productName 검색어
     * @return 검색한 상품정보 리스트
     */
    public List<Product> findByProductName(String productName) {
        return queryFactory.selectFrom(product)
                .where(likeProductName(productName))
                .orderBy(product.id.asc())
                .fetch();
    }

    private BooleanExpression likeProductName(String productName) {
        return StringUtils.hasText(productName) ? product.productName.like("%" + productName + "%") : null;
    }

    /**
     * 판매여부로 상품 검색
     * @param salesYn 판매여부
     * @return 검색한 상품정보 리스트
     */
    public List<Product> findBySalesYn(Boolean salesYn) {
        return queryFactory.selectFrom(product)
                .where(isEqualsToSalesYn(salesYn))
                .orderBy(product.id.asc())
                .fetch();
    }

    private BooleanExpression isEqualsToSalesYn(Boolean salesYn) {
        return ObjectUtils.isEmpty(salesYn) ? null : product.salesYn.eq(salesYn);
    }
}
