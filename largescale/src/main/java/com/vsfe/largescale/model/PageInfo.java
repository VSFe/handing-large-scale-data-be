package com.vsfe.largescale.model;

import java.util.List;

public record PageInfo<T>(
	String pageToken,
	List<T> data,
	boolean hasNext
) {
}
