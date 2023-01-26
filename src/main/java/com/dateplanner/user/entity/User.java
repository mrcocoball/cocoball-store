package com.dateplanner.user.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j(topic = "ENTITY")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @Column(length = 50)
    private String uid;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 30)
    private String introduce;

    @Column
    private boolean deleted;

    @Column
    private boolean social;

    @ElementCollection(fetch = FetchType.EAGER) // 즉시 로딩
    @Builder.Default
    private List<String> roleSet = new ArrayList<>();

    private User(String uid, String password, String email, String introduce, boolean deleted, boolean social) {

        this.uid = uid;
        this.password = password;
        this.email = email;
        this.introduce = introduce;
        this.deleted = deleted;
        this.social = social;
    }

    public static User of(String uid, String password, String email, String introduce, boolean deleted, boolean social) {
        return new User(uid, password, email, introduce, deleted, social);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void changeDelete(boolean deleted) {
        this.deleted = deleted;
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return uid != null && uid.equals(((User) o).getUid());
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return uid != null && uid.equals(that.getUid());
    }
     */

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleSet
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 이하 UserDetails 재정의
     */

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.uid;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
