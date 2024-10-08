package com.lamine.book.history;

import com.lamine.book.book.Book;
import com.lamine.book.common.BaseEntity;
import com.lamine.book.user.User;
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
public class BookTransactionHistory extends BaseEntity {

  @ManyToOne private User user;
  @ManyToOne private Book book;

  private boolean returned;
  private boolean returnedApproved;
}
