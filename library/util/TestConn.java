package util;

import java.sql.Connection;

public class TestConn {
    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection()) {
            if (conn != null) {
                System.out.println("🎉 축하합니다! MariaDB 연결에 성공했어요!");
                System.out.println("연결된 정보: " + conn.getMetaData().getURL());
            }
        } catch (Exception e) {
            System.err.println("😭 연결 실패... 아래 에러 메시지를 확인해 보세요.");
            e.printStackTrace();
        }
    }
}