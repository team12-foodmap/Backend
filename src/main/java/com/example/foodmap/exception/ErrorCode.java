package com.example.foodmap.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    NICKNAME_EMPTY(BAD_REQUEST, "닉네임을 입력해주세요."),
    WRONG_NICKNAME_LENGTH(BAD_REQUEST, "닉네임은 10자 이내로 입력해주세요."),
    ADDRESS_EMPTY(BAD_REQUEST, "관심주소를 입력해주세요."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "로그인 후 이용가능한 서비스입니다."),
    UNAUTHORIZED_UPDATE(UNAUTHORIZED, "수정할 수 있는 권한이 없습니다."),
    UNAUTHORIZED_DELETE(UNAUTHORIZED, "삭제할 수 있는 권한이 없습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    POST_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    RESTAURANT_NOT_FOUND(NOT_FOUND, "해당 음식점이 존재하지 않습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "해당 리뷰가 존재하지 않습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다."),
    DUPLICATE_NICKNAME(CONFLICT, "이미 존재하는 닉네임입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
