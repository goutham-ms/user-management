package com.merchant.user_onboarding.repository.custom;

import com.merchant.user_onboarding.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<UserEntity> findUsersByCriteria(Optional<String> optionalKeyword, Optional<Long> optionalAge, Optional<String> optionalDapartment, Optional<String> optionalFrom, Optional<String> optionalTo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);

        Root<UserEntity> user = cq.from(UserEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        final String LIKE_WILDCARD = "%";
        final char LIKE_ESCAPE_CHAR = '\\';
        final String BACKSLASH = "\\";

        String keyword = optionalKeyword.get();
        Long age = optionalAge.get();

		if(keyword != null && !keyword.isEmpty()) {

			String escapeKeyword = keyword.replace(LIKE_ESCAPE_CHAR + "", LIKE_ESCAPE_CHAR + "" + LIKE_ESCAPE_CHAR)
					.replace(LIKE_WILDCARD, LIKE_ESCAPE_CHAR + LIKE_WILDCARD);

			String pattern = LIKE_WILDCARD + escapeKeyword + LIKE_WILDCARD;

			Predicate keywordPredicate = cb.or(
					cb.like(user.get("userId"), pattern , LIKE_ESCAPE_CHAR),
					cb.like(user.get("userName"), pattern, LIKE_ESCAPE_CHAR),
					cb.like(user.get("department"), pattern, LIKE_ESCAPE_CHAR)
			);

			if(age != null) {

				Predicate agePredicate = cb.equal(user.get("age"), age);
				Predicate finalPredicate = cb.and(keywordPredicate, agePredicate);
				predicates.add(finalPredicate);
				cq.where(predicates.toArray(new Predicate[0]));
				TypedQuery<UserEntity> query = entityManager.createQuery(cq);

				return query.getResultList();

			} else {

				predicates.add(keywordPredicate);
				cq.where(predicates.toArray(new Predicate[0]));
				TypedQuery<UserEntity> query = entityManager.createQuery(cq);

				return query.getResultList();
			}


		}

		return Collections.emptyList();
    }

}