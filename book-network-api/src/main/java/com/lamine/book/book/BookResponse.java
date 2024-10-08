package com.lamine.book.book;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

  private Integer id;
  private String title;
  private String authorName;
  private String synospis;
  private String isbn;
  private String owner;
  private byte[] cover;
  private double rate;
  private Boolean archived;
  private Boolean shareable;
}
