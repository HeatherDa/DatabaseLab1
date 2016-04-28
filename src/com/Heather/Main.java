package com.Heather;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
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
            statement = connect.createStatement();
            //make new table
            String newTable="CREATE TABLE IF NOT EXISTS rubikscube (Solver varchar (50)UNIQUE , Solving_time double)";
            statement.executeUpdate(newTable);
            //get prepared statement for insert ready
            String prepStatementInsert="INSERT INTO rubikscube VALUES(?,?)";
            psInsert=connect.prepareStatement(prepStatementInsert);
            //get prepared statement for search ready
            String seSolver="SELECT * FROM rubikscube WHERE Solver = ?";
            searchSolver= connect.prepareStatement(seSolver);

            //store test data
            HashMap<String, Double> testData=new HashMap<>();
            testData.put("Cubestormer II robot",5.270);
            testData.put("Fakhri Raihaan (using his feet)", 27.93);
            testData.put("Ruxin Liu (age 3)", 99.33);
            testData.put("Mats Valk (human record holder)", 6.27);

            //add test data to table
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
                String b="How many seconds did it take?";
                timing = testDoub(b);
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
            String prepStatementChange="UPDATE rubikscube SET Solving_time=(?) WHERE Solver=(?)";
            psChange=connect.prepareStatement(prepStatementChange);
            while(changeIt) {
                System.out.println("What is the name of the person who's record needs to be updated?");
                solverName = scan4.nextLine();
                String a="How many seconds did it take?";
                newTiming = testDoub(a);
                psChange.setDouble(1,newTiming);
                psChange.setString(2,solverName);
                psChange.executeUpdate();
                System.out.println("do you want to change another entry?");
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
                if(searchSolver !=null) {
                    searchSolver.close();
                }
            }catch (SQLException se){
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
    public static double testDoub(String printPrompt){
        Scanner scanDoub=new Scanner (System.in);
        boolean isdoub;
        double next=0.0;
        do {//test for correct input
            System.out.println(printPrompt);


            if (scanDoub.hasNextDouble()) {
                next = scanDoub.nextDouble();
                isdoub = true;
            } else {
                System.out.println("Not a valid choice.  Please type a double.");
                isdoub = false;
                scanDoub.next();
            }
        } while (!(isdoub));
        return next;
    }
}
