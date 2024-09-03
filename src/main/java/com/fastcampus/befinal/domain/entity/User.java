package com.fastcampus.befinal.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "user_id", columnDefinition = "varchar(15)")
    private String userId;

    @Column(nullable = false, name = "name", columnDefinition = "varchar(10)")
    private String name;

    @Column(nullable = false, name = "password", columnDefinition = "varchar(50)")
    private String password;

    @Column(nullable = false, unique = true, name = "phone_number", columnDefinition = "varchar(11)")
    private String phoneNumber;

    @Column(nullable = false, unique = true, name = "emp_number", columnDefinition = "varchar(50)")
    private String empNumber;

    @Column(nullable = false, unique = true, name = "email", columnDefinition = "varchar(30)")
    private String email;

    @Column(nullable = false, name = "signup_datetime", columnDefinition = "datetime")
    private LocalDateTime signUpDateTime;

    @Column(nullable = false, name = "final_login_datetime", columnDefinition = "datetime")
    private LocalDateTime finalLoginDateTime;

    @Column(nullable = false, name = "role", columnDefinition = "varchar(20)")
    private String role;

    public void updateFinalLoginDateTime() {
        this.finalLoginDateTime = LocalDateTime.now();
    }
}
