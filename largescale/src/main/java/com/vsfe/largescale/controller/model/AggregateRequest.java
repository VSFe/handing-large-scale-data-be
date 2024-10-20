package com.vsfe.largescale.controller.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record AggregateRequest(
	@NotEmpty List<String> userIds
) {

}
