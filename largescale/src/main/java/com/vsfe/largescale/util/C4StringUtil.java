package com.vsfe.largescale.util;

import org.slf4j.helpers.MessageFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class C4StringUtil {
	/**
	 * 형식과 객체가 주어졌을 때, 해당 형식에 맞춰 객체를 배치한다.
	 * @param format
	 * @param objects
	 * @return
	 */
	public static String format(String format, Object... objects) {
		return MessageFormatter.arrayFormat(format, objects).getMessage();
	}
}
