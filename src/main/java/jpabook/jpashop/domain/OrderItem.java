package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.*;


@Entity
@Table
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격
    private int count; // 주문수량

    // 비즈니스 로직

    // 생성
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = OrderItem.builder()
            .item(item)
            .orderPrice(orderPrice)
            .count(count)
            .build();
        
        item.removeStock(count);

        return orderItem;
    }

    // 상품취소
    public void cancel() {
        getItem().addStock(count);
    }

    // 총가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
