package com.example.demo.repository;

import com.example.demo.entity.MemberEntity;
import com.example.demo.object.MemberInfoDto;
import com.example.demo.object.MemberSpendingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 根據暱稱或 Email 進行模糊搜尋 % 符號可以直接寫在 @Query 裡面，乾淨又安全
    @Query("SELECT m FROM MemberEntity m WHERE m.nickname LIKE %:keyword% OR m.email LIKE %:keyword%")
    List<MemberEntity> searchMembers(@Param("keyword") String keyword);

    // 找出今天註冊的活躍會員 ( status = 1 為活躍) CURRENT_DATE 是 JPQL 提供的標準函數
    @Query("SELECT m FROM MemberEntity m WHERE m.status = 1 AND m.createdAt >= CURRENT_DATE")
    List<MemberEntity> findTodayActiveMembers();

    // 查詢指定狀態的帳號 : 只撈取需要的欄位，直接封裝成 DTO
    @Query("SELECT new com.example.demo.object.MemberInfoDto(m.email, m.nickname) FROM MemberEntity m WHERE m.status = :status")
    List<MemberInfoDto> findMemberInfosByStatus(@Param("status") Integer status);

    // 從 Member 出發，串接 Order，並進行分群加總
    @Query("SELECT new com.example.demo.object.MemberSpendingDto(" +
            "   m.id, " +
            "   m.email, " +
            "   m.nickname, " +
            "   SUM(o.totalAmount)" +
            ") " +
            "FROM MemberEntity m " +
            "LEFT JOIN OrdersEntity o ON o.member = m " + // 使用 LEFT JOIN 確保沒有訂單的會員也會被查詢出來
            "GROUP BY m.id, m.email, m.nickname " +      // 彙總查詢必須使用 GROUP BY
            "ORDER BY SUM(o.totalAmount) DESC NULLS LAST") // 依照消費總額排序，NULLS LAST 讓沒有消費的會員排在最後
    List<MemberSpendingDto> findAllMembersTotalSpending();
}