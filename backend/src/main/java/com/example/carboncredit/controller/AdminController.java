package com.example.carboncredit.controller;

import com.example.carboncredit.dto.ResolveTransactionRequest;
import com.example.carboncredit.dto.UserDto;
import com.example.carboncredit.entity.User;
import com.example.carboncredit.entity.Transaction;
import com.example.carboncredit.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/users")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> dtos = adminService.listAllUsers().stream()
                .map((User u) -> UserDto.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .role(u.getRole())
                        .enabled(u.getEnabled())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/users/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> patchUser(@PathVariable UUID id, @RequestBody UserDto in) {
        User updated = adminService.updateUser(id, in.getName(), in.getEnabled(), in.getRole());
        UserDto dto = UserDto.builder()
                .id(updated.getId())
                .name(updated.getName())
                .email(updated.getEmail())
                .role(updated.getRole())
                .enabled(updated.getEnabled())
                .build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/transactions")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Transaction>> listTransactions(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(adminService.listTransactions(status));
    }

    @PostMapping("/transactions/{id}/resolve")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Transaction> resolveTransaction(
            @PathVariable UUID id,
            @RequestBody ResolveTransactionRequest req
    ) {
        Transaction tx = adminService.resolveTransaction(id, req.getAction(), req.getComment());
        return ResponseEntity.ok(tx);
    }

    @GetMapping("/reports/summary")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminService.ReportSummary> getSummary() {
        return ResponseEntity.ok(adminService.getReportSummary());
    }
}
