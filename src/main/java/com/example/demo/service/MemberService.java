package com.example.demo.service;

import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.OrdersEntity;
import com.example.demo.exceptions;
import com.example.demo.object.MemberDto;
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

    @Transactional // 關鍵：開啟交易，確保 EntityManager 持續監控
    public void updateMemberName(MemberDto updateMember) {
        // 1. 撈出資料，此時 member 進入【Managed (託管)】狀態
        MemberEntity member = memberRepository.findById(updateMember.getId()).get();

        // 2. 修改屬性。EntityManager 會默默記下這個改變
        member.setNickname(updateMember.getNickname());

        // 3. 方法結束，Transaction 準備 Commit。
        // JPA 發現物件跟剛撈出來時不一樣 (Dirty)，自動發出 UPDATE SQL。
        // 完全不需要呼叫 memberRepository.save(member)！
    }

    public void updateFailMemberName(MemberDto updateMember) {
        // 1. 因為沒有 @Transactional，撈出資料後 session 立刻關閉
        MemberEntity member = memberRepository.findById(updateMember.getId()).get();

        // 2. 這裡不管怎麼 set，資料庫都不會更新，因為 EntityManager 已經不管它了！
        member.setNickname(updateMember.getNickname());
    }

    public void placeOrderWithoutTransaction(Long memberId, int amount) {
        // 步驟 1: 查詢會員
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("找不到會員: " + memberId));

        // 步驟 2: 建立並儲存訂單 (第一個資料庫寫入)
        OrdersEntity order = new OrdersEntity();
        order.setMember(member);
        order.setTotalAmount(amount);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        ordersRepository.save(order);
        System.out.println("訂單已成功儲存到資料庫！訂單 ID: " + order.getId());

        // 步驟 3: 故意在此處拋出一個執行階段錯誤，模擬程式發生意外
        if (true) {
            System.out.println("糟糕！程式發生了無法預期的錯誤！");
            throw new RuntimeException("模擬一個意料之外的系統錯誤");
        }

        // 步驟 4: 更新會員狀態為 VIP (這段程式碼永遠不會被執行到)
        member.setStatus(2); // 2 = VIP
        memberRepository.save(member);
        System.out.println("會員狀態已更新");
    }

    @Transactional
    public void placeOrderWithDefaultTransactional(Long memberId, int amount) throws exceptions.InvalidOrderAmountException {
        System.out.println("--- 案例二：執行有 @Transactional 但不回滾的操作 ---");

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("找不到會員: " + memberId));

        OrdersEntity order = new OrdersEntity();
        order.setMember(member);
        order.setTotalAmount(amount);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        ordersRepository.save(order);
        System.out.println("訂單已暫存 (尚未提交)");

        // 模擬一個業務邏輯上的錯誤，例如訂單金額無效
        if (amount < 0) {
            System.out.println("業務邏輯錯誤：訂單金額無效，拋出受檢例外！");
            // InvalidOrderAmountException 是一個繼承自 Exception 的受檢例外
            throw new exceptions.InvalidOrderAmountException("訂單金額不可為負數");
        }

        // 假設金額正確，繼續執行
        member.setStatus(2);
        memberRepository.save(member);
        System.out.println("會員狀態已更新 (尚未提交)");
    }

    @Transactional(rollbackFor = Exception.class) // 關鍵！告訴 Spring 任何 Exception 都要回滾
    public void placeOrderTheRightWay(Long memberId, int amount) throws exceptions.InvalidOrderAmountException {
        System.out.println("--- 案例三：執行有明確回滾規則的操作 ---");

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("找不到會員: " + memberId));

        OrdersEntity order = new OrdersEntity();
        order.setMember(member);
        order.setTotalAmount(amount);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        ordersRepository.save(order);
        System.out.println("✅ 訂單已暫存 (尚未提交)");

        if (amount < 0) {
            System.out.println("🛑 業務邏輯錯誤：訂單金額無效，拋出受檢例外！");
            throw new exceptions.InvalidOrderAmountException("訂單金額不可為負數");
        }

        member.setStatus(2);
        memberRepository.save(member);
        System.out.println("會員狀態已更新 (尚未提交)");
    }
}