package com.example.demo.service;

import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.OrdersEntity;
import com.example.demo.object.MemberInfoDto;
import com.example.demo.object.MemberSpendingDto;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    // 1. 【增】新增一筆資料
    public MemberEntity createMember(MemberEntity member) {
        // save 方法會判斷 ID，若 ID 為空則執行 INSERT
        return memberRepository.save(member);
    }

    // 2. 【查】根據 ID 查詢一筆資料
    public Optional<MemberEntity> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // 3. 【查】查詢所有資料
    public List<MemberEntity> getAllMembers() {
        return memberRepository.findAll();
    }

    // 4. 【改】更新資料
    @Transactional
    public MemberEntity updateMember(Long id, String newNickname) {
        // 先查出該筆資料，若不存在則拋出異常（這就是嚴謹的表現）
        MemberEntity existingMember = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到該會員"));

        // 修改欄位
        existingMember.setNickname(newNickname);

        // 在 @Transactional 下，其實不需要呼叫 save()，JPA 會自動執行 Dirty Checking 並更新
        // 但為了讓新手好理解，手動呼叫 save() 也是可以的
        return memberRepository.save(existingMember);
    }

    // 5. 【刪】刪除一筆資料
    public void deleteMember(Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
        }
    }

    // 【查】關鍵字模糊搜尋會員
    public List<MemberEntity> searchMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return memberRepository.searchMembers(keyword);
    }

    // 【查】找出今天註冊的活躍會員
    public List<MemberEntity> getTodayActiveMembers() {
        return memberRepository.findTodayActiveMembers();
    }

    // 【查】查詢指定狀態的帳號
    public List<MemberInfoDto> findMemberInfosByStatus(Integer status) {
        return memberRepository.findMemberInfosByStatus(status);
    }

    // 【查】查詢所有會員花費的金額
    public List<MemberSpendingDto> findBadAllMembersTotalSpending() {
        System.out.println("--- 開始執行 N+1 查詢範例 ---");

        // 1. 第一次查詢：取得所有會員列表 (這就是 "1" 查詢)
        List<MemberEntity> allMembers = memberRepository.findAll();
        System.out.println("已取得 " + allMembers.size() + " 位會員");

        List<MemberSpendingDto> results = new ArrayList<>();

        // 2. 遍歷所有會員，為每位會員單獨計算總額
        for (MemberEntity member : allMembers) {
            // 3. 【危險！】為每一位會員，都發起一次資料庫查詢來取得他的訂單 (這就是 "+N" 查詢)
            List<OrdersEntity> ordersForMember = ordersRepository.findByMember(member);

            // 4. 在 Java 記憶體中計算訂單總額
            long totalAmount = ordersForMember.stream()
                    .mapToLong(OrdersEntity::getTotalAmount)
                    .sum();

            // 5. 建立 DTO 並加入結果列表
            results.add(new MemberSpendingDto(member.getId(), member.getNickname(), member.getNickname(), totalAmount));
        }

        System.out.println("--- N+1 查詢結束 ---");
        return results;
    }

    // 【查】查詢所有會員花費的金額
    public List<MemberSpendingDto> findAllMembersTotalSpending() {
        return memberRepository.findAllMembersTotalSpending();
    }
}