package com.mildo.code;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CodeRepository {

    public void dummyCode(String userId){
        CodeDBManger.dummyCode(userId);
    }

}
