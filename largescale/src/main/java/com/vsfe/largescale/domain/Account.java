package com.vsfe.largescale.domain;

import java.time.Instant;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id", nullable = false)
	private Integer id;

	@Size(max = 13)
	@NotNull
	@Column(name = "account_number", nullable = false, length = 13)
	private String accountNumber;

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@NotNull
	@Column(name = "account_type", nullable = false)
	private Character accountType;

	@Size(max = 200)
	@Column(name = "memo", length = 200)
	private String memo;

	@NotNull
	@ColumnDefault("0")
	@Column(name = "balance", nullable = false)
	private Long balance;

	@NotNull
	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "create_date", nullable = false)
	private Instant createDate;

	@Column(name = "recent_transaction_date")
	private Instant recentTransactionDate;

}