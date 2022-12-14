package com.kmong.api.order.service.impl;

import com.kmong.api.common.response.ListResponse;
import com.kmong.api.common.response.SingleResponse;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.exception.MemberNotFoundException;
import com.kmong.api.member.service.MemberService;
import com.kmong.api.order.domain.Order;
import com.kmong.api.order.domain.OrderProduct;
import com.kmong.api.order.repository.OrderRepository;
import com.kmong.api.order.request.OrderCreate;
import com.kmong.api.order.request.OrderSearch;
import com.kmong.api.order.request.OrderedProduct;
import com.kmong.api.order.response.ImpossibleProductView;
import com.kmong.api.order.response.OrderView;
import com.kmong.api.order.service.OrderService;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.exception.ImpossibleOrderException;
import com.kmong.api.product.exception.ProductsNotFoundException;
import com.kmong.api.product.repository.ProductRepository;
import com.kmong.api.product.service.ProductService;
import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final MemberService memberService;

    private final ProductService productService;

    /**
     * 주문 생성
     * @param orderCreate 생성할 주문정보
     * @return 주문 생성 결과
     */
    @Override
    public ResponseEntity<OrderView> createOrder(OrderCreate orderCreate) {
        ResponseEntity response = new ResponseEntity(HttpStatus.CREATED);
        Optional<Member> memberFindById = memberService.findById(orderCreate.getMemberId());

        // orderCreate로 멤버 정보 가져오고 -> 없으면 MemberNotFoundException
        if (memberFindById.isEmpty()) {
            throw new MemberNotFoundException();
        }

        // id들로 상품정보 가져오기
        List<Product> products = productService.findByIds(orderCreate.getOrderedProductIds());

        checkPossibleOrderOrNot(orderCreate, products);

        List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
        // 상품정보 -> 주문상품으로 변경
        for (Product product : products) {
            // 생성 요청된 주문정보에서 같은 상품 아이디의 주문수량 찾기
            Optional<Integer> orderQuantity = orderCreate.getOrderedProducts().stream().filter(p -> product.getId().equals(p.getProductId())).map(p -> p.getQuantity()).findFirst();
            if (orderQuantity.isPresent()) {
                orderProducts.add(OrderProduct.builder()
                                                .orderProductName(product.getProductName())
                                                .product(product)
                                                .quantity(orderQuantity.get())
                                                .price(product.getPrice())
                                                .build());
            } else {
                response = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }

        if (HttpStatus.CREATED.equals(response.getStatusCode()) && !orderProducts.isEmpty() && !products.isEmpty()) {
            Order order = orderCreate.toOrder(memberFindById.get(), orderProducts);
            orderRepository.createOrder(order);
            decreaseQuantity(orderCreate, products);
            SingleResponse body = SingleResponse.builder().code("201").message("주문 완료했습니다.").data(order.toOrderView()).build();
            response = new ResponseEntity(body, HttpStatus.CREATED);
        }
        return response;
    }

    /**
     * 가능한 주문인지 확인한다.
     * @param orderCreate 주문정보
     * @param products DB 상품정보
     * @return 가능한 주문인지 여부
     */
    private void checkPossibleOrderOrNot(OrderCreate orderCreate, List<Product> products) {
        List<OrderedProduct> notExists = getNotExistsProductsInOrderedProducts(orderCreate, products);

        if (!CollectionUtils.isEmpty(notExists)) {
            throw new ProductsNotFoundException(notExists);
        }

        List<ImpossibleProductView> duplicateProducts = getDuplicateProductsInOrderedProducts(orderCreate, products);

        if (!CollectionUtils.isEmpty(duplicateProducts)) {
            throw new ImpossibleOrderException(duplicateProducts);
        }

        List<ImpossibleProductView> wrongQuantityProducts = getImpossibleOrders(orderCreate, products);

        if (!CollectionUtils.isEmpty(wrongQuantityProducts)) {
            throw new ImpossibleOrderException(wrongQuantityProducts);
        }

        List<ImpossibleProductView> impossibleProducts = getImpossibleProducts(orderCreate, products);

        if (!CollectionUtils.isEmpty(impossibleProducts)) {
            throw new ImpossibleOrderException(impossibleProducts);
        }
    }

    /**
     * 상품 수량에서 주문한 수량만큼 감소시킨다.
     * @param orderCreate 주문정보
     * @param products DB 상품 리스트
     */
    public void decreaseQuantity(OrderCreate orderCreate, List<Product> products) {
        for (OrderedProduct orderedProduct : orderCreate.getOrderedProducts()) {
            for (Product product : products) {
                if (product.getId().equals(orderedProduct.getProductId())) {
                    int newQuantity = product.getQuantity() - orderedProduct.getQuantity();
                    productRepository.decreaseQuantity(product.getId(), newQuantity);
                    break;
                }
            }
        }
    }

    /**
     * 같은 상품아이디가 중복되어 주문 불가능인 상품정보 리스트를 반환한다.
     * @param orderCreate 주문정보
     * @param products DB 상품
     * @return 주문 불가능인 상품정보 리스트
     */
    private List<ImpossibleProductView> getDuplicateProductsInOrderedProducts(OrderCreate orderCreate, List<Product> products) {
        List<ImpossibleProductView> duplicateProducts = new ArrayList<ImpossibleProductView>();

        List<OrderedProduct> orderedProducts = orderCreate.getOrderedProducts();
        for (OrderedProduct standard : orderedProducts) {
            boolean isDuplicate = false;
            Product product = products.stream().filter(p -> p.getId().equals(standard.getProductId())).findFirst().get();
            ImpossibleProductView impossibleProductView = product.toImpossibleProductView();
            impossibleProductView.setQuantity(standard.getQuantity());
            for (OrderedProduct compare : orderedProducts) {
                if (orderedProducts.indexOf(compare) != orderedProducts.indexOf(standard)
                        && compare.getProductId().equals(standard.getProductId())) {
                    impossibleProductView.setReason("한 상품을 중복하여 주문할 수 없습니다.");
                    isDuplicate = true;
                }
            }

            if (isDuplicate) {
                duplicateProducts.add(impossibleProductView);
            }
        }
        return duplicateProducts;
    }

    /**
     * 판매중이 아니거나 재고가 없어서 주문 불가능인 상품정보 리스트를 반환한다.
     * @param orderCreate 주문정보
     * @param products DB 상품
     * @return 주문 불가능인 상품정보 리스트
     */
    private List<ImpossibleProductView> getImpossibleProducts(OrderCreate orderCreate, List<Product> products) {
        List<ImpossibleProductView> impossibleProducts = new ArrayList<ImpossibleProductView>();

        for (Product product : products) {
            boolean isPossible = true;
            ImpossibleProductView impossibleProductView = product.toImpossibleProductView();
            for (OrderedProduct orderedProduct : orderCreate.getOrderedProducts()) {
                impossibleProductView.setQuantity(orderedProduct.getQuantity());
                if (product.getId().equals(orderedProduct.getProductId())
                        && !product.isSalesYn()) {
                    impossibleProductView.setReason("판매중이 아닌 상품입니다.");
                    isPossible = false;
                }

                if (product.getId().equals(orderedProduct.getProductId())
                        && product.getQuantity() < orderedProduct.getQuantity()) {
                    if (StringUtils.isNullOrEmpty(impossibleProductView.getReason())) {
                        impossibleProductView.setReason("재고가 부족합니다.");
                    }
                    isPossible = false;
                }
            }

            if (!isPossible) {
                impossibleProducts.add(impossibleProductView);
            }
        }
        return impossibleProducts;
    }

    /**
     * 수량이 유효하지 않아 주문불가능인 주문상품정보 리스트를 반환한다.
     * @param orderCreate 주문정보
     * @param products DB 상품
     * @return 주문 불가능인 상품정보 리스트
     */
    private List<ImpossibleProductView> getImpossibleOrders(OrderCreate orderCreate, List<Product> products) {
        List<ImpossibleProductView> impossibleProducts = new ArrayList<ImpossibleProductView>();

        for (Product product : products) {
            boolean isPossible = true;
            ImpossibleProductView impossibleProductView = product.toImpossibleProductView();
            for (OrderedProduct orderedProduct : orderCreate.getOrderedProducts()) {
                impossibleProductView.setQuantity(orderedProduct.getQuantity());
                if (product.getId().equals(orderedProduct.getProductId())
                        && 0 > orderedProduct.getQuantity()) {
                    if (StringUtils.isNullOrEmpty(impossibleProductView.getReason())) {
                        impossibleProductView.setReason("수량은 양수만 입력 가능합니다.");
                    }
                    isPossible = false;
                }
            }

            if (!isPossible) {
                impossibleProducts.add(impossibleProductView);
            }
        }
        return impossibleProducts;
    }

    /**
     * 주문상품 중 존재하지 않는 상품 리스트를 반환한다.
     * @param orderCreate 주문정보
     * @param products DB 상품
     * @return 존재하지 않는 상품리스트
     */
    private static List<OrderedProduct> getNotExistsProductsInOrderedProducts(OrderCreate orderCreate, List<Product> products) {
        List<OrderedProduct> notExists = new ArrayList<OrderedProduct>();

        for (OrderedProduct orderedProduct : orderCreate.getOrderedProducts()) {
            boolean isExists = false;
            for (Product product : products) {
                if (product.getId().equals(orderedProduct.getProductId())) {
                    isExists = true;
                    break;
                }
            }

            if (!isExists) {
                notExists.add(orderedProduct);
            }
        }
        return notExists;
    }

    /**
     * 주문내역 전체 조회
     * @param orderSearch 주문내역 검색 조건
     * @return 주문내역 리스트
     */
    @Override
    public ResponseEntity<ListResponse<OrderView>> findAllOrder(OrderSearch orderSearch) {
        ListResponse response = ListResponse.builder()
                                            .code("200")
                                            .message("주문내역 조회되었습니다.")
                                            .build();

        List<OrderView> orderViews = new ArrayList<OrderView>();
        if (!ObjectUtils.isEmpty(orderSearch)) {
            List<Order> orders = orderRepository.findAllOrder(orderSearch);
            for (Order order : orders) {
                orderViews.add(order.toOrderView());
            }
            response.setDatas(orderViews);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
