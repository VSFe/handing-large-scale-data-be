package com.vsfe.largescale.model.type;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionSearchOption {
	SENDER("sender_account"),
	RECEIVER("receiver_account"),
	ALL(StringUtils.EMPTY);

	private final String targetColumn;
}
