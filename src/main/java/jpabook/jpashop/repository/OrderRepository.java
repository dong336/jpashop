package jpabook.jpashop.repository;

import static jpabook.jpashop.domain.QMember.member;
import static jpabook.jpashop.domain.QOrder.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    
    private final EntityManager em;
    private final JPAQueryFactory qf;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return qf
            .select(order)
            .from(order)
            .join(order.member, member)
            .on(order.id.eq(member.id))
            .where(statusEq(orderSearch.getOrderStatus()),
                nameLike(orderSearch.getMemberName()))
            .limit(1000)
            .fetch();
    }

    private BooleanExpression statusEq(OrderStatus orderStatus) {
        if(orderStatus == null) return null;

        return order.status.eq(orderStatus);
    }

    private BooleanExpression nameLike(String name) {
        if(!StringUtils.hasText(name)) return null;

        return member.name.like(name);
    }

    // public List<Order> findAll(OrderSearch orderSearch) {
    //     return em.createQuery("""
    //             select o
    //             from Order o
    //             join o.member m
    //             where o.status = :status
    //             and m.name like :name
    //         """, Order.class)
    //         .setParameter("status", orderSearch.getOrderStatus())
    //         .setParameter("name", orderSearch.getMemberName())
    //         .setMaxResults(1000)
    //         .getResultList();
    // }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        if(orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);

        return query.getResultList();
    }
}
