package com.lancesoft.omg.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.lancesoft.omg.idGenerator.CustomeIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class MyCartList {
@Id
@GeneratedValue(strategy= GenerationType.IDENTITY, generator = "user_sql")
@GenericGenerator(name="user_sql", strategy="com.lancesoft.omg.idGenerator.CustomeIdGenerator", parameters = {
        @Parameter(name=CustomeIdGenerator.INCREMENT_PARAM, value="1"),
        @Parameter(name=CustomeIdGenerator.VALUE_PREFIX_PARAMAETER, value="CARTL"),
        @Parameter(name=CustomeIdGenerator.NUMBER_FORMAT_PARAMETER, value="%03d")

       
})
private String id; 
private String userName;
@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
@JoinColumn(name="myCartItems")
private List<MyCart> myCartItems;
private long totalCost;
}
