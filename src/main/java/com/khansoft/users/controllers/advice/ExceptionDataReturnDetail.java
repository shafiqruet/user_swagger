package com.khansoft.users.controllers.advice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDataReturnDetail {

    @JsonIgnore
    private int code;

    private String message;

    @JsonIgnore
    private String messageSource;

    @JsonIgnore
    private String errorDetails;

}
