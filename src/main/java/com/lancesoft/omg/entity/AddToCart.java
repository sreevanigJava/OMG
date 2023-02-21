package com.lancesoft.omg.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
public class AddToCart {
	
	private String prodId;
	private long qty;
}
