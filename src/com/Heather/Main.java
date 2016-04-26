package com.Heather;

import java.sql.*;
import java.util.*;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cube";

    static final String USER = "Heather";
    static final String PASSWORD = "ashlynn8";
    static Scanner scan=new Scanner(System.in);

    public static void main(String[] args) throws Exception{
	// write your code here
        try {
            Class.forName(JDBC_DRIVER);
        }catch(ClassNotFoundException cnfe){
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);
        }
        Connection connection = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS rubikscube (CUBE_SOLVER VARCHAR (50), SOLVING_TIME DOUBLE, )");
        statement.execute("INSERT INTO rubikscube VALUES ('Cubestormer II robot', 5.270)");
        statement.execute("INSERT INTO rubikscube VALUES ('Fakhri Raihaan (using his feet)', 27.93)");
        statement.execute("INSERT INTO rubikscube VALUES ('Ruxin Liu (age 3)', 99.33)");
        statement.execute("INSERT INTO rubikscube VALUES ('Mats Valk (human record holder)', 6.27)");

        ResultSet results=statement.executeQuery("SELECT * FROM rubikscube");
        while (results.next()){
            String solver=results.getString("cube_solver");
            Double time=results.getDouble("solving_time");
            System.out.println("Solver of Rubik's Cube= "+ solver+ " Time in seconds to solve= "+time);
        }
        System.out.println("Do you want to add a new entry?  Type y for yes.");
        String new_entry=scan.nextLine();
        String name;
        Double timing;
        if (new_entry.equals("y")){
            System.out.println("What is the solver's name?");
            name=scan.nextLine();
            System.out.println("How many seconds did it take?");
            timing=scan.nextDouble();//TODO verify correct input
            statement.execute("INSERT INTO rubikscube VALUES (name, timing)");
        }





        results.close();
        statement.close();
        connection.close();

    }
}
