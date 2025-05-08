package com.example.project.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActionRequest {
    private Long bookId;
    private String actionType; // "FAVORITE" หรืออื่นๆในอนาคต

}
