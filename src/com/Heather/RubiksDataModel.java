package com.Heather;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cryst on 4/28/2016.
 */
public class RubiksDataModel extends AbstractTableModel{

    private int rowCount=0;
    private int colCount=0;
    ResultSet resultSet;

    public RubiksDataModel(ResultSet results){
        this.resultSet=results;
        setup();
    }

    private void setup(){
        countRows();
        try{
            colCount=resultSet.getMetaData().getColumnCount();
        }catch(SQLException se){
            System.out.println("Couldn't count Columns "+ se);
        }
    }

    public void updateResultSet(ResultSet newRS){
        resultSet=newRS;
        setup();
    }

    private void countRows() {
        rowCount = 0;
        try {
            //Move cursor to the start...
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                rowCount++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }
    @Override
    public int getRowCount(){
        countRows();
        return rowCount;
    }
    @Override
    public int getColumnCount(){
        getColumnCount();
        return colCount;
    }
    @Override
    public Object getValueAt(int row, int col){
        try{
            resultSet.absolute(row+1);
            Object o=resultSet.getObject(col+1);
            return o.toString();
        }catch(SQLException se){
            System.out.println(se);
            return se.toString();
        }
    }


}
