package com.kmong.api.product.repository;

import com.kmong.api.product.domain.Product;
import com.kmong.api.product.request.ProductSearch;
import com.querydsl.core.BooleanBuilder;
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
     * 아이디로 상품 조회
     * @param productId 검색할 상품 아이디
     * @return 검색결과
     */
    public Optional<Product> findById(Long productId) {
        return Optional.ofNullable(em.find(Product.class, productId));
    }

    /**
     * 상품 검색(필터 적용)
     * @param productSearch 상품 검색 조건
     * @return
     */
    public List<Product> findAll(ProductSearch productSearch) {
        Long productId = productSearch.getProductId();
        String productName = productSearch.getProductName();
        Boolean salesYn = ObjectUtils.isEmpty(productSearch.getSalesYn()) ? null : Boolean.TRUE.equals(productSearch.getSalesYn());

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!ObjectUtils.isEmpty(productId)) {
            booleanBuilder.and(isEqualsToId(productId));
        }

        if (StringUtils.hasText(productName)) {
            booleanBuilder.and(isLikeProductName(productName));
        }

        if (!ObjectUtils.isEmpty(salesYn)) {
            booleanBuilder.and(isEqualsToSalesYn(salesYn));
        }

        return queryFactory.selectFrom(product)
                .where(booleanBuilder)
                .orderBy(product.id.asc())
                .fetch();
    }

    private BooleanExpression isEqualsToId(Long productId) {
        return ObjectUtils.isEmpty(productId) ? null : product.id.eq(productId);
    }

    private BooleanExpression isLikeProductName(String productName) {
        return StringUtils.hasText(productName) ? product.productName.like("%" + productName + "%") : null;
    }

    private BooleanExpression isEqualsToSalesYn(Boolean salesYn) {
        return ObjectUtils.isEmpty(salesYn) ? null : product.salesYn.eq(salesYn);
    }

    public Product createProduct(Product product) {
        em.persist(product);
        return product;
    }

}
