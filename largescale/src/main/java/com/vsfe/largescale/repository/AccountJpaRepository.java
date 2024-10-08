package com.vsfe.largescale.repository;

import java.util.List;

import com.vsfe.largescale.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {
	@Query("""
	SELECT a
	FROM Account a
	ORDER BY a.id
	LIMIT :size
	""")
	List<Account> findAccount(@Param("size") int size);

	@Query("""
	SELECT a
	FROM Account a
	WHERE a.id > :lastAccountId
	ORDER BY a.id
	LIMIT :size
	""")
	List<Account> findAccountWithLastAccountId(@Param("lastAccountId") int lastAccountId, @Param("size") int size);
}
