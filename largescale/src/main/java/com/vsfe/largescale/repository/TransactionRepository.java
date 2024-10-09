package com.vsfe.largescale.repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Repository;

import com.vsfe.largescale.domain.Transaction;
import com.vsfe.largescale.model.PageInfo;
import com.vsfe.largescale.model.type.TransactionSearchOption;
import com.vsfe.largescale.util.C4PageTokenUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
	private final TransactionJpaRepository transactionJpaRepository;

	public PageInfo<Transaction> findTransactionWithoutPageToken(String accountNumber, TransactionSearchOption option, int count) {
		var data = switch (option) {
			case SENDER -> transactionJpaRepository.findTransactionsBySenderAccount(accountNumber, count + 1);
			case RECEIVER -> transactionJpaRepository.findTransactionsByReceiverAccount(accountNumber, count + 1);
			case ALL -> mergeAllOptions(
				transactionJpaRepository.findTransactionsBySenderAccount(accountNumber, count + 1),
				transactionJpaRepository.findTransactionsByReceiverAccount(accountNumber, count + 1),
				count + 1
			);
		};

		return PageInfo.of(data, count, Transaction::getTransactionDate, Transaction::getId);
	}

	public PageInfo<Transaction> findTransactionWithPageToken(String accountNumber, String pageToken, TransactionSearchOption option, int count) {
		var pageData = C4PageTokenUtil.decodePageToken(pageToken, Instant.class, Integer.class);
		var data = switch (option) {
			case SENDER -> transactionJpaRepository.findTransactionsBySenderAccountWithCursor(accountNumber, pageData.getLeft(), pageData.getRight(), count + 1);
			case RECEIVER -> transactionJpaRepository.findTransactionsByReceiverAccountWithCursor(accountNumber, pageData.getLeft(), pageData.getRight(), count + 1);
			case ALL -> mergeAllOptions(
				transactionJpaRepository.findTransactionsBySenderAccountWithCursor(accountNumber, pageData.getLeft(), pageData.getRight(), count + 1),
				transactionJpaRepository.findTransactionsByReceiverAccountWithCursor(accountNumber, pageData.getLeft(), pageData.getRight(), count + 1),
				count + 1
			);
		};

		return PageInfo.of(data, count, Transaction::getTransactionDate, Transaction::getId);
	}

	private List<Transaction> mergeAllOptions(List<Transaction> senderResult, List<Transaction> receiverResult, int count) {
		return ListUtils.union(senderResult, receiverResult).stream()
			.sorted(
				Comparator.comparing(Transaction::getTransactionDate).reversed()
					.thenComparing(Transaction::getId)
			)
			.limit(count)
			.toList();
	}
}
