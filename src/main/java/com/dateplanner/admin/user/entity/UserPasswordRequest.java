package com.dateplanner.admin.user.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import com.dateplanner.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "createdAt")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPasswordRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean complete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    private UserPasswordRequest(boolean complete, User user) {
        this.complete = complete;
        this.user = user;
    }

    public static UserPasswordRequest of(boolean complete, User user) {
        return new UserPasswordRequest(complete, user);
    }

    public void complete() {
        this.complete = true;
    }

}
