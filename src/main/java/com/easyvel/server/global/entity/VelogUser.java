package com.easyvel.server.global.entity;

import com.easyvel.server.global.entity.bridge.UserVelogUser;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class VelogUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "velogUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVelogUser> userVelogUsers = new ArrayList<>();

}
