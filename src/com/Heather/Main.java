package com.Heather;

import jdk.nashorn.internal.objects.annotations.Where;

import java.sql.*;
import java.util.*;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cube";

    static final String USER = "Heather";
    static final String PASSWORD = "ashlynn8";
    static Scanner scan=new Scanner(System.in);//why didn't it work with one scanner?  I couldn't get it working with less than 4!
    static Scanner scan2=new Scanner(System.in);
    static Scanner scan3=new Scanner(System.in);
    static Scanner scan4=new Scanner(System.in);

    public static void main(String[] args) {
	// write your code here
        try {
            Class.forName(JDBC_DRIVER);

        }catch(ClassNotFoundException cnfe){
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);
        }
        Statement statement=null;
        Connection connect=null;

        try {
            connect = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
            System.out.println("is there really a connection? "+connect);//yes there is
            statement = connect.createStatement();
            System.out.println("is there really a statemnt? "+statement);//yes there is

            String newTable="CREATE TABLE IF NOT EXISTS rubikscube (Solver varchar (50), Solving_time double)";
            statement.executeUpdate(newTable);
            System.out.println("I made a rubikscube table");//doesn't get here

            String addMe="INSERT INTO rubikscube VALUES ('Cubestormer II robot', 5.270)";
            statement.executeUpdate(addMe);
            statement.executeUpdate("INSERT INTO rubikscube VALUES ('Fakhri Raihaan (using his feet)', 27.93)");
            statement.executeUpdate("INSERT INTO rubikscube VALUES ('Ruxin Liu (age 3)', 99.33)");
            statement.executeUpdate("INSERT INTO rubikscube VALUES ('Mats Valk (human record holder)', 6.27)");

            //adding a new entry to table
            System.out.println("Do you want to add a new entry?  Type y for yes and n for no.");
            String new_entry = scan.nextLine();
            boolean entry=false;
            if (new_entry.equalsIgnoreCase("y")){//does the person want to add an entry?
                entry=true;
            }
            String solveName;
            Double timing;

            while(entry){
                System.out.println("What is the solver's name?");
                solveName = scan2.nextLine();
                System.out.println("How many seconds did it take?");
                timing = scan.nextDouble();//TODO verify correct input
                statement.executeUpdate("INSERT INTO rubikscube VALUES ('"+solveName+"', '"+timing+"')");//not adding entry
                System.out.println("Do you want to make another entry?");
                String con=scan2.nextLine();
                if (!con.equalsIgnoreCase("y")){//set entry false to exit loop
                    entry=false;
                }
            }
            //changing an entry for the table
            System.out.println("Do you want to change an entry?  Type y for yes and n for no.");
            String change = scan3.nextLine();
            boolean changeIt=false;
            if (change.equalsIgnoreCase("y")){//does the person want to change an entry?
                changeIt=true;
            }
            String solverName;
            Double newTiming;
            ResultSet results = null;
            while(changeIt) {
                results = statement.executeQuery("SELECT * FROM rubikscube");
                System.out.println("What is the name of the person who's record needs to be updated?");
                solverName = scan4.nextLine();
                System.out.println("How many seconds did it take?");
                newTiming = scan3.nextDouble();//TODO verify correct input
                statement.executeUpdate("UPDATE rubikscube SET Solving_time='"+newTiming+"' WHERE Solver='"+solverName+"'");
                System.out.println("do you want to change another entry?");//it's not getting here.
                String chan=scan4.nextLine();
                if (!chan.equalsIgnoreCase("y")){
                    changeIt=false;
                }
            }

        }catch(SQLException se){
            se.printStackTrace();
        }finally{
            try{
                if (statement!=null){
                    statement.close();

                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            try{
                if (connect !=null){
                    connect.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Program finished");

    }
}
