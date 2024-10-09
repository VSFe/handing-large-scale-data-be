package com.vsfe.largescale.repository;

import java.time.Instant;
import java.util.List;

import com.vsfe.largescale.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
	@Query("""
	SELECT t
	FROM Transaction t
	WHERE t.senderAccount = :account
	ORDER BY t.transactionDate DESC, t.id
	LIMIT :limit
	""")
	List<Transaction> findTransactionsBySenderAccount(@Param("account") String account, @Param("limit") int limit);

	@Query("""
	SELECT t
	FROM Transaction t
	WHERE t.senderAccount = :account
	AND ((t.transactionDate = :transactionDate AND t.id > :id) OR (t.transactionDate < :transactionDate))
	ORDER BY t.transactionDate DESC, t.id
	LIMIT :limit
	""")
	List<Transaction> findTransactionsBySenderAccountWithCursor(
		@Param("account") String account,
		@Param("transactionDate") Instant transactionDate,
		@Param("id") int id,
		@Param("limit") int limit
	);

	@Query("""
	SELECT t
	FROM Transaction t
	WHERE t.receiverAccount = :account
	ORDER BY t.transactionDate DESC, t.id
	LIMIT :limit
	""")
	List<Transaction> findTransactionsByReceiverAccount(@Param("account") String account, @Param("limit") int limit);

	@Query("""
	SELECT t
	FROM Transaction t
	WHERE t.receiverAccount = :account
	AND ((t.transactionDate = :transactionDate AND t.id > :id) OR (t.transactionDate < :transactionDate))
	ORDER BY t.transactionDate DESC, t.id
	LIMIT :limit
	""")
	List<Transaction> findTransactionsByReceiverAccountWithCursor(
		@Param("account") String account,
		@Param("transactionDate") Instant transactionDate,
		@Param("id") int id,
		@Param("limit") int limit
	);
}
