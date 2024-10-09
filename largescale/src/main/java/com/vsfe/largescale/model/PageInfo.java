package com.vsfe.largescale.model;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.vsfe.largescale.util.C4PageTokenUtil;

/**
 * cursor 페이징을 위한 정보
 * @param pageToken cursor 정보 (다음 호출 시 사용)
 * @param data 데이터 목록
 * @param hasNext 다음 페이지 존재 여부
 * @param <T>
 */
public record PageInfo<T>(
	String pageToken,
	List<T> data,
	boolean hasNext
) {
	public static <T> PageInfo<T> of(
		List<T> data,
		int expectedSize,
		Function<T, Object> firstPageTokenFunction,
		Function<T, Object> secondPageTokenFunction
	) {
		if (data.size() <= expectedSize) {
			return new PageInfo<>(null, data, false);
		}

		var lastValue = data.get(expectedSize - 1);
		var pageToken = C4PageTokenUtil.encodePageToken(Pair.of(
			firstPageTokenFunction.apply(lastValue),
			secondPageTokenFunction.apply(lastValue)
		));

		return new PageInfo<>(pageToken, data.subList(0, expectedSize), true);
	}
}
