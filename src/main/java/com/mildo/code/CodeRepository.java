package com.mildo.code;

import com.mildo.study.StudyDBManger;
import com.mildo.study.StudyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CodeRepository {

    public void dummyCode(String userId){
        CodeDBManger.dummyCode(userId);
    }

}
