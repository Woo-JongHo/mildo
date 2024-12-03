package com.mildo.study;


import com.mildo.study.Vo.RemainingDaysDTO;
import com.mildo.study.Vo.StudyVO;
import com.mildo.user.UserDBManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public RemainingDaysDTO studyDays(String studyId){
        return StudyDBManger.studyDays(studyId);
    }

    public RemainingDaysDTO studyDaysRe(String studyId){
        return StudyDBManger.studyDaysRe(studyId);
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

    public List<StudyVO> updateStudyName(String studyId, String studyName) {
        List<StudyVO> list = new ArrayList<>();

        int success = StudyDBManger.updateStudyName(studyId, studyName);
        if(success >= 1){
            list = StudyDBManger.studyList(studyId);
            log.info("list = {}", list);
        }

        return list;
    }

    public String updateLeader(String studyId,  String newLeaderId) {
        int success = StudyDBManger.updateLeader(studyId, newLeaderId);
        if(success != 1)
            return "";

        return newLeaderId;
    }

    public boolean deleteStudy(String studyId) {
        int success = StudyDBManger.deleteStudy(studyId);

        return success != 0;
    }

    public StudyVO findStudyNo(int studyNo) {
        return StudyDBManger.findStudyNo(studyNo);
    }

    public int findStudyNextNo() {
        return StudyDBManger.findStudyNextNo();
    }
}
