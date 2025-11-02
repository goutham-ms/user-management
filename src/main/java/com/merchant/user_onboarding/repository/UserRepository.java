package com.merchant.user_onboarding.repository;

import com.merchant.user_onboarding.model.UserEntity;
import com.merchant.user_onboarding.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>, UserRepositoryCustom {

    boolean existsByUserId(String id);

    UserEntity findByUserId(String id);
}
