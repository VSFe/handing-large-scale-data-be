package com.vsfe.largescale.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class C4QueryExecuteTemplate {

	/**
	 * Cursor Paging 을 적용하여 Select 를 수행한 후, 조회된 결과로 비즈니스 로직을 수행한다.
	 * @param limit
	 * @param selectFunction
	 * @param resultConsumer
	 * @param <T>
	 */
	public static <T> void selectAndExecuteWithCursor(
		int limit,
		Function<T, List<T>> selectFunction,
		Consumer<List<T>> resultConsumer
	) {
		List<T> resultList = null;
		do {
			resultList = selectFunction.apply(resultList != null ? resultList.get(resultList.size() - 1) : null);
			if (!resultList.isEmpty()) {
				resultConsumer.accept(resultList);
			}
		} while (resultList.size() >= limit);
	}
}
