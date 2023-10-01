package com.easyvel.server.global.entity;

import com.easyvel.server.global.entity.bridge.UserVelogUser;
import com.easyvel.server.notification.fcm.FcmToken;
import com.easyvel.server.tag.bridge.UserTag;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Todo: 애플이나 구글의 사용자 정보 넣기
    @Column(nullable = false, unique = true)
    private String uid;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    //영속성 전이. 삭제되면 아래 얘도 삭제 되도록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTag> userTagList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVelogUser> userVelogUsers = new ArrayList<>();

    @Nullable
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private FcmToken fcmToken;

    //Todo: roles 시간나면 개선 이거 이렇게 말고 다른거로 바꾸고싶은데...
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.uid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
