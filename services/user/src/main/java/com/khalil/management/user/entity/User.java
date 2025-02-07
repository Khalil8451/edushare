package com.khalil.management.user.entity;

import com.khalil.management.group.entity.AcademicGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "user_account")
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private AcademicGroup group;
}
