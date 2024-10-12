package com.lamine.book.feedBack;
import com.lamine.book.book.Book;
import com.lamine.book.book.BookRepository;
import com.lamine.book.exception.OperationNotPermittedException;
import com.lamine.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final BookRepository bookRepository;
    private final FeedBackMapper feedBackMapper;
    private final FeedBackRepository feedBackRepository;

    public Integer saveFeedback(FeedBackRequest feedBackRequest, Authentication connectUser) {
        Book book = bookRepository.findById(feedBackRequest.bookId()).orElseThrow(
                ()-> new EntityNotFoundException("Book not found")
        );

        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot give a feedback for an archived or not shareable book");
        }

        User user = (User) connectUser.getPrincipal();

        if (Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot give a feedback for your own book");
        }

        FeedBack feedBack= feedBackMapper.toFeedBack(feedBackRequest);
        return feedBackRepository.save(feedBack).getId();
    }

}
