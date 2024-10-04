package com.lamine.book.book;

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
                //cover
                .build();
    }
}
