package com.lancesoft.omg.entity;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.lancesoft.omg.idGenerator.CustomeIdGenerator;

import lombok.Data;

@Data
@Entity
@Table
public class MyCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_sql")
	@GenericGenerator(name = "user_sql", strategy = "com.lancesoft.omg.idGenerator.CustomeIdGenerator", parameters = {
			@Parameter(name = CustomeIdGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = CustomeIdGenerator.VALUE_PREFIX_PARAMAETER, value = "CART"),
			@Parameter(name = CustomeIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d") })

	private String cartId;
	private String userName;
	@ManyToOne
	@JoinColumn(name = "prodId")
	private ProductsEntity productsEntity;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date addOnDate;
	private long quantity;
}
