package jpabook.jpashop.controller;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberForm {
    
    @NotEmpty(message = "회원 이름은 필수입니다")
    private String name;
    
    private String city;
    private String street;
    private String zipcode;
}
