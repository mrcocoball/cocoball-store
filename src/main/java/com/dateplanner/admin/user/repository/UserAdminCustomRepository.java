package com.dateplanner.admin.user.repository;

import com.dateplanner.admin.user.dto.UserResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.dateplanner.user.entity.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class UserAdminCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public UserAdminCustomRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<UserResponseDto> userList(@Param("email") String email,
                                          @Param("nickname") String nickname,
                                          @Param("deleted") boolean deleted,
                                          @Param("social") boolean social,
                                          @Param("provider") String provider,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("targetDate") LocalDate targetDate) {

        return jpaQueryFactory
                .select(Projections.fields(UserResponseDto.class,
                        user.uid.as("uid"),
                        user.email.as("email"),
                        user.nickname.as("nickname"),
                        user.social.as("social"),
                        user.deleted.as("deleted"),
                        user.provider.as("provider"),
                        user.createdAt.as("createdAt"),
                        user.modifiedAt.as("modifiedAt")
                ))
                .from(user)
                .where(
                        searchCondition(deleted, social, provider, startDate, targetDate, email, nickname)
                )
                .fetch();

    }

    public List<UserResponseDto> deletedUserList(@Param("email") String email,
                                                 @Param("nickname") String nickname,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("targetDate") LocalDate targetDate) {

        return jpaQueryFactory
                .select(Projections.fields(UserResponseDto.class,
                        user.uid.as("uid"),
                        user.email.as("email"),
                        user.nickname.as("nickname"),
                        user.social.as("social"),
                        user.deleted.as("deleted"),
                        user.provider.as("provider"),
                        user.createdAt.as("createdAt"),
                        user.modifiedAt.as("modifiedAt")
                ))
                .from(user)
                .where(
                        searchConditionDeletedUser(true, startDate, targetDate, email, nickname)
                )
                .fetch();

    }


    private BooleanExpression emailEq(String email) {
        return hasText(email) ? user.email.eq(email) : null;
    }

    private BooleanExpression nicknameEq(String nickname) {
        return hasText(nickname) ? user.nickname.eq(nickname) : null;
    }

    private BooleanExpression deletedEq(boolean deleted) {
        return user.deleted.eq(deleted);
    }

    private BooleanExpression socialEq(boolean social) {
        return user.social.eq(social);
    }

    private BooleanExpression providerEq(String provider) {
        return hasText(provider) ? user.provider.eq(provider) : null;
    }

    private BooleanExpression betweenStartDateAndTargetDate(LocalDate startDate, LocalDate targetDate) {

        LocalDate defaultTargetDate = LocalDate.now();
        LocalDate defaultStartDate = defaultTargetDate.minusDays(7);

        if (startDate == null) {
            startDate = defaultStartDate;
        }

        if (targetDate == null) {
            targetDate = defaultTargetDate;
        }

        return user.createdAt.between(startDate.atStartOfDay(), targetDate.atStartOfDay());

    }

    private BooleanExpression searchCondition(boolean deleted,
                                              boolean social,
                                              String provider,
                                              LocalDate startDate,
                                              LocalDate targetDate,
                                              String email,
                                              String nickname) {
        return deletedEq(deleted)
                .and(socialEq(social))
                .and(providerEq(provider))
                .and(betweenStartDateAndTargetDate(startDate, targetDate))
                .and(emailEq(email))
                .and(nicknameEq(nickname));
    }

    private BooleanExpression searchConditionDeletedUser(boolean deleted,
                                                         LocalDate startDate,
                                                         LocalDate targetDate,
                                                         String email,
                                                         String nickname) {
        return deletedEq(deleted)
                .and(betweenStartDateAndTargetDate(startDate, targetDate))
                .and(emailEq(email))
                .and(nicknameEq(nickname));
    }

}
