package dev.be.modulecore.domain.security;

import dev.be.modulecore.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "refresh_token")
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_key", nullable = false) // key로 할 경우 MariaDB 고유명 때문에 테이블 생성이 안됨
    private String key;

    @Column(name = "ref_token", nullable = false) // token으로 할 경우 MariaDB 고유명 때문에 테이블 생성이 안됨
    private String token;

    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }

    @Builder
    public RefreshToken(String key, String token) {
        this.key = key;
        this.token = token;
    }
}
