package com.vsfe.largescale.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
	@Id
	@Column(name = "user_id", nullable = false)
	private Integer id;

	@Size(max = 30)
	@NotNull
	@Column(name = "username", nullable = false, length = 30)
	private String username;

	@Size(max = 30)
	@NotNull
	@Column(name = "email", nullable = false, length = 30)
	private String email;

	@Size(max = 10)
	@NotNull
	@Column(name = "nickname", nullable = false, length = 10)
	private String nickname;

	@NotNull
	@Column(name = "group_id", nullable = false)
	private Integer groupId;

	@NotNull
	@Column(name = "user_status", nullable = false)
	private Character userStatus;

	@NotNull
	@Column(name = "create_date", nullable = false)
	private Instant createDate;

	@NotNull
	@Column(name = "update_date", nullable = false)
	private Instant updateDate;

}