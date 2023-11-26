package jpabook.jpashop.api;

import jpabook.jpashop.api.dto.SimpleOrderDto;
import jpabook.jpashop.api.dto.SimpleOrderQueryDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.SimpleOrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final SimpleOrderQueryRepository simpleOrderQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        // N + 1 문제
        // 첫번째 쿼리가 나가면 (N개) 연관관계에 대해 첫번째 쿼리의 결과만큼 추가 실행된다
        return orders.stream()
                .map(SimpleOrderDto::new)
                .toList();
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(SimpleOrderDto::new)
                .toList();
    }

    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4() {
        return simpleOrderQueryRepository.findOrderDtos();
    }
}
