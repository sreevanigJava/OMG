package com.lancesoft.omg.dto;

import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.lancesoft.omg.entity.CategoriesEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsDto {
private String prodId;
private String prodName;
private String description;
@CreationTimestamp
@Temporal(TemporalType.TIMESTAMP)
private Date addOnDate;
private String price;
private String imageUrl;
private String status;
@ManyToOne
@JoinColumn
private CategoriesEntity categoriesEntity;
}
