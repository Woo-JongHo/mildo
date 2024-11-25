package com.mildo.utill;

import java.util.Random;

public class CodeGenerator {

    private static final Random RANDOM = new Random();
    private static final String[] password = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };
    public static String generateRandomCode(int codeLength) {
        //01 배열은 0~9 와 알파벳으로 구성되어있고, 랜덤으로 배열을 뽑아 5자리로 구성됩니다
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int index = RANDOM.nextInt(password.length);
            codeBuilder.append(password[index]);
        }
        return codeBuilder.toString();
    }
    public static String generateRandomCode() {
        return generateRandomCode(5);
    }

    public static String UserId() {     //  @@@@@ 스터디 코드와 동일한 방식의 코드 구성이라면,
                                        // 메서드를 하나로 합치는 것을 고민해보면 좋을 것같음

        //01 배열은 0~9 와 알파벳으로 구성되어있고, 랜덤으로 배열을 뽑아 #4자리로 구성됩니다
        StringBuilder studyCodeBuilder = new StringBuilder("#");
        for (int i = 0; i < 4; i++) {
            int index = RANDOM.nextInt(password.length);
            studyCodeBuilder.append(password[index]);
        }
        return studyCodeBuilder.toString();
    }
}