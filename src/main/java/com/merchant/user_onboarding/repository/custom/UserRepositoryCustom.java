package com.merchant.user_onboarding.repository.custom;

import com.merchant.user_onboarding.model.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepositoryCustom {
    List<UserEntity> findUsersByCriteria(Optional<String> keyword, Optional<Long> age, Optional<String> department, Optional<String> from, Optional<String> to);
}
