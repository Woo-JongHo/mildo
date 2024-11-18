package com.swyp.mildo.code;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;
}
