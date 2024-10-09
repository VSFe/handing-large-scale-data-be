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

	/**
	 * pageToken 을 사용하지 않고 Cursor 페이징 쿼리를 호출한다.
	 * @param accountNumber
	 * @param option
	 * @param count
	 * @return
	 */
	public PageInfo<Transaction> findTransactionWithoutPageToken(
		String accountNumber,
		TransactionSearchOption option,
		int count
	) {
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

	/**
	 * pageToken 을 포함하여 Cursor 페이징 쿼리를 호출한다.
	 * @param accountNumber
	 * @param pageToken
	 * @param option
	 * @param count
	 * @return
	 */
	public PageInfo<Transaction> findTransactionWithPageToken(
		String accountNumber,
		String pageToken,
		TransactionSearchOption option,
		int count
	) {
		var pageData = C4PageTokenUtil.decodePageToken(pageToken, Instant.class, Integer.class);
		var transactionDate = pageData.getLeft();
		var transactionId = pageData.getRight();

		var data = switch (option) {
			case SENDER -> transactionJpaRepository.findTransactionsBySenderAccountWithPageToken(accountNumber, transactionDate, transactionId, count + 1);
			case RECEIVER -> transactionJpaRepository.findTransactionsByReceiverAccountWithPageToken(accountNumber, transactionDate, transactionId, count + 1);
			case ALL -> mergeAllOptions(
				transactionJpaRepository.findTransactionsBySenderAccountWithPageToken(accountNumber, transactionDate, transactionId, count + 1),
				transactionJpaRepository.findTransactionsByReceiverAccountWithPageToken(accountNumber, transactionDate, transactionId, count + 1),
				count + 1
			);
		};

		return PageInfo.of(data, count, Transaction::getTransactionDate, Transaction::getId);
	}

	/**
	 * 두 결과를 합쳐서, 데이터를 정렬 조건에 맞춰 count 개 만큼 가져온다.
	 * @param senderResult
	 * @param receiverResult
	 * @param count
	 * @return
	 */
	private List<Transaction> mergeAllOptions(
		List<Transaction> senderResult,
		List<Transaction> receiverResult,
		int count
	) {
		return ListUtils.union(senderResult, receiverResult).stream()
			.sorted(
				Comparator.comparing(Transaction::getTransactionDate).reversed()
					.thenComparing(Transaction::getId)
			)
			.limit(count)
			.toList();
	}
}
