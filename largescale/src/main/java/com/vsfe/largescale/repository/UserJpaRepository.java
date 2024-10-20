package com.vsfe.largescale.repository;

import com.vsfe.largescale.domain.User;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    // index: user_idx04
    User findByUsername(String username);

    // index: user_idx05 (create_date_-1, user_id)
    @Query("""
    SELECT u
    FROM User u
    ORDER BY u.createDate DESC, u.id ASC
    LIMIT :count
    """)
    List<User> findRecentCreatedUsers(@Positive @Param("count") int count);

    // index: X
    @Query("""
    SELECT u
    FROM User u
    ORDER BY u.updateDate DESC, u.id ASC
    LIMIT :count
    """)
    List<User> findRecentUpdatedUsers(@Positive @Param("count") int count);

    // index: pk
    @Query("""
    SELECT u
    FROM User u
    WHERE u.id > :lastUserId
    ORDER BY u.id
    LIMIT :count
    """)
    List<User> findUsersWithLastUserId(
        @PositiveOrZero @Param("lastUserId") int lastUserId,
        @Positive @Param("count") int count
    );
}
