package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("A")
@Getter
@SuperBuilder
public class Album extends Item{
    
    private String artist;
    private String etc;
}
