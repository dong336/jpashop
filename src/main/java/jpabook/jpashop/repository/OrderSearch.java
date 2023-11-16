package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderSearch {
    
    private String memberName;
    private OrderStatus orderStatus;
}
