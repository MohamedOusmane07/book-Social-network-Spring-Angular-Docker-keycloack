package com.lamine.book.history;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookTransactionHistoryRepository
    extends JpaRepository<BookTransactionHistory, Long> {

  @Query(
      """
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id=:userdId
""")
  Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

  // Find all borrowed books for owner of books
  @Query(
      """
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id=:userdId
""")
  Page<BookTransactionHistory> findAllReturnedBooks(int page, Integer id);

  @Query(
      """
    SELECT
    (count(*) > 0) AS isAlreadyBorrowed
    FROM BookTransactionHistory history
    WHERE history.user.id=:userdId
    AND history.book.id=:bookId
    AND history.returnedApproved=false
""")
  boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

  @Query(
      """
    SELECT history
    FROM BookTransactionHistory history
    where history.user.id=:userdId
    AND history.book.id=:bookId
    AND history.returned=false
""")
  Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

  @Query(
      """
    SELECT bookTransaction
    FROM BookTransactionHistory bookTransaction
    WHERE bookTransaction.book.id=:bookId
    AND bookTransaction.book.owner.id=:ownerId
    AND bookTransaction.returned=true
    AND bookTransaction.returnedApproved=false
""")
  Optional<BookTransactionHistory> findByBookIdAndOwner(Integer bookId, Integer ownerId);
}
