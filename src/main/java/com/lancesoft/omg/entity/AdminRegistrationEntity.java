package com.lancesoft.omg.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.repository.NoRepositoryBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="User_Data")

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminRegistrationEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer user_Id;
private String firstName;
private String lastName;
private String userName;
private String password;
private String confirmPassword;
private String gender;
private String email;
private String phoneNumber;
private String  dob;
@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
@JoinTable(name="user_role", joinColumns = @JoinColumn(name="user_Id"), inverseJoinColumns = @JoinColumn(name="role_id"))
private List<Authorities> authorities;  


}

