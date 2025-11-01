package com.merchant.user_onboarding.repository.custom;

import com.merchant.user_onboarding.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<UserEntity> findUsersByCriteria(Optional<String> optionalKeyword, Optional<Long> optionalAge, Optional<String> optionalDapartment, Optional<String> optionalFrom,Optional<String> optionalTo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);

        Root<UserEntity> user = cq.from(UserEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        final String LIKE_WILDCARD = "%";
        final char LIKE_ESCAPE_CHAR = '\\';
        final String BACKSLASH = "\\";


        if (optionalKeyword.isPresent()) {

            String keyword = optionalKeyword.get();

            String escapeKeyword = keyword.replace(BACKSLASH, BACKSLASH + BACKSLASH)
                    .replace(LIKE_ESCAPE_CHAR + "", LIKE_ESCAPE_CHAR + "" + LIKE_ESCAPE_CHAR)
                    .replace(LIKE_WILDCARD, LIKE_ESCAPE_CHAR + LIKE_WILDCARD);

            String pattern = LIKE_WILDCARD + escapeKeyword + LIKE_WILDCARD;

            Predicate keywordPredicate = cb.or(
                    cb.like(user.get("userId"), pattern, LIKE_ESCAPE_CHAR),
                    cb.like(user.get("userName"), pattern, LIKE_ESCAPE_CHAR),
                    cb.like(user.get("department"), pattern, LIKE_ESCAPE_CHAR)
            );
            predicates.add(keywordPredicate);
        }

        if (optionalAge.isPresent()) {

            Long age = optionalAge.get();
            Predicate agePredicate = cb.equal(user.get("age"), age);
            predicates.add(agePredicate);
        }

        if (optionalDapartment.isPresent()) {

            String department = optionalDapartment.get();
            Predicate departmentPredicate = cb.equal(user.get("department"), department);
            predicates.add(departmentPredicate);
        }

        if (optionalFrom.isPresent()) {
            if (optionalTo.isPresent()) {
                LocalDate from = LocalDate.parse(optionalFrom.get());
                LocalDate to = LocalDate.parse(optionalTo.get());
//		    	Predicate datePredicate = cb.between(user.get("registeredDate"), from, to);
//	    		predicates.add(datePredicate);

                if (from.equals(to)) {
                    Predicate greaterThanorEqual = cb.greaterThanOrEqualTo(user.get("registeredDate"), from);
                    Predicate lessThan = cb.lessThan(user.get("registeredDate"), to.plusDays(1));
                    predicates.add(greaterThanorEqual);
                    predicates.add(lessThan);

//	    			Predicate date = cb.equal(user.get("registeredDate"), from);
//					predicates.add(date);
                } else {
                    Predicate greaterThanorEqual = cb.greaterThanOrEqualTo(user.get("registeredDate"), from);
                    Predicate lessThanorEqual = cb.lessThanOrEqualTo(user.get("registeredDate"), to.plusDays(1));
                    predicates.add(greaterThanorEqual);
                    predicates.add(lessThanorEqual);
                }

            }
        }


        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        TypedQuery<UserEntity> query = entityManager.createQuery(cq);
        return query.getResultList();

    }
}