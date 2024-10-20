package com.vsfe.largescale.repository;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vsfe.largescale.domain.Account;
import com.vsfe.largescale.util.C4StringUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
	private final AccountJpaRepository accountJpaRepository;
	private final JdbcTemplate jdbcTemplate;

	/**
	 * 주어진 lastAccountId 를 활용하여, size 만큼의 Account 정보를 가져온다.
	 * @param lastAccountId
	 * @param size
	 * @return
	 */
	public List<Account> findAccountByLastAccountId(Integer lastAccountId, int size) {
		if (lastAccountId == null) {
			return accountJpaRepository.findAccount(size);
		}

		return accountJpaRepository.findAccountWithLastAccountId(lastAccountId, size);
	}

	/**
	 *
	 * @param userId
	 * @param lastAccountId
	 * @param size
	 * @return
	 */
	public List<Account> findAccountByUserIdAndLastAccountId(Integer userId, Integer lastAccountId, int size) {
		if (lastAccountId == null) {
			return accountJpaRepository.findAccountByUserId(userId, size);
		}

		return accountJpaRepository.findAccountByUserIdWithLastUserId(userId, lastAccountId, size);
	}

	/**
	 * @param groupId
	 * @param accounts
	 */
	public void saveAll(int groupId, List<Account> accounts) {
		var sql = C4StringUtil.format("""
			INSERT INTO account_migration_test_{} (account_id, account_number, user_id, account_type, memo, balance, create_date, recent_transaction_date)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""", groupId);

		jdbcTemplate.batchUpdate(sql,
			accounts, accounts.size(),
			(PreparedStatement ps, Account account) -> {
				ps.setInt(1, account.getId());
				ps.setString(2, account.getAccountNumber());
				ps.setInt(3, account.getUserId());
				ps.setString(4, account.getAccountType().toString());
				ps.setString(5, account.getMemo());
				ps.setDouble(6, account.getBalance());
				ps.setObject(7, account.getCreateDate());
				ps.setObject(8, account.getRecentTransactionDate());
			});
	}
}
