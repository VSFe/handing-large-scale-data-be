package com.vsfe.largescale.repository;

import java.util.List;

import com.vsfe.largescale.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	// index: user_idx05 (create_date_-1, user_id)
	@Query("""
	SELECT u
	FROM User u
	ORDER BY u.createDate DESC, u.id ASC
	LIMIT :count
	""")
	List<User> findRecentCreatedUsers(@Param("count") int count);
}
