package com.mildo.code;

import com.mildo.code.Vo.CodeVO;
import com.mildo.code.Vo.CommentVO;
import com.mildo.code.Vo.RecentVO;
import com.mildo.common.Page.PageInfo;
import com.mildo.common.Page.Pagenation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    public void dummyCode(String userId) {
        codeRepository.dummyCode(userId);
    }

    public ArrayList<Map<String, String>> getSolvedByDaySelectedMonth(String userId, String month) {
        return codeRepository.getSolvedByDaySelectedMonth(userId, month);
    }


    public void codeUpload(UploadDTO request) throws ParseException {

        String id = request.getId();
        String filename = request.getFilename();
        int filenamelength = filename.length();
        filename = filename.substring(0, filenamelength - 5);
        String sourceText = request.getSourceText();
        String readmeText = request.getReadmeText();
        String dateInfo = request.getDateInfo();
        int problemId = Integer.parseInt(request.getProblemId());
        String level = request.getLevel();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateInfo);

        log.info("UploadRequest processed:");
        log.info("ID: {}", id);
        log.info("Filename (original): {}", request.getFilename());
        log.info("Filename (processed): {}", filename);
        log.info("SourceText: {}", sourceText);
        log.info("ReadmeText: {}", readmeText);
        log.info("DateInfo (string): {}", dateInfo);
        log.info("Parsed Date: {}", date);
        log.info("ProblemID: {}", problemId);
        log.info("Level: {}", level);
    }

    public int saveComment(CommentVO comment){
        return codeRepository.saveComment(comment);
    }

    public List<CodeVO> solvedList(String userId) {
        // 문제 푼 총 수량
        int totalSolved = codeRepository.totalSolved(userId);
        log.info("totalSolved = {}", totalSolved);

        // 리스트 페이지 별로 주는 메서드
        PageInfo pi = Pagenation.getPageInfo(totalSolved, 1, 5, 9);
        log.info("pi = {}", pi);

        // 문재 리스트
        List<CodeVO> solvedList = codeRepository.solvedList(pi, userId);
        log.info("solvedList = {}", solvedList);

        return solvedList;
    }

    // codeId로 코드 조회
    public CodeVO detailCode(int codeId, String userId){
        return codeRepository.detailCode(codeId, userId);
    }

    // 댓글 리스트
    public List<CommentVO> CommentList(int codeId, int currentPage){
        PageInfo pi = Pagenation.getPageInfo(codeRepository.commentCount(codeId), currentPage, 5, 5);

        return codeRepository.CommentList(codeId, pi);
    }

    // 최근 활동 가져오기
    public List<RecentVO> getRecent(String studyId){
        return codeRepository.getRecent(studyId);
    }

    // 댓글 수정
    public List<CommentVO> updateComment(CommentVO comment){
        codeRepository.updateComment(comment);
        PageInfo pi = Pagenation.getPageInfo(codeRepository.commentCount(comment.getCodeId()), 1, 5, 5);
        return codeRepository.CommentList(comment.getCodeId(), pi);
    }

    // 댓글 삭제
    public List<CommentVO> deleteComment(int commentId, CommentVO comment){
        codeRepository.deleteComment(commentId);
        PageInfo pi = Pagenation.getPageInfo(codeRepository.commentCount(comment.getCodeId()), 1, 5, 5);
        return codeRepository.CommentList(comment.getCodeId(), pi);
    }

}
