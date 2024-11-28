package com.mildo.code;

import lombok.Data;

//익스텐션 업로드 DATA 관리
@Data
public class UploadDTO {
    private String id;
    private String studyCode;
    private String sourceText;
    private String readmeText;
    private String filename;
    private String commitMessage;
    private String dateInfo;
    private String problemId;
    private String level;
}
