package com.lamine.book.feedBack;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feedback")
@RequiredArgsConstructor
@Tag(name="FeedBack")
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping
    public ResponseEntity<?> feedBack(
            @Valid @RequestBody FeedBackRequest feedBackRequest,
            Authentication connectUser
    ){
        return ResponseEntity.ok(feedBackService.saveFeedback(feedBackRequest,connectUser));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<List<FeedBackResponse>> getFeedBackByBook(
            @PathVariable(name = "book-id") Integer bookId,
            Authentication connectUser
    ){
        return ResponseEntity.ok(feedBackService.getFeedBackByBook(bookId,connectUser));
    }



}
