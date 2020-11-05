package com.example.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "todo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Todo implements Serializable{
  /** ToDoごとに固有のID.自動採番を利用 **/
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "todo_id")
  private Integer todoId;

  /** ToDoを作成したユーザーの固有ID. **/
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  /** ToDoのタイトル. **/
  @Column(name = "title")
  private String title;

  /** ToDoの期限. **/
  @Column(name = "deadline")
  private LocalDateTime deadline;

  /** ToDoの優先度. **/
  @Column(name = "priority")
  private Integer priority;

  /** メモ. **/
  @Column(name = "memo")
  private String memo;

  /** 完了済みかどうか. **/
  @Column(name = "completed")
  private boolean completed;

  /** 最終更新日時.*/
  @Column(name = "last_update")
  private LocalDateTime lastUpdate;
}
