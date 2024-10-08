package com.vsfe.largescale.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vsfe.largescale.domain.Account;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
	private final AccountJpaRepository accountJpaRepository;

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
}
