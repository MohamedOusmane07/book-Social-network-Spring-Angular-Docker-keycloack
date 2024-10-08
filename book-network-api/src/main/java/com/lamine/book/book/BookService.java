package com.lamine.book.book;

import static com.lamine.book.book.BookSpecification.*;

import com.lamine.book.common.PageResponse;
import com.lamine.book.exception.OperationNotPermittedException;
import com.lamine.book.history.BookTransactionHistory;
import com.lamine.book.history.BookTransactionHistoryRepository;
import com.lamine.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final BookTransactionHistoryRepository transactionHistoryRepository;
  private final BookMapper bookMapper;
  private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

  public Integer saveBook(BookRequest bookRequest, Authentication connectedUser) {

    User user = (User) connectedUser.getPrincipal();
    Book book = bookMapper.toBook(bookRequest);
    book.setOwner(user);
    return bookRepository.save(book).getId();
  }

  public BookResponse findBookById(Integer bookId) {
    return bookRepository
        .findById(bookId)
        .map(bookMapper::toBookResponse)
        .orElseThrow(() -> new EntityNotFoundException("Book with ID : " + bookId + " not found"));
  }

  public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Book> books = bookRepository.findAllDisplayablesBooks(pageable, user.getId());
    List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();

    return new PageResponse<>(
        bookResponses,
        books.getNumber(),
        books.getSize(),
        books.getTotalElements(),
        books.getTotalPages(),
        books.isFirst(),
        books.isLast());
  }

  public PageResponse<BookResponse> findBooksByOwner(
      int page, int size, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Book> bookPage = bookRepository.findAll(withOwnerId(user.getId()), pageable);
    List<BookResponse> bookResponses = bookPage.stream().map(bookMapper::toBookResponse).toList();

    return new PageResponse<>(
        bookResponses,
        bookPage.getNumber(),
        bookPage.getSize(),
        bookPage.getTotalElements(),
        bookPage.getTotalPages(),
        bookPage.isFirst(),
        bookPage.isLast());
  }

  public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(
      int page, int size, Authentication connectedUser) {

    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<BookTransactionHistory> allBorrowedBooks =
        transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
    List<BorrowedBookResponse> booksResponse =
        allBorrowedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();

    return new PageResponse<>(
        booksResponse,
        allBorrowedBooks.getNumber(),
        allBorrowedBooks.getSize(),
        allBorrowedBooks.getTotalElements(),
        allBorrowedBooks.getTotalPages(),
        allBorrowedBooks.isFirst(),
        allBorrowedBooks.isLast());
  }

  public PageResponse<BorrowedBookResponse> findAllReturnedBooks(
      int page, int size, Authentication connectedUser) {

    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<BookTransactionHistory> allBorrowedBooks =
        transactionHistoryRepository.findAllReturnedBooks(page, user.getId());
    List<BorrowedBookResponse> booksResponse =
        allBorrowedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();

    return new PageResponse<>(
        booksResponse,
        allBorrowedBooks.getNumber(),
        allBorrowedBooks.getSize(),
        allBorrowedBooks.getTotalElements(),
        allBorrowedBooks.getTotalPages(),
        allBorrowedBooks.isFirst(),
        allBorrowedBooks.isLast());
  }

  public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(
                () -> new EntityNotFoundException("Book with ID : " + bookId + " not found"));

    User user = (User) connectedUser.getPrincipal();
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot update others books shareable status");
    }
    book.setShareable(!book.isShareable());
    bookRepository.save(book);
    return bookId;
  }

  public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(
                () -> new EntityNotFoundException("Book with ID : " + bookId + " not found"));

    User user = (User) connectedUser.getPrincipal();
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {

      throw new OperationNotPermittedException("You cannot update others books  archived status");
    }
    book.setArchived(!book.isArchived());
    bookRepository.save(book);
    return bookId;
  }

  public Integer borrowBook(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(
                () -> new EntityNotFoundException("Book with ID : " + bookId + " not found"));

    if (!book.isShareable() || book.isArchived()) {
      throw new OperationNotPermittedException(
          "The requested book cannot be borrowed because it is archived or not shareable");
    }
    User user = (User) connectedUser.getPrincipal();
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot borrow your own book");
    }
    final boolean isAlreadyBorrowed =
        bookTransactionHistoryRepository.isAlreadyBorrowedByUser(book.getId(), user.getId());
    if (isAlreadyBorrowed) {
      throw new OperationNotPermittedException("The requested book is Already borrowed");
    }
    BookTransactionHistory bookTransactionHistory =
        BookTransactionHistory.builder()
            .user(user)
            .book(book)
            .returned(false)
            .returnedApproved(false)
            .build();

    return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
  }

  public Integer returnBorrowBook(Integer bookId, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    BookTransactionHistory history =
        bookTransactionHistoryRepository
            .findByBookIdAndUserId(bookId, user.getId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "BookTransactionHistory with ID : " + bookId + " not found"));

    history.setReturned(true);
    return bookTransactionHistoryRepository.save(history).getId();
  }
}
