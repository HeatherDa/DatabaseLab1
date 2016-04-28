package com.Heather;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import jdk.nashorn.internal.objects.annotations.Where;

import javax.naming.directory.SearchResult;
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
        PreparedStatement psInsert=null;
        PreparedStatement psChange=null;
        PreparedStatement searchSolver=null;

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


            String newTable="CREATE TABLE IF NOT EXISTS rubikscube (Solver varchar (50)UNIQUE , Solving_time double)";
            statement.executeUpdate(newTable);
            System.out.println("I made a rubikscube table");//doesn't get here
            String prepStatementInsert="INSERT INTO rubikscube VALUES(?,?)";
            psInsert=connect.prepareStatement(prepStatementInsert);
            String seSolver="SELECT * FROM rubikscube WHERE Solver = ?";
            searchSolver= connect.prepareStatement(seSolver);


            try {
                HashMap<String, Double> testData=new HashMap<>();
                testData.put("Cubestormer II robot",5.270);
                testData.put("Fakhri Raihaan (using his feet)", 27.93);
                testData.put("Ruxin Liu (age 3)", 99.33);
                testData.put("Mats Valk (human record holder)", 6.27);
                for(String s:testData.keySet()) {//s is each name in test data
                    searchSolver.setString(1, s);
                    ResultSet searchR = searchSolver.executeQuery();//look for an entry with this name
                    int count=0;
                    while (searchR.next()){
                        count++;
                    }
                    if (count==0) {//if there is no entry, add it
                        psInsert.setString(1, s);
                        psInsert.setDouble(2, testData.get(s));
                        psInsert.executeUpdate();
                    }
                }
            }catch(MySQLIntegrityConstraintViolationException ex){
                System.out.print("");
            }



            //adding a new entry to table

            System.out.println("Do you want to add a new entry?  Type y for yes and n for no.");
            String new_entry = scan.nextLine();
            boolean entry = false;
            if (new_entry.equalsIgnoreCase("y")) {//does the person want to add an entry?
                entry = true;
            }
            String solveName;
            Double timing;

            while (entry) {
                System.out.println("What is the solver's name?");
                solveName = scan2.nextLine();
                System.out.println("How many seconds did it take?");
                timing = scan.nextDouble();//TODO verify correct input
                searchSolver.setString(1, solveName);
                ResultSet searchR = searchSolver.executeQuery();//look for an entry with this name
                int resultsCounter=0;
                while(searchR.next()){
                    resultsCounter++;
                }
                if (resultsCounter==0) {//if there is no entry, add it
                    psInsert.setString(1, solveName);
                    psInsert.setDouble(2, timing);
                    psInsert.executeUpdate();
                }else{
                    System.out.println("That person is already in the database.");
                }
                System.out.println("Do you want to make another entry?");
                String con = scan2.nextLine();
                if (!con.equalsIgnoreCase("y")) {//set entry false to exit loop
                    entry = false;
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
            String prepStatementChange="UPDATE rubikscube SET Solving_time=(?) WHERE Solver=(?)";
            psChange=connect.prepareStatement(prepStatementChange);
            while(changeIt) {
                results = statement.executeQuery("SELECT * FROM rubikscube");
                System.out.println("What is the name of the person who's record needs to be updated?");
                solverName = scan4.nextLine();
                System.out.println("How many seconds did it take?");
                newTiming = scan3.nextDouble();//TODO verify correct input
                psChange.setDouble(1,newTiming);
                psChange.setString(2,solverName);
                psChange.executeUpdate();
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
                if (psInsert !=null){
                    psInsert.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
            try{
                if (psChange !=null) {
                    psChange.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
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
