package com.mildo.study;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StudyRepository {

    public void create(StudyVO study){
        StudyDBManger.create(study);
    }
}
