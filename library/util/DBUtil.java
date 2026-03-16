package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // DB 접속 정보
    private static final String URL = "jdbc:mariadb://localhost:3306/library_db"; //DB 주소
    private static final String User = "root";
    private static final String PASSWORD = "0124"; // DB에 따라 비밀번호 바꿔야 하나

    // 연결 도와주는 메서드

    public static Connection getConnection() throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return DriverManager.getConnection(URL, User, PASSWORD);
    }

}
