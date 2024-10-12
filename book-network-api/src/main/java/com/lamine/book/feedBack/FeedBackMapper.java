package com.lamine.book.feedBack;

import com.lamine.book.book.Book;
import org.springframework.stereotype.Service;

@Service
public class FeedBackMapper {
    public FeedBack toFeedBack(FeedBackRequest feedBackRequest) {
        return FeedBack.builder()
                .note(feedBackRequest.note())
                .comment(feedBackRequest.comment())
                .book(Book.builder()
                        .id(feedBackRequest.bookId())
                        .build())
                .build();
    }
}
