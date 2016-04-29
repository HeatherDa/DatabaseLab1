package com.Heather;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.*;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cube";

    static final String USER = "root";
    static final String PASSWORD = "itecitec";
    static Scanner scan=new Scanner(System.in);//why didn't it work with one scanner?  I couldn't get it working with less than 4!
    static Scanner scan2=new Scanner(System.in);
    static Scanner scan3=new Scanner(System.in);
    static Scanner scan4=new Scanner(System.in);
    static ResultSet rs = null;
    static String tableName="rubikscube";
    static RubiksDataModel rubiksDataModel;
    static PreparedStatement psInsert=null;
    static PreparedStatement psChange=null;
    static PreparedStatement searchSolver=null;
    static PreparedStatement psDelete=null;
    static Statement statement=null;
    static Connection connect=null;
    static HashMap<String, Double> testData;


    public static void main(String[] args) {
        // write your code here


        /*try {
            Class.forName(JDBC_DRIVER);

        }catch(ClassNotFoundException cnfe){
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);
        }*/


        try {
            setup();

            //connect = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
            //statement = connect.createStatement();
            //make new table
            //String newTable = "CREATE TABLE IF NOT EXISTS '" + tableName + "' (Solver varchar (50)UNIQUE , Solving_time double)";
            //statement.executeUpdate(newTable);
            //set prepared statement for insert
            String prepStatementInsert = "INSERT INTO rubikscube VALUES(?,?)";
            psInsert = connect.prepareStatement(prepStatementInsert);
            //set prepared statement for search
            String seSolver = "SELECT * FROM rubikscube WHERE Solver = ?";
            searchSolver = connect.prepareStatement(seSolver);
            //set prepared statement for updating current entry
            String prepStatementChange = "UPDATE rubikscube SET Solving_time = ? WHERE Solver = ?";
            psChange = connect.prepareStatement(prepStatementChange);
            //set prepared statement for deleteing current entry
            String delStatement="DELETE FROM rubikscube WHERE Solver = ?";
            psDelete= connect.prepareStatement(delStatement);
            loadAllData();
            addTestData();
            RubiksDataModel r=new RubiksDataModel(rs);
            RubiksGUI gui=new RubiksGUI(r);
            /*//add testData to Database
            addTestData();
            //adding a new entry to table
            addEntry();
            //changing an existing entry in table
            editEntry();*/


        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
        public static void shutDown() {

            try {
                if (psInsert != null) {
                    psInsert.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (psChange != null) {
                    psChange.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (psDelete != null) {
                    psDelete.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (searchSolver != null) {
                    searchSolver.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

            System.out.println("Program finished");

        }

    public static void addEntry(String solveName, Double timing){

        try {
            searchSolver.setString(1, solveName);
            ResultSet searchR = searchSolver.executeQuery();//look for an entry with this name
            int resultsCounter = 0;
            while (searchR.next()) {
                resultsCounter++;
            }
            if (resultsCounter == 0) {//if there is no entry, add it
                psInsert.setString(1, solveName);
                psInsert.setDouble(2, timing);
                psInsert.executeUpdate();
            } else {
                System.out.println("That person is already in the database.");
            }

        }catch(SQLException se){
            System.out.println("error adding entry "+se);
        }
    }

    public static void editEntry(String solverName, Double newTiming){
        //changing an entry for the table
        try {
            psChange.setDouble(1, newTiming);
            psChange.setString(2, solverName);
            psChange.executeUpdate();

        }catch(SQLException se){
            System.out.println("there was an error updating this file "+ se);
        }
    }

    public static void deleteEntry(String solverName){
        try{
            psDelete.setString(1, solverName);
            psDelete.executeUpdate();
        }catch(SQLException se){
            System.out.println(se);
            se.printStackTrace();
        }
    }

    public static void addTestData(){
        try{
            //store test data
            testData=new HashMap<>();
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
        }catch (SQLException se){
            System.out.println("failed to add test data "+se);
        }
    }
    //make results list of all data
    public static boolean loadAllData(){

        try{

            if (rs!=null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM " + tableName;
            rs = statement.executeQuery(getAllData);

            if (rubiksDataModel == null) {
                //If no current movieDataModel, then make one
                rubiksDataModel = new RubiksDataModel(rs);
            } else {
                //Or, if one already exists, update its ResultSet
                rubiksDataModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading cube");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }
    public static boolean setup(){//setup the database and connection
        try {
            Class.forName(JDBC_DRIVER);

        }catch(ClassNotFoundException cnfe){
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);
        }

        try {
            connect = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
            statement = connect.createStatement();
            boolean tableExists=false;
            String checkTablePresentQuery = "SHOW TABLES LIKE '"+tableName+"'";
            ResultSet tablesRS=statement.executeQuery(checkTablePresentQuery);
            if (tablesRS.next()){
                tableExists=true;
            }


            if(!tableExists){
                //make new table
                String newTable="CREATE TABLE IF NOT EXISTS "+tableName+" (Solver varchar (50), Solving_time double)";
                statement.executeUpdate(newTable);
            }

            return true;

        }catch(SQLException se){
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }
    public static ResultSet getRs(){
        loadAllData();
        return rs;
    }
}
