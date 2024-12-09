package com.mildo.code;

import com.mildo.code.Vo.CodeVO;
import com.mildo.code.Vo.CommentVO;
import com.mildo.code.Vo.RecentVO;
import com.mildo.common.Page.PageInfo;
import com.mildo.user.UserDBManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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

    // 최근 활동 가져오기
    public List<RecentVO> getRecent(String studyId){
        return CodeDBManger.getRecent(studyId);
    }

    // 댓글 수정
    public int updateComment(CommentVO comment){
        return CodeDBManger.updateComment(comment);
    }

    // 댓글 삭제
    public int deleteComment(int commentId){
        return CodeDBManger.deleteComment(commentId);
    }

    // 업로드
    @Transactional(rollbackFor = Exception.class)
    public void upload(CodeVO vo) {
        try {
            // code upload
            CodeDBManger.upload(vo);

            // increment USERS Table
            UserDBManger.solvedIncrement(vo.getUserId());

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
