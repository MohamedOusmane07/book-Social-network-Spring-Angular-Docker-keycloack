package com.lamine.book.feedBack;

import com.lamine.book.book.Book;
import com.lamine.book.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class FeedBack extends BaseEntity {

  private Double note;
  private String comment;

  @ManyToOne private Book book;
}
