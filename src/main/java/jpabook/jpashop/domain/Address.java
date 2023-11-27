package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Embeddable
@Getter
@Builder
@AllArgsConstructor
public class Address {
    
    private String city;

    private String street;

    private String zipcode;

    protected Address() {}

}
