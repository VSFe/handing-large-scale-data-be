package com.vsfe.largescale.repository;

import org.springframework.stereotype.Repository;

import com.vsfe.largescale.domain.Transaction;
import com.vsfe.largescale.model.PageInfo;
import com.vsfe.largescale.model.type.TransactionSearchOption;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
	private final TransactionJpaRepository transactionJpaRepository;

	public PageInfo<Transaction> findTransactionWithoutPageToken(String account, TransactionSearchOption option, int count) {
		return null;
	}

	public PageInfo<Transaction> findTransactionWithPageToken(String account, TransactionSearchOption option, String pageToken, int count) {
		return null;
	}
}
