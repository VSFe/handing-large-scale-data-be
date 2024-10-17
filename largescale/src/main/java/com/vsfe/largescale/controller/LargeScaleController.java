package com.vsfe.largescale.controller;

import com.vsfe.largescale.domain.Transaction;
import com.vsfe.largescale.domain.User;
import com.vsfe.largescale.model.PageInfo;
import com.vsfe.largescale.model.type.TransactionSearchOption;
import com.vsfe.largescale.service.LargeScaleService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 예시 중심의 세미나이므로,
 * 실제 서비스와는 달리 사용하는 API만 Controller 형태로 구성했습니다.
 */
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class LargeScaleController {
    private final LargeScaleService largeScaleService;

    /**
     * Step 1. 기본적인 쿼리의 최적화를 수행해 봅시다.
     */
    @GetMapping("/user-info")
    public List<User> getUserInfo(@RequestParam @Positive @Max(100) int count) {
        return largeScaleService.getUserInfo(count);
    }

    /**
     * Step 2. 페이징을 활용한 쿼리 최적화 방식에 대해 고민해 봅시다.
     */
    @GetMapping("/get-transactions")
    public PageInfo<Transaction> getTransactions(
        @RequestParam @NotEmpty String accountNumber,
        @RequestParam(required = false) String pageToken,
        @RequestParam @NotNull TransactionSearchOption option,
        @RequestParam @Positive @Max(100) int count
    ) {
        return largeScaleService.getTransactions(accountNumber, pageToken, option, count);
    }

    /**
     * Step 3. Full Scan 을 수행해야 하는 로직은 어떻게 수행해야 할까요?
     */
    @GetMapping("/validate-account")
    public void validateAccountNumber(@RequestParam int pageSize) {
        largeScaleService.validateAccountNumber(pageSize);
    }

    /**
     * Step 4. 병렬 처리를 사용한 마이그레이션 작업을 수행해 봅시다.
     */
    @GetMapping("/migrate-data")
    public void migrateData() {
        largeScaleService.migrateData();
    }

    /**
     * Step 5. 데이터를 샤딩한다면 어떻게 될까요?
     */
    public void aggregateTransactionsWithSharding() {

    }
}
