package jpabook.jpashop.api;

import jpabook.jpashop.api.dto.OrderDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        all.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(o -> o.getItem().getName());
        });
        // json 직렬화 문제, 엔티티와 api에 의존성을 만들지 말고, dto를 만들자
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return orders.stream()
                .map(OrderDto::new)
                .toList();
    }

    /*
     * 페치조인 한방쿼리는 쿼리는 한 번이지만 데이터 전송량이 많아진다
     * 1:N 엔티티일 때, 비정규화된 데이터를 조회하기 때문
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        return orders.stream()
                .map(OrderDto::new)
                .toList();
    }

    /*
     * 페치 사이즈를 설정하면 여러 쿼리를 실행하게 된다
     * 쿼리가 더 나가긴 하지만 중복이 없는 정규화된 데이터를 조회하며, 페이징도 가능하다
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        return orders.stream()
                .map(OrderDto::new)
                .toList();
    }
}
