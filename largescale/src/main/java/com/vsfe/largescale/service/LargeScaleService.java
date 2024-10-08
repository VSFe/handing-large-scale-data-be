package com.vsfe.largescale.service;

import com.vsfe.largescale.repository.AccountRepository;
import com.vsfe.largescale.repository.TransactionRepository;
import com.vsfe.largescale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LargeScaleService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public void getUserInfo() {

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
