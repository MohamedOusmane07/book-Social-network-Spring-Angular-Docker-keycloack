package com.lamine.book.book;

import com.lamine.book.common.BaseEntity;
import com.lamine.book.feedBack.FeedBack;
import com.lamine.book.history.BookTransactionHistory;
import com.lamine.book.user.User;
import jakarta.persistence.*;
import java.util.List;
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
public class Book extends BaseEntity {
  private String title;
  private String authorName;
  private String isbn;
  private String synopsis;
  private String bookCover;
  private Boolean archived;
  private Boolean shareable;

  @ManyToOne private User owner;

  @OneToMany(mappedBy = "book")
  private List<FeedBack> feedBacks;

  @OneToMany(mappedBy = "book")
  private List<BookTransactionHistory> histories;

  @Transient
  public double getRate(){
    if (feedBacks == null || feedBacks.isEmpty()){
      return 0.0;
    }
    else {
      var rate=feedBacks.stream()
              .mapToDouble(FeedBack::getNote)
              .average()
              .orElse(0.0); // 4.21

      return Math.round(rate);
    }
  }
}
