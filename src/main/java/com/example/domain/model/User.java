package com.example.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name ="user")
@Data
public class User implements Serializable{
  @Id
  @Column(name = "user_id")
  private String userId;
  
  @Column(name = "password")
  private String password;
  
  @Column(name = "role_name")
  @Enumerated(EnumType.STRING)
  private RoleName roleName;
}
