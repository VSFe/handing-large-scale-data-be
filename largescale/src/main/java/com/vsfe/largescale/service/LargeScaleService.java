package com.vsfe.largescale.service;

import com.vsfe.largescale.domain.Account;
import com.vsfe.largescale.domain.Transaction;
import com.vsfe.largescale.domain.User;
import com.vsfe.largescale.model.PageInfo;
import com.vsfe.largescale.model.type.TransactionSearchOption;
import com.vsfe.largescale.repository.AccountRepository;
import com.vsfe.largescale.repository.TransactionRepository;
import com.vsfe.largescale.repository.UserRepository;
import com.vsfe.largescale.util.C4QueryExecuteTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public List<User> getUserInfo(int count) {
//        return userRepository.findRecentUpdatedUsers(count);
        return userRepository.findRecentCreatedUsers(count);
    }

    /**
     * 거래 내역을 가져오자.
     * 어떤 페이징 전략을 사용해야 하는가? 페이징 조건은 어떻게 설정해야 하는가?
     * @param accountNumber
     * @param pageToken
     * @param option
     * @param count
     * @return
     */
    public PageInfo<Transaction> getTransactions(String accountNumber, String pageToken, TransactionSearchOption option, int count) {
        if (pageToken == null) {
            return transactionRepository.findTransactionWithoutPageToken(accountNumber, option, count);
        } else {
            return transactionRepository.findTransactionWithPageToken(accountNumber, pageToken, option, count);
        }
    }

    /**
     * Full-Scan 로직을 효율적으로 작성 해보자.
     * 데이터의 수가 아주 많은 경우, 어떻게 해야 할까?
     * @param pageSize
     */
    public void validateAccountNumber(int pageSize) {
        C4QueryExecuteTemplate.<Account>selectAndExecuteWithCursorAndPageLimit(pageSize, 1000,
            lastAccount -> accountRepository.findAccountByLastAccountId(lastAccount == null ? null : lastAccount.getId(), 1000),
            accounts -> accounts.forEach(this::validateAccount)
        );
    }

    public void aggregateTransactions() {

    }

    public void aggregateTransactionsWithSharding() {

    }

    private void validateAccount(Account account) {
        if (!account.validateAccountNumber()) {
            log.error("invalid accountNumber - accountNumber:{}", account.getAccountNumber());
        }
    }
}
