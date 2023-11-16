package jpabook.jpashop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService; 

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    private Book createBook() {
        Book book = Book.builder()
            .name("시골 jpa")
            .price(10000)
            .stockQuantity(10)
            .build();
        em.persist(book);
        
        return book;
    }

    private Member createMember() {
        Member member = Member.builder()
            .name("회원1")
            .address(new Address("서울", "강가", "123-123"))
            .build();
        em.persist(member);

        return member;
    }

    @Test
    void testOrder() {
        // given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    void testCancelOrder() {
        // given
        Member member = createMember();
        Book item = createBook();

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);
        
        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL이다");
        assertEquals(10, item.getStockQuantity(), "주문 취소된 상품은 그만큼 재고가 원복되야 한다.");
    }

    @Test
    void testOverstock() {
        assertThrows(NotEnoughStockException.class, () -> {
            //given
            Member member = createMember();
            Item item = createBook();

            int orderCount = 11;

            //when
            orderService.order(member.getId(), item.getId(), orderCount);

            //then
            System.out.println("재고수량 예외");
        });
    }
}