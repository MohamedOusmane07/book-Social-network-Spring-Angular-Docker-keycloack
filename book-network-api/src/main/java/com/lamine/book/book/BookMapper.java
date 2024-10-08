package com.lamine.book.book;

import com.lamine.book.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

  public Book toBook(BookRequest request) {

    return Book.builder()
        .id(request.id())
        .title(request.title())
        .authorName(request.authorName())
        .synopsis(request.synopsis())
        .archived(false)
        .isbn(request.isbn())
        .shareable(request.shareable())
        .build();
  }

  public BookResponse toBookResponse(Book book) {

    return BookResponse.builder()
        .id(book.getId())
        .title(book.getTitle())
        .authorName(book.getAuthorName())
        .isbn(book.getIsbn())
        .owner(book.getOwner().fullName())
        .shareable(book.getShareable())
        .archived(book.getArchived())
        .synospis(book.getSynopsis())
        // cover
        .build();
  }

  public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {

    return BorrowedBookResponse.builder()
        .id(history.getBook().getId())
        .title(history.getBook().getTitle())
        .authorName(history.getBook().getAuthorName())
        .isbn(history.getBook().getIsbn())
        .rate(history.getBook().getRate())
        .returned(history.isReturned())
        .returnApproved(history.isReturnedApproved())
        .build();
  }
}
