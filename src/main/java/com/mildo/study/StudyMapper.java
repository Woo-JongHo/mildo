package com.mildo.study;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudyMapper {
    void create(StudyVO study);
}
