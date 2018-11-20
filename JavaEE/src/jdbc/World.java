package jdbc;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class World {
    public static void main(String[] args) throws Exception {
        String userName = "root";
        String password = "savaleks";
        String connectionUrl = "jdbc:mysql://localhost/consult";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(connectionUrl, userName, password)){
            System.out.println("We'are connected!");
        }
    }
}
