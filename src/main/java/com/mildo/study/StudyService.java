package com.mildo.study;

import com.mildo.study.Vo.StudyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;

    LocalDate currentDate = LocalDate.now();

    public void create(String name, String password) {
        Date date = new Date(System.currentTimeMillis()); // 현재 시간
        System.out.println("현재 날짜: " + date);

        // Calendar 객체 생성 및 날짜 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 1년 추가
        calendar.add(Calendar.YEAR, 1);

        // 업데이트된 Date 객체 가져오기
        Date newDate = new Date(calendar.getTimeInMillis());

        StudyVO newStudy = new StudyVO("#Q1W2", "#E3R4", name, password, date, newDate);

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


    //Study의 시작일(YYYY-MM) 현재 날짜까지의 리스트를 뽑는 메소드
    public List<String> studyMonthList(String studyCode) {

        String study_start = studyRepository.getStartMonth(studyCode);
        log.info(study_start + "start_month 찍히는것확인");

        int start_years = Integer.parseInt(study_start.substring(0,4));
        int start_month = Integer.parseInt(study_start.substring(5,7));
        log.info("start_years " + start_years);
        log.info("start_month " + start_month);

        int current_years = currentDate.getYear();
        int current_month = currentDate.getMonthValue();
        log.info("current_years" + current_years);
        log.info("current_month" + current_month);


        int yearDiff = current_years - start_years;
        int monthDiff;

        log.info(yearDiff + "----------yearDiff");

        if (yearDiff == 0)
            monthDiff = current_month - start_month;
        else
            monthDiff = (yearDiff * 12) + (current_month - start_month);


        List<String> monthList = new ArrayList<>();
        for (int i = monthDiff; i > 0; i--) {
            String[] parts = study_start.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            month = month + 1;
            if (month > 12) {
                year++;
                month = 1;
            }

            String monthString = String.format("%02d", month);
            String yearString = String.valueOf(year);

            study_start = yearString + "-" + monthString;
            monthList.add(study_start);
            System.out.println("study_start가 추가가 되었는가" + study_start);

        }

        return monthList;
    }


    //YYYY-MM 형식으로 String을 받음
    public int DayCheck(String month){
        YearMonth yearMonth = YearMonth.parse(month);
        int daysInMonth = yearMonth.lengthOfMonth();

        return daysInMonth;
    }

    private int getLastDayOfMonth(int year, int month){
        return java.time.YearMonth.of(year, month).lengthOfMonth();
    }

        //month를 돌면서 member를 가지고옵니다
        //member들이 각 푼 문제를 가지고 옵니다


    //밀도심기 로직
    public void Mildo(String studyCode){

        Map<String, Map<String,List<String>>> mildoList = new LinkedHashMap<>();

        List<String> monthData = studyMonthList(studyCode);
        Map<String, Integer> dayData = new LinkedHashMap<>();

        for( String month : monthData){
            List<String> memberName = (List<String>) studyRepository.getStudyMemberByMonth(studyCode,month);

            int countMember = memberName.size();
            System.out.println("MontMember : " + countMember + monthData + ": Month");

            Map<String, List<String>> memberData = new LinkedHashMap<>();
            mildoList.put(month,memberData);

            dayData.put(month, DayCheck(month));

        }
    }
}
