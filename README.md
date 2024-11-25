# SWYP7


# 의존성
------
- Spring Boot Starter Web
- MyBatis Spring Boot Starter
- PostgreSQL Driver
- Lombok (Optional)
- Spring Boot DevTools
- Spring Boot Starter Test
- MyBatis Spring Boot Starter Test


# ERD 
![image](https://github.com/user-attachments/assets/df030931-b491-4870-8774-3af445b75081)

# 밀도 커밋 컨벤션
1. 제목과 본문을 한 줄 띄워 분리하기

    ex) 
    ```
    [STYLE] sqlMapConfig.xml 수정 및 db.properties 위치 변경
    
    resource 주소 변경
    classpath(resources 패키지)를 기준으로 하기에 경로에 맞게 수정
    ```
2. 제목은 한글 기준 30자 이내
3. 설명 일관되게 통일
   >ex) ~기능 구현, ~작성, ~출력, ~삭제, ~생성, ~수정 등
4. TYPE은 대문자로 통일    
   >ex) [FEAT], [FIX], [DOCS], [STYLE], ...

### TYPE 종류
> **feat**: 새로운 기능 (기능 구현)
> 
>**fix**: 버그 수정
> 
>**docs**: documentation 변경
> 
>**style**: 코드 의미에 영향을 주지 않는, 코드가 아닌 스타일에 관련된 변경사항(포맷, 공백, 빼먹은 세미콜론, 함수 이름 변경, 줄간격, 파일 이름, 의미없는 주석 삭제)
>
>**refactor**: 리팩토링에 대한 커밋(버그를 수정하지 않고 기능을 추가하지 않는 코드 변경)
>
>**test**: 누락된 테스트 추가 또는 기존 테스트 수정
>
>**chore**: 패키지 매니저 설정할 경우, 코드 수정 없이 설정을 변경 (eslint, prettier... 패키지 설정)

# 주석 키워드
1. `TODO`
   - 할일이 남아 있음을 표시
   - 일반적으로 미완성 기능이나 추가 작업이 필요한 부분에 사용
2. `FIXME`
   - 코드에 수정이 필요함을 나타냄
   - 오류를 수정하거나 비효율적인 코드를 개선해야 할 경우 사용
3. `NOTE`
   - 특정 코드에 대한 주석이나 설명을 남길 때 사용
4. `HACK`
   - 임시로 작성한 해결책임을 표시
   - 향후 리팩토링이 필요하거나 개선해야 할 부분임을 나타냄
5. `XXX`
   - 주의가 필요한 코드를 표시
   - 위험하거나 논란의 여지가 있는 로직에 대해 경고하거나 검토가 필요한 경우 사용


