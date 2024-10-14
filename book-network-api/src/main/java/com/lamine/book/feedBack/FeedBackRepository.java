package com.lamine.book.feedBack;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedBackRepository extends JpaRepository<FeedBack, Integer> {

    @Query("""
        SELECT feedBack
        FROM FeedBack feedBack
        WHERE feedBack.book.id=:bookId
""")
    Page<FeedBack> findAllFeedbackByBookId(Integer bookId, Pageable pageable);
}
