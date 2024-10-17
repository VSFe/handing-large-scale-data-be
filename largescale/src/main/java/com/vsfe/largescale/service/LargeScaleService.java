package com.vsfe.largescale.service;

import com.vsfe.largescale.core.C4ThreadPoolExecutor;
import com.vsfe.largescale.domain.Account;
import com.vsfe.largescale.domain.Transaction;
import com.vsfe.largescale.domain.User;
import com.vsfe.largescale.model.PageInfo;
import com.vsfe.largescale.model.type.TransactionSearchOption;
import com.vsfe.largescale.repository.AccountRepository;
import com.vsfe.largescale.repository.TransactionRepository;
import com.vsfe.largescale.repository.UserRepository;
import com.vsfe.largescale.util.C4QueryExecuteTemplate;
import com.vsfe.largescale.util.C4StringUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LargeScaleService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    /**
     * 일반적으로 ThreadPool을 Bean 으로 선언해서 사용하는 편인데, (요청이 들어올 때 마다 스레드풀이 생성되는 것을 방지하기 위함)
     * 학습 목적이므로 여기서는 그런 과정을 수행하지 않음.
     * CompletableFuture 에 대해 아시면 다른 방식으로도 가능합니다. (default ForkJoinPool 을 사용한 처리 가능)
     */
    private final C4ThreadPoolExecutor threadPoolExecutor = new C4ThreadPoolExecutor(8, 32);


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

    /**
     * 공포의 데이터 마이그레이션...
     * User.groupId 에 해당하는 그룹에 맞춰 데이터를 이전합니다.
     * @param pageSize
     */
    public void migrateData(int pageSize) {
        C4QueryExecuteTemplate.<User>selectAndExecuteWithCursorAndPageLimit(pageSize, 100,
            lastUser -> userRepository.findRecentCreatedUsers(1),
            users -> users.forEach(this::doSomething)
        );

        threadPoolExecutor.waitToEnd();
    }

    /**
     * 샤딩한 데이터를 기반으로, 통계 집계를 수행합니다.
     * @param today
     */
    public void aggregateTransactionsWithSharding(Instant today) {
        // 실력이 있으시다면... executorService 에 일종의 callback 을 정의해서 삽입해서 처리할 수도 있습니다.
        // 여기서는 빙빙 돌아가도 좀 덜 어려운 방식으로 구현할게요.

        var todayAmount = new AtomicLong();
        var totalAmount = new AtomicLong();

        IntStream.rangeClosed(0, 1).forEach(idx -> {
            // 현재는 groupId에 따라 테이블이 분리된 상황
            // 각각에 대해서 병렬적으로 집계를 수행하자
            // 사실 이건 C4ThreadPoolExecutor 를 쓰기엔 적합하지 않지만....
            // 다른 방식을 설명하기엔 시간이 부족하므로 그냥 재활용합니다 ㅠㅠㅠ

            var tableName = C4StringUtil.format("transaction_sd_{}", idx);
            // 오늘꺼는 그냥 여유 있다고 믿고 대충 한방 쿼리로 긁기

            // 100건씩 쓸어서 가져옴
            //
        });

    }

    /**
     * 유저 정보를 기반으로...
     * @param user
     */
    private void doSomething(User user) {
        threadPoolExecutor.execute(() -> {
            var groupId = user.getGroupId();

            // 계좌번호 페이징으로 가져옴
            // 1 - 계좌번호 데이터를 groupId 에 해당 하는 곳에 삽입
            // 2 - 계좌번호 기반으로 쿼리를 호출하여, 거래 내역을 가져옴
            // 3 - 해당 거래 내역을 groupId 에 맞춰서 삽입
        });
    }

    private void validateAccount(Account account) {
        if (!account.validateAccountNumber()) {
            log.error("invalid accountNumber - accountNumber:{}", account.getAccountNumber());
        }
    }
}
