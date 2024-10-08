package com.vsfe.largescale.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class C4QueryExecuteTemplate {
    /**
     * limit 보다 affected 가 작을 때 까지 반복 수행한다.
     * delete/update 쿼리를 limit 로 반복 수행할 때 사용한다.
     * @param limit
     * @param executeSupplier
     * @return
     */
    public static int execute(int limit, IntSupplier executeSupplier) {
        var totalAffected = 0;
        var affected = 0;

        do {
            affected = executeSupplier.getAsInt();
            totalAffected += affected;
        } while (affected >= limit);

        return totalAffected;
    }

    /**
     * select 의 조회 결과가 더 나오지 않을 때 까지 반복하여 update/delete 연산을 수핸한다.
     * 주의: consumer가 update 인 경우, selectFunction 에서 조회된 값이 consumer 동작 이후 재 조회되지 않아야 한다.
     * @param limit
     * @param selectFunction
     * @param resultConsumer
     * @param <T>
     */
    public static <T> void selectAndExecute(int limit, IntFunction<List<T>> selectFunction, Consumer<List<T>> resultConsumer) {
        List<T> resultList;
        while (CollectionUtils.isNotEmpty(resultList = selectFunction.apply(limit))) {
            resultConsumer.accept(resultList);
        }
    }

    /**
     * Cursor Paging 을 적용하여 select 를 수행한 후, 조회된 결과로 비즈니스 로직을 수행한다.
     * @param limit
     * @param selectFunction
     * @param resultConsumer
     * @param <T>
     */
    public static <T> void selectAndExecuteWithCursor(int limit, Function<T, List<T>> selectFunction, Consumer<List<T>> resultConsumer) {
        List<T> resultList = null;
        do {
            resultList = selectFunction.apply(resultList != null ? resultList.get(resultList.size() - 1) : null);
            if (!resultList.isEmpty()) {
                resultConsumer.accept(resultList);
            }
        } while (resultList.size() >= limit);
    }

    /**
     * Cursor Paging 을 적용하여 select 를 수행한 후, 조회된 결과로 비즈니스 로직을 수행한다.
     * 단, iteration 횟수가 pageLimit 에 도달한 경우 중단한다.
     * @param pageLimit
     * @param limit
     * @param selectFunction
     * @param resultConsumer
     * @param <T>
     */
    public static <T> void selectAndExecuteWithCursorAndPageLimit(int pageLimit, int limit, Function<T, List<T>> selectFunction, Consumer<List<T>> resultConsumer) {
        var iterationCount = 0;
        List<T> resultList = null;
        do {
            resultList = selectFunction.apply(resultList != null ? resultList.get(resultList.size() - 1) : null);
            if (!resultList.isEmpty()) {
                resultConsumer.accept(resultList);
            }
            if (++iterationCount >= pageLimit) {
                break;
            }
        } while (resultList.size() >= limit);
    }
}
