package com.mildo.code;

import lombok.Data;

import java.sql.Date;

@Data
public class CommentVO {

    private int commentId;          // 댓글 아이디
    private int codeId;             // 코드 아이디
    private String userId;          // 회원 아이디 ex) #G090
    private String commentContent;  // 댓글 내용
    private Date commentDate;     // 댓글 작성 날짜

    public CommentVO(){}

    public CommentVO(int commentId, int codeId, String userId, String commentContent, Date commentDate) {
        this.commentId = commentId;
        this.codeId = codeId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.commentDate = commentDate;
    }
}
