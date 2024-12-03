package com.mildo.study;

import com.mildo.code.CodeService;
import com.mildo.study.Vo.RemainingDaysDTO;
import com.mildo.study.Vo.StudyVO;
import com.mildo.user.UserRepository;
import com.mildo.utills.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import com.mildo.study.Vo.StudyVO;
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
    private final UserRepository userRepository;
    private final CodeService codeService;
    private static int studyNextNo = 1;

    LocalDate currentDate = LocalDate.now();

    public void create(String userId, String name, String password) {

        StudyVO studyVo = findStudyNo(studyNextNo);

        // 스터디 아이디가 무결성을 유지하는지 검증
        while (studyVo.getStudyName() != null){
            studyNextNo = findStudyNextNo();
            studyVo = findStudyNo(studyNextNo);
        }

        log.info("Study No: {}", studyNextNo);

        Date date = new Date(System.currentTimeMillis()); // 현재 시간
        log.info("현재 날짜: {}", date);

        Date newDate = addOneYear(date);

        studyVo.setStudyName(name);
        studyVo.setStudyPassword(password);
        studyVo.setStudyStart(date);
        studyVo.setStudyEnd(newDate);

        studyRepository.create(studyVo);

        // 유저 Leader 및 studyId값 변경
        userRepository.createStudy(userId, studyVo.getStudyId(), date);
        log.info("Created Study: {}, Leader : {}", studyVo, userId);
        studyNextNo++;
    }

public StudyVO findStudyNo(int studyNo) {
    return studyRepository.findStudyNo(studyNo);
}

public int findStudyNextNo() {
    return studyRepository.findStudyNextNo();
}


    private static Date addOneYear(Date date) {
        // Calendar 객체 생성 및 날짜 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 1년 추가
        calendar.add(Calendar.YEAR, 1);

        // 업데이트된 Date 객체 가져오기
        Date newDate = new Date(calendar.getTimeInMillis());
        return newDate;
    }

    public List<StudyVO> studyList(String studyId){
        return studyRepository.studyList(studyId);
    }

    public int totalMembers(String studyId){
        return studyRepository.totalMembers(studyId);
    }


    public RemainingDaysDTO  studyDays(String studyId){
        RemainingDaysDTO el = studyRepository.studyDays(studyId);
        RemainingDaysDTO re = studyRepository.studyDaysRe(studyId);
        log.info("el = {}", el);
        log.info("re = {}", re);

        RemainingDaysDTO result = new RemainingDaysDTO();
        result.setElapsedDays(el.getElapsedDays());
        result.setRemainingDays(re.getRemainingDays());

        return result;
    }

    public List<StudyVO> totalrank(String studyId){
        return studyRepository.totalrank(studyId);
    }

    //Study의 시작일(YYYY-MM) 현재 날짜까지의 리스트를 뽑는 메소드
    public List<String> studyMonthList(String studyId) {

        String study_start = studyRepository.getStartMonth(studyId);
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

    //날짜 입력란. 이번달의 일수를 확인
    public int DayCheck(){
        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();

        return daysInMonth;
    }


    private int getLastDayOfMonth(int year, int month){
        return java.time.YearMonth.of(year, month).lengthOfMonth();
    }


    //밀도심기 로직
    public Map<String, Map<String,List<String>>> Mildo(String studyId){

        Map<String, Map<String,List<String>>> mildoList = new LinkedHashMap<>();

        List<String> monthData = studyMonthList(studyId);
        Map<String, Integer> dayData = new LinkedHashMap<>();

        for( String month : monthData){
            List<String> memberID = (List<String>) studyRepository.getStudyMemberIdByMonth(studyId, month);
            List<String> memberName = (List<String>) studyRepository.getStudyMemberByMonth(studyId,month);

            int countMember = memberName.size();
            System.out.println("MonthData : " + countMember + monthData + ": Month");

            Map<String, List<String>> memberData = new LinkedHashMap<>();
            mildoList.put(month,memberData);

            dayData.put(month, DayCheck(month));
            for (int i = 0; i < countMember; i++) {

                ArrayList<Map<String, String>> solvedList;
                List<String> solvedDataTypeList = new ArrayList<>();
                solvedList = codeService.getSolvedByDaySelectedMonth(memberID.get(i), month);
                int[] MonthDay = new int[DayCheck(month)];

                //리스트를 일단 가져옵니다 아예없을수도있음.
                for (Map<String, String> map : solvedList) {
                    System.out.println("Map contents: " + map);

                    String dataDay = map.get("c_date");
                    System.out.println(dataDay + "dateDay");

                    int day = Integer.parseInt(dataDay.substring(8, 10)); // 일(day)을 가져와야 하므로 8, 10 인덱스 사용
                    System.out.println(day + "day----------");

                    String solved = String.valueOf(map.get("solved"));
                    System.out.println(solved + "solved------------");
                    MonthDay[day - 1] += Integer.parseInt(solved); // 합산하기 위해 += 사용
                }

                //데이터를 내 방식대로 바꾸기위한 배열을 선언하고
                for (int j = 0; j < DayCheck(month); j++) {
                    switch (MonthDay[j]) {
                        case 0:
                            solvedDataTypeList.add("solved-0");
                            break;
                        case 1:
                            solvedDataTypeList.add("solved-1");
                            break;
                        case 2:
                            solvedDataTypeList.add("solved-2");
                            break;
                        case 3:
                            solvedDataTypeList.add("solved-3");
                            break;
                        default:
                            solvedDataTypeList.add("solved-4");
                            break;
                    }
                }
                memberData.put(memberName.get(i),solvedDataTypeList);
            }
            mildoList.put(month,memberData);
        }// month
        return mildoList;
    }

    public boolean checkstudyIdPassword(String studyId, String password) {
        return studyRepository.checkstudyIdPassword(studyId,password);
    }

    public List<StudyVO> updateStudyName(String studyId, String studyName){

        return studyRepository.updateStudyName(studyId, studyName);
    }

    public String updateLeader(String studyId, String newLeaderId) {
        return studyRepository.updateLeader(studyId, newLeaderId);
    }

    public boolean deleteStudy(String studyId) {
        return studyRepository.deleteStudy(studyId);
    }
}
