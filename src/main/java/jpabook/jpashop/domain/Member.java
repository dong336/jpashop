package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member", indexes = {
        @Index(name = "IDX_MEMBER1", columnList = "name"),
        @Index(name = "IDX_MEMBER2", columnList = "city")
})
@Getter @Setter
@NoArgsConstructor
public class Member {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @Builder
    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
