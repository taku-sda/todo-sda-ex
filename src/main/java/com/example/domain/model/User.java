package com.example.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * usrテーブルに対応するエンティティ.
 * 主にユーザーの認証、認可に用いる
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "usr")
@AllArgsConstructor
@Getter
@Setter
public class User implements Serializable {
  /** ユーザー固有のID文字列.*/
  @Id
  @Column(name = "user_id")
  private String userId;

  /** ユーザー認証用パスワード.*/
  @Column(name = "password")
  private String password;

  /** ユーザーが持つロール.*/
  @Column(name = "role_name")
  @Enumerated(EnumType.STRING)
  private RoleName roleName;
}
