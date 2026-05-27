package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // 明確對應資料庫中真實的表名
public class OrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 對應 SQL 的 AUTO_INCREMENT
    private Long id;

    // 訂單編號，對應 order_number (JPA 預設會將駝峰式命名轉為底線)
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    // 訂單總金額
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    // 訂單狀態：1=處理中, 2=已完成, 0=已取消
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 給予預設值

    // 🌟 核心重點：多對一關聯 (多筆訂單對應一個會員)
    // 預設為 EAGER，強烈建議手動改為 LAZY 避免 N+1 陷阱
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false) // id 這是要對應到 members 表的欄位
    private MemberEntity member;

    // 自動管理建立時間 (完全不需寫原生 SQL 預設值)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 自動管理更新時間
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- 以下為 Getter 與 Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}