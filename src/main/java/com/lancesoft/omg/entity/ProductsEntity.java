package com.lancesoft.omg.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_sql")
	@GenericGenerator(name = "user_sql", strategy = "com.lancesoft.omg.idGenerator.CustomeIdGenerator", parameters = {
			@Parameter(name = CustomeIdGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = CustomeIdGenerator.VALUE_PREFIX_PARAMAETER, value = "PROD"),
			@Parameter(name = CustomeIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d") })
	private String prodId;
	private String prodName;
	private String description;
	private long price;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date AddOnDate;
	private String imageUrl;
	private String status;
	@ManyToOne
	@JoinColumn(name="catId")
	private CategoriesEntity categoriesEntity;
}