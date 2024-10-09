package com.vsfe.largescale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vsfe.largescale.domain.Transaction;
import com.vsfe.largescale.domain.User;
import com.vsfe.largescale.model.PageInfo;
import com.vsfe.largescale.model.type.TransactionSearchOption;
import com.vsfe.largescale.repository.AccountJpaRepository;
import com.vsfe.largescale.repository.TransactionRepository;
import com.vsfe.largescale.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LargeScaleService {
    private final AccountJpaRepository accountJpaRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    /**
     * 최신 유저의 목록을 가져올 것이다. (count 만큼)
     * "최신" 의 조건을 바꿔가면서 쿼리를 날려보자.
     * @param count
     * @return
     */
    public List<User> getUserInfo(int count) {
        return userRepository.findRecentCreatedUsers(count);
    }

    /**
     * 거래 내역을 가져오자.
     * @param accountNumber
     * @param pageToken
     * @param option
     * @param size
     */
    public PageInfo<Transaction> getTransactions(String accountNumber, String pageToken, TransactionSearchOption option, int size) {
        if (pageToken == null) {
            return transactionRepository.findTransactionWithoutPageToken(accountNumber, option, size);
        } else {
            return transactionRepository.findTransactionWithPageToken(accountNumber, pageToken, option, size);
        }
    }

    public void validateAccountNumber() {

    }

    public void aggregateTransactions() {

    }

    public void aggregateTransactionsWithSharding() {

    }
}
