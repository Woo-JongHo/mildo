package com.mildo.code;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CodeRepository {

    public void dummyCode(String userId){
        CodeDBManger.dummyCode(userId);
    }

    public ArrayList<Map<String, String>> getSolvedByDaySelectedMonth(String userId, String month) {
        return CodeDBManger.getSolvedByDaySelectedMonth(userId, month);
    }

    public int saveComment(CommentVO comment){
        return CodeDBManger.saveComment(comment);
    }

}
