package com.mildo.code;

import com.mildo.code.Vo.CodeVO;
import com.mildo.code.Vo.CommentVO;
import com.mildo.common.Page.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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

    // 문제 푼 총 수량
    public int totalSolved(String userId){
        return CodeDBManger.totalSolved(userId);
    }

    // 문재 리스트
    public List<CodeVO> solvedList(PageInfo pi, String userId){
        return CodeDBManger.solvedList(pi, userId);
    }

    // codeId로 코드 조회
    public CodeVO detailCode(int codeId, String userId){
        return CodeDBManger.detailCode(codeId, userId);
    }

    // 댓글 총 갯수
    public int commentCount(int codeId){
        return CodeDBManger.commentCount(codeId);
    }

    // 탯글 리스트
    public List<CommentVO> CommentList(int codeId, PageInfo pi){
        return CodeDBManger.CommentList(codeId, pi);
    }
}
