package com.lancesoft.omg.entity;

import lombok.Data;

@Data
public class ChangePasswordEntity {
private String oldPassword;
private String newPassword;
private String confirmPassword;
}
