package com.matrix.BankAccountManagementSystem.model.entity.security;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String authority;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
