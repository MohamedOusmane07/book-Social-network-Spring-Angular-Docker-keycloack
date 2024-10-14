package com.lamine.book.feedBack;


import com.lamine.book.common.PageResponse;
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

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> findAllFeedBackByBookId(
            @PathVariable(name = "book-id") Integer bookId,
            @RequestParam(name = "page" ,defaultValue = "0", required=false) int page,
            @RequestParam(name = "size" ,defaultValue = "10", required=false) int size,
            Authentication connectUser
    ){
        return ResponseEntity.ok(feedBackService.findAllFeedBackByBookId(bookId,page,size,connectUser));
    }



}
