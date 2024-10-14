package com.lamine.book.feedBack;

import com.lamine.book.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public FeedBackResponse toFeedBackResponse(FeedBack feedBack, Integer userId) {

        return FeedBackResponse.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedBack(Objects.equals(feedBack.getCreatedBy(), userId))
                .build();
    }
}
