package com.vsfe.largescale.repository;

import java.util.List;

import com.vsfe.largescale.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
	@Query
	List<Transaction> findTransactionsByUserIdAndOption();

	@Query
	List<Transaction> findTransactionsByUserIdAndOptionByCursor();


}
