package com.mildo.code;

import com.mildo.common.Page.PageInfo;
import com.mildo.db.DBManger;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeDBManger extends DBManger {

    public static int dummyCode(String userId) {
        int re = -1;
        SqlSession session = sqlSessionFactory.openSession();
        re = session.insert("code.dummyCode", userId);
        session.commit();
        session.close();

        return re;
    }

    public static ArrayList<Map<String, String>> getSolvedByDaySelectedMonth(String userId, String month) {
        ArrayList<Map<String, String>> results = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            params.put("month", month);

            List<Map<String, String>> rows = session.selectList("code.getSolvedByDaySelectedMonth", params);
            session.close();
            for (Map<String, String> row : rows) {
                results.add(row);
            }
        } finally {
            session.close();
        }

        System.out.println("result값 보기 " + results);

        return results;
    }

    public static int saveComment(CommentVO comment) {
        SqlSession session = sqlSessionFactory.openSession();
        int re = session.insert("code.saveComment", comment);
        session.commit();
        session.close();

        return re;
    }

    // 문제 푼 총 수량
    public static int totalSolved(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        int totalSolved = session.selectOne("code.totalSolved", userId);
        session.commit();
        session.close();
        return totalSolved;
    }

    // 문재 리스트
    public static List<CodeVO> solvedList(PageInfo pi, String userId) {
        SqlSession session = sqlSessionFactory.openSession();

        int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
        int limit = pi.getBoardLimit();
        RowBounds rowBounds = new RowBounds(offset, limit);

        List<CodeVO> solvedList = session.selectList("code.solvedList", userId, rowBounds);

        session.commit();
        session.close();
        return solvedList;
    }
}
