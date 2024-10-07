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
@Table(name = "transaction")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", nullable = false)
	private Integer id;

	@Size(max = 20)
	@NotNull
	@Column(name = "sender_account", nullable = false, length = 20)
	private String senderAccount;

	@Size(max = 20)
	@NotNull
	@Column(name = "receiver_account", nullable = false, length = 20)
	private String receiverAccount;

	@Size(max = 11)
	@NotNull
	@Column(name = "sender_swift_code", nullable = false, length = 11)
	private String senderSwiftCode;

	@Size(max = 11)
	@NotNull
	@Column(name = "receiver_swift_code", nullable = false, length = 11)
	private String receiverSwiftCode;

	@Size(max = 20)
	@NotNull
	@Column(name = "sender_name", nullable = false, length = 20)
	private String senderName;

	@Size(max = 20)
	@NotNull
	@Column(name = "receiver_name", nullable = false, length = 20)
	private String receiverName;

	@NotNull
	@Column(name = "amount", nullable = false)
	private Long amount;

	@Size(max = 200)
	@Column(name = "memo", length = 200)
	private String memo;

	@NotNull
	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "transaction_date", nullable = false)
	private Instant transactionDate;

}