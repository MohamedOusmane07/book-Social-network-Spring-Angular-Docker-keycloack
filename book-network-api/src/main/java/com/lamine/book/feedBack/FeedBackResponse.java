package com.lamine.book.feedBack;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedBackResponse {

    private Double note;
    private String comment;
    private boolean ownFeedBack;
}
