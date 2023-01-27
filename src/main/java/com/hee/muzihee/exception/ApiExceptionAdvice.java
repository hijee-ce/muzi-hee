package com.hee.muzihee.exception;

import com.hee.muzihee.exception.dto.ApiExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
/* <지히>
* @ControllerAdvice + @ResponseBody
* @ControllerAdvice와 동일한 역할을 수행(단순 예외처리)하고, 추가적으로 @ResponseBody를 통해 객체 리턴 가능
* => 응답으로 객체를 리턴해야 한다면 @RestControllerAdvice를 적용
* */
public class ApiExceptionAdvice {

    // <지히> @ExceptionHandler : 특정 예외 클래스를 지정해주면 해당 예외가 발생했을 때 메서드에 정의한 로직으로 처리 가능

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ApiExceptionDto> exceptionHandler(LoginFailedException e) {
        //e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiExceptionDto(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(DuplicatedUsernameException.class)
    public ResponseEntity<ApiExceptionDto> exceptionHandler(DuplicatedUsernameException e) {
        //e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiExceptionDto(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiExceptionDto> exceptionHandler(TokenException e) {
        //e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiExceptionDto(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

}