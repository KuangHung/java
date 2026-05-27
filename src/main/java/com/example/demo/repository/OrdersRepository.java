package com.example.demo.repository;

import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {

    /**
     * 根據會員實體查詢其所有訂單。
     * Spring Data JPA 會自動產生 "SELECT * FROM orders WHERE member_id = ?" 的查詢。
     * 這就是等一下會造成 N+1 問題的 "+N" 查詢來源。
     */
    List<OrdersEntity> findByMember(MemberEntity member);
}