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
import java.time.format.DateTimeFormatter;
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
        // "lv2" -> "2"로 변경 (자료형 유지를 위해 charAt 등과 같은 것을 사용하지 않음)
        String level = request.getLevel();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(dateInfo, formatter);

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

        // TODO: 추가 로직 작성 필요(코드 아이디에 대한 의견 필요)
        // codeLikes의 값이 Y / N 임.
        CodeVO vo = new CodeVO (id, filename, readmeText, sourceText, "N", level, problemId, 0, date);

        codeRepository.upload(vo);

    }

    public List<CommentVO> saveComment(CommentVO comment){
        int res = codeRepository.saveComment(comment);
        PageInfo pi = Pagenation.getPageInfo(codeRepository.commentCount(comment.getCodeId()), 1, 5, 5);
        return codeRepository.CommentList(comment.getCodeId(), pi);
    }

    public List<CodeVO> solvedListSearchTrue(String userId, String title, int currentPage, String category) {
        // 리스트 페이지 별로 주는 메서드
        PageInfo pi = Pagenation.getPageInfo(codeRepository.totalSolved(userId), currentPage, 5, 9);

        // 검색조건이 없고 레벨 순으로 주는 메서드
        if(title == null && "level".equals(category)){
            List<CodeVO> solvedListCategory = codeRepository.solvedListCategory(pi, userId);
            return solvedListCategory;
        }

        // 검색조건이 있고 레벨 순으로 주는 메서드
        if(title != null && "level".equals(category)){
            List<CodeVO> solvedSearchCategory = codeRepository.solvedSearchCategory(pi, userId, title);
            return solvedSearchCategory;
        }

        // 검색조건이 있고 최신 순으로 주는 메서드
        if(title != null){
            List<CodeVO> solvedSerachList = codeRepository.solvedSerachList(pi, userId, title);
            return solvedSerachList;
        }
        List<CodeVO> list = null;
        return list;
    }

    public List<CodeVO> solvedListSearchFail(String userId, int currentPage, String category) {
        // 리스트 페이지 별로 주는 메서드
        PageInfo pi = Pagenation.getPageInfo(codeRepository.totalSolved(userId), currentPage, 5, 9);

        if("level".equals(category)){
            List<CodeVO> solvedListCategory = codeRepository.solvedListCategory(pi, userId);
            return solvedListCategory;
        }

        List<CodeVO> solvedList = codeRepository.solvedList(pi, userId);
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
    public List<CommentVO> deleteComment(int commentId, int codeId){
        codeRepository.deleteComment(commentId);
        PageInfo pi = Pagenation.getPageInfo(codeRepository.commentCount(codeId), 1, 5, 5);
        return codeRepository.CommentList(codeId, pi);
    }

    public List<CodeVO> searchCode(String userId, CodeVO vo, int currentPage, String category) {
        PageInfo pi = Pagenation.getPageInfo(codeRepository.totalSolved(userId), currentPage, 5, 9);

        if("level".equals(category)){
            List<CodeVO> solvedSearchCategory = codeRepository.solvedSearchCategory(pi, userId, vo.getCodeTitle());
            return solvedSearchCategory;
        }
        List<CodeVO> solvedSerachList = codeRepository.solvedSerachList(pi, userId, vo.getCodeTitle());
        return solvedSerachList;
    }

}
