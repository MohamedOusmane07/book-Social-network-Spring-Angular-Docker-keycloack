package com.lamine.book.book;

import com.lamine.book.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

  private final BookService bookService;

  @PostMapping
  public ResponseEntity<Integer> saveBook(
      @Valid @RequestBody BookRequest bookRequest, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.saveBook(bookRequest, connectedUser));
  }

  @GetMapping("{bookId}")
  public ResponseEntity<BookResponse> findBookById(@PathVariable Integer bookId) {
    return ResponseEntity.ok(bookService.findBookById(bookId));
  }

  @GetMapping
  public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
  }

  @GetMapping("/owner")
  public ResponseEntity<PageResponse<BookResponse>> findBooksByOwner(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    return ResponseEntity.ok(bookService.findBooksByOwner(page, size, connectedUser));
  }

  @GetMapping("/borrowed")
  public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
  }

  // findAllReturnedBooks
  @GetMapping("/returned")
  public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    return ResponseEntity.ofNullable(bookService.findAllReturnedBooks(page, size, connectedUser));
  }

  @PatchMapping("/shareable/{book-Id}")
  public ResponseEntity<Integer> updateShareableStatus(
      @PathVariable("book-Id") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.updateShareableStatus(bookId, connectedUser));
  }

  @PatchMapping("/archived/{book-id}")
  public ResponseEntity<Integer> updateArchivedStatus(
      @PathVariable("book-id") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.updateArchivedStatus(bookId, connectedUser));
  }

  @PostMapping("/borrow/{book-id}")
  public ResponseEntity<Integer> borrowBook(
      @PathVariable("book-id") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
  }

  @PatchMapping("/borrow/return/{book-id}")
  public ResponseEntity<Integer> returnBook(
      @PathVariable("book-id") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.returnBorrowBook(bookId, connectedUser));
  }

  @PatchMapping("/borrow/return/approve/{book-id}")
  public ResponseEntity<Integer> returnApproveBook(
      @PathVariable("book-id") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.returnApprovedBook(bookId, connectedUser));
  }

  @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadBookCoverPicture(
      @PathVariable("book-id") Integer bookId,
      @Parameter() @RequestPart("file") MultipartFile file,
      Authentication connectedUser) {
    bookService.uploadBookCoverPicture(file, connectedUser, bookId);
    return ResponseEntity.accepted().build();
  }
}
