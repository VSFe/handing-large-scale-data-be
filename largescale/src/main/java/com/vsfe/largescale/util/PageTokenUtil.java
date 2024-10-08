package com.vsfe.largescale.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageTokenUtil {
	public static final String PAGE_TOKEN_FORMAT = "{}|{}";

	public static String encodePageToken(Pair<String, String> data) {
		return Base64.encodeBase64URLSafeString(
			C4StringUtil.format(PAGE_TOKEN_FORMAT, data.getLeft(), data.getRight()).getBytes(StandardCharsets.UTF_8)
		);
	}

	public static Pair<String, String> decodePageToken(String pageToken) {
		var parts = Arrays.toString(Base64.decodeBase64(pageToken)).split("\\|");
		Assert.isTrue(parts.length == 2, "invalid pageToken");
		return Pair.of(parts[0], parts[1]);
	}
}
