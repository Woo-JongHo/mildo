package com.mildo.study;

import com.mildo.study.Vo.StudyVO;
import com.mildo.utill.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;


    public void create( String userId,String name, String password) {
        Date date = new Date(System.currentTimeMillis()); // 현재 시간
        System.out.println("현재 날짜: " + date);

        // Calendar 객체 생성 및 날짜 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 1년 추가
        calendar.add(Calendar.YEAR, 1);

        // 업데이트된 Date 객체 가져오기
        Date newDate = new Date(calendar.getTimeInMillis());

        String studyCode = CodeGenerator.generateRandomCode();

        // TODO 해당 스터디 코드가 무결성을 해치지 않는지 확인 필요

        StudyVO newStudy = new StudyVO(userId, studyCode, name, password, date, newDate);

        log.info("[Test] Create study : {}", newStudy);

        studyRepository.create(newStudy);
    }

    public List<StudyVO> studyList(String studyCode){
        return studyRepository.studyList(studyCode);
    }

    public int totalMembers(String studyCode){
        return studyRepository.totalMembers(studyCode);
    }


    public List<StudyVO> studyDays(String studyCode){
        return studyRepository.studyDays(studyCode);
    }

    public List<StudyVO> totalrank(String studyCode){
        return studyRepository.totalrank(studyCode);
    }
}
