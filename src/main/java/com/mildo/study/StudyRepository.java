package com.mildo.study;


import com.mildo.study.Vo.StudyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StudyRepository {

    public void create(StudyVO study){
        StudyDBManger.create(study);
    }

    public List<StudyVO> studyList(String studyId){
        return StudyDBManger.studyList(studyId);
    }

    public int totalMembers(String studyId){
        return StudyDBManger.totalMembers(studyId);
    }

    public List<StudyVO> studyDays(String studyId){
        return StudyDBManger.studyDays(studyId);
    }

    public List<StudyVO> totalrank(String studyId){
        return StudyDBManger.totalrank(studyId);
    }

    public String getStartMonth(String studyId){
        return StudyDBManger.getStartMonth(studyId);
    }

    public Object getStudyMemberByMonth(String studyId, String month) {
        return StudyDBManger.getStudyMemberByMonth(studyId, month);
    }

    public Object getStudyMemberIdByMonth(String studyId, String month) {
        return StudyDBManger.getStudyMemberIdByMonth(studyId, month);
    }

    public boolean checkstudyIdPassword(String studyId, String password) {
        return StudyDBManger.checkstudyIdPassword(studyId, password);
    }

    public void enterStudy(String studyId, String password, String userId) {
        StudyDBManger.enterStudy(studyId, password, userId);
    }
}
