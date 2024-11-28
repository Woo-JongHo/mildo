package com.mildo.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    public void dummyCode(String userId) {
        codeRepository.dummyCode(userId);
    }

    public ArrayList<Map<String, String>> getSolvedByDaySelectedMonth(String userId, String month) {
        return codeRepository.getSolvedByDaySelectedMonth(userId, month);
    }


    public void codeUpload(UploadDTO request) throws ParseException {

        String id = request.getId();
        String filename = request.getFilename();
        int filenamelength = filename.length();
        filename = filename.substring(0, filenamelength - 5);
        String sourceText = request.getSourceText();
        String readmeText = request.getReadmeText();
        String dateInfo = request.getDateInfo();
        int problemId = Integer.parseInt(request.getProblemId());
        String level = request.getLevel();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateInfo);

        log.info("UploadRequest processed:");
        log.info("ID: {}", id);
        log.info("Filename (original): {}", request.getFilename());
        log.info("Filename (processed): {}", filename);
        log.info("SourceText: {}", sourceText);
        log.info("ReadmeText: {}", readmeText);
        log.info("DateInfo (string): {}", dateInfo);
        log.info("Parsed Date: {}", date);
        log.info("ProblemID: {}", problemId);
        log.info("Level: {}", level);
    }
}
