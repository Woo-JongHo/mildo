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

    public List<StudyVO> studyList(String studyCode){
        return StudyDBManger.studyList(studyCode);
    }

    public int totalMembers(String studyCode){
        return StudyDBManger.totalMembers(studyCode);
    }

    public List<StudyVO> studyDays(String studyCode){
        return StudyDBManger.studyDays(studyCode);
    }

    public List<StudyVO> totalrank(String studyCode){return StudyDBManger.totalrank(studyCode);}

}
