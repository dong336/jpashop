package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("B")
@SuperBuilder
@Getter @Setter
public class Book extends Item{
    
    private String author;
    private String isbn;
}
