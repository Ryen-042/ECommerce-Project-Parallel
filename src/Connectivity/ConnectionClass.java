/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Tayseer
 */
public class ConnectionClass {
public Connection connection;
    public  Connection getConnection(){


        String dbName="parallel";
        String userName="root";
        String password="2927256";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

        connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return connection;
    }
}
