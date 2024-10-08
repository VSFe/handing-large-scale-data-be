package com.vsfe.largescale.service;

import com.vsfe.largescale.domain.User;
import com.vsfe.largescale.repository.AccountRepository;
import com.vsfe.largescale.repository.TransactionRepository;
import com.vsfe.largescale.repository.UserRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LargeScaleService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    /**
     * 최신 유저의 목록을 가져온다.
     * 최신의 조건이 어떻게 될까?
     * @param count
     * @return
     */
    public List<User> getUserInfo(@Positive @Max(100) int count) {
//        return userRepository.findRecentUpdatedUsers(count);
        return userRepository.findRecentCreatedUsers(count);
    }

    public void getTransactions() {

    }

    public void validateAccountNumber() {

    }

    public void aggregateTransactions() {

    }

    public void aggregateTransactionsWithSharding() {

    }
}
