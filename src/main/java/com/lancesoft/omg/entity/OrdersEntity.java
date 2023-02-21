package com.lancesoft.omg.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.lancesoft.omg.idGenerator.CustomeIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrdersEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_sql")
	@GenericGenerator(name = "user_sql", strategy = "com.lancesoft.omg.idGenerator.CustomeIdGenerator", parameters = {
			@Parameter(name = CustomeIdGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = CustomeIdGenerator.VALUE_PREFIX_PARAMAETER, value = "1"),
			@Parameter(name = CustomeIdGenerator.NUMBER_FORMAT_PARAMETER, value = "1"),

	})
	private String orderId;
	private String userName;
	@OneToOne(cascade = CascadeType.ALL)
	private AddressEntity addressEntity;
	@OneToOne(cascade = CascadeType.ALL)
	private MyCartList myCartList;
	//private LocalDate date;
	private String deliveryTime;
	private String paymentMode;
	private String paymentStatus;
}
