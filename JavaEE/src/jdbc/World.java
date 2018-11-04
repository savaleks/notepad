package jdbc;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class World {
    public static void main(String[] args){
        String userName = "root";
        String password = "nbuser";
        String connectionUrl = "jdbc:mysql://localhost/world?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(connectionUrl, userName, password)){
            System.out.println("We'are connected!");
        }
    }
}
