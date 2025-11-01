package com.merchant.user_onboarding.repository;

import com.merchant.user_onboarding.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUserId(String id);

    UserEntity findByUserId(String id);

    	@Query("SELECT u FROM MyUserEntity u WHERE " +
			"u.userId LIKE :keyword " +
			"OR u.userName LIKE :keyword " +
			"OR u.department LIKE :keyword ")
        Optional<List<UserEntity>> getUsers(@Param("keyword") String keyword);
}
