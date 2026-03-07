package dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String url = "jdbc:mysql://localhost:3306/lib_db";
    private static final String dbuser = "root";
    private static final String password = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC 드라이버 로드 실패 : " + e.getMessage());
        }
    }

    // 기본 연결 메서드 (기존 유지)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, dbuser, password);
    }

    // URL 직접 입력 메서드 (기존 유지)
    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url, dbuser, password);
    }

    // 연결 성공 시 true, 실패 시 false를 반환하여 프로그램 종료를 막는 용도
    public static boolean isConnected() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}