package com.mildo.user.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class TokenVO {

    private String userId;
    private String accessToken;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp expirationTime;

    public TokenVO(){}

    public TokenVO(String userId, String accessToken, Timestamp expirationTime) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.expirationTime = expirationTime;
    }
}
