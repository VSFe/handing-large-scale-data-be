package com.vsfe.largescale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vsfe.largescale.domain.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {
	private final UserJpaRepository userJpaRepository;

	/**
	 *
	 * @param count
	 * @return
	 */
	public List<User> findRecentCreatedUsers(int count) {
		return userJpaRepository.findRecentCreatedUsers(count);
	}

	/**
	 *
	 * @param count
	 * @return
	 */
	public List<User> findRecentUpdatedUsers(int count) {
		return userJpaRepository.findRecentUpdatedUsers(count);
	}

	/**
	 *
	 * @param lastUserId
	 * @param count
	 * @return
	 */
	public List<User> findUsersWithLastUserId(int lastUserId, int count) {
		return userJpaRepository.findUsersWithLastUserId(lastUserId, count);
	}
}
