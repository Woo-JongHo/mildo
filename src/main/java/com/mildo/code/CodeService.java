package com.mildo.code;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    public void dummyCode(String userId) {
        codeRepository.dummyCode(userId);
    }
}
