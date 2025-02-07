package com.khalil.management.group.entity;

import com.khalil.management.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "academic_group")
public class AcademicGroup {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String class_group;
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<User> users;
}
