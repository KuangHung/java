package com.example.demo.service;

import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

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
}