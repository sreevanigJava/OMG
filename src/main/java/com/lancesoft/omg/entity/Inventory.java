package com.lancesoft.omg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.lancesoft.omg.idGenerator.CustomeIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Inventory {
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator = "user_sql")
    @GenericGenerator(name="user_sql", strategy="com.lancesoft.omg.idGenerator.CustomeIdGenerator", parameters = {
            @Parameter(name=CustomeIdGenerator.INCREMENT_PARAM, value="1"),
            @Parameter(name=CustomeIdGenerator.VALUE_PREFIX_PARAMAETER, value="INV"),
            @Parameter(name=CustomeIdGenerator.NUMBER_FORMAT_PARAMETER, value="%03d")
    })
	private String inventoryId;
	private String productName;
	@Column(precision=10, scale=2)
	private long quantity;
	

}

