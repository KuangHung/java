package com.example.demo.object;

import java.math.BigDecimal;

// 這個 DTO 用於接收 JPQL 查詢的結果
public class MemberSpendingDto {

    private final Long memberId;
    private final String email;
    private final String nickname;
    private final Long totalSpending;

    // 這個建構子的參數順序和類型，必須與 Repository 中的 JPQL 查詢完全對應
    public MemberSpendingDto(Long memberId, String email, String nickname, Long totalSpending) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        // 如果 SUM() 的結果是 null (例如某會員沒有任何訂單)，給他一個 0
        this.totalSpending = (totalSpending == null) ? 0 : totalSpending;
    }

    // --- Getters ---
    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getTotalSpending() {
        return totalSpending;
    }
}