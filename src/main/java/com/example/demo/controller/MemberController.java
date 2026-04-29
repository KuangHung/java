package com.example.demo.controller;

import com.example.demo.entity.MemberEntity;
import com.example.demo.object.MemberDto;
import com.example.demo.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;

@RestController // 代表這是 API 接口，會自動將回傳值轉為 JSON
@RequestMapping("/api/members") // 定義統一的路由前綴 (RESTful 習慣用複數名詞)
public class MemberController {

    private final MemberService memberService;

    // 建構子注入
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // --- 【定義一個簡單的回傳用 DTO】 ---
    // 這能防止我們把 password_hash 洩漏給前端
    public record MemberResponseDTO(Long id, String email, String nickname, Integer status) {
        public static MemberResponseDTO fromEntity(MemberEntity member) {
            return new MemberResponseDTO(
                    member.getId(), member.getEmail(), member.getNickname(), member.getStatus()
            );
        }
    }

    // --- 1. 【增】新增會員 ---
    @PostMapping
    public ResponseEntity<MemberResponseDTO> createMember(@RequestBody MemberDto newMember) {
        // 將 newMember 轉換為 entity
        MemberEntity entity = new MemberEntity();
        BeanUtils.copyProperties(newMember, entity);

        // 依據 entity 建立資料
        MemberEntity savedMember = memberService.createMember(entity);

        // 新增成功，標準應回傳 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberResponseDTO.fromEntity(savedMember));
    }

    // --- 2. 【查】取得所有會員 ---
    @GetMapping
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        List<MemberResponseDTO> dtoList = memberService.getAllMembers()
                .stream()
                .map(MemberResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList); // 預設回傳 200 OK
    }

    // --- 3. 【查】根據 ID 取得單一會員 ---
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                // 如果有找到，轉成 DTO 並回傳 200 OK
                .map(member -> ResponseEntity.ok(MemberResponseDTO.fromEntity(member)))
                // 如果沒找到，回傳 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    // --- 4. 【改】更新會員暱稱 ---
    // 建立一個專門接收更新請求的 DTO
    public record UpdateNicknameRequest(String nickname) {}

    @PutMapping("/{id}/nickname")
    public ResponseEntity<MemberResponseDTO> updateNickname(
            @PathVariable Long id,
            @RequestBody UpdateNicknameRequest request) {

        try {
            MemberEntity updatedMember = memberService.updateMember(id, request.nickname());
            return ResponseEntity.ok(MemberResponseDTO.fromEntity(updatedMember));
        } catch (RuntimeException e) {
            // 對應 Service 拋出的「找不到該會員」異常
            return ResponseEntity.notFound().build();
        }
    }

    // --- 5. 【刪】刪除會員 ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        // 刪除成功且不回傳任何內容時，標準應回傳 204 No Content
        return ResponseEntity.noContent().build();
    }
}