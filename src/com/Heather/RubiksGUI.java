package com.Heather;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.geom.Arc2D;


public class RubiksGUI extends JFrame {
    private JPanel rootPanel;
    private JTextField Name;
    private JTextField time;
    private JButton updateEntryButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JTable rubikscubeTable;
    private JButton addButton;

    boolean editEntry=true;

    protected RubiksGUI(final RubiksDataModel rubiksDataTableModel){
        setContentPane(rootPanel);
        pack();
        setTitle("Rubics Cube Solution Records");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Why the hell isn't this working?
        rubikscubeTable.setGridColor(Color.BLACK);
        rubikscubeTable.setModel(rubiksDataTableModel);

        int currentRow = rubikscubeTable.getSelectedRow();
        if(currentRow !=-1){
            editEntry=true;
        }
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name=Name.getText();
                double timming= Double.parseDouble(time.getText());
                Main.addEntry(name, timming);//TODO figure out how to throw up an option screen if there is a record with this name already.
                rubiksDataTableModel.updateResultSet(Main.getRs());
            }
        });

        updateEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name=Name.getText();
                Double newTime=Double.parseDouble(time.getText());
                Main.editEntry(name, newTime);
                rubikscubeTable.clearSelection();
                Name.setText("");
                time.setText("");
                rubiksDataTableModel.updateResultSet(Main.getRs());
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!editEntry) {//nothing selected, can't delete anything
                    System.out.println("Select a record to delete it.");//TODO replace with option pane
                    JOptionPane.showMessageDialog(rootPane, "Select a record to delete it.");
                }else{//something is selected, so proceed with deletion
                    String name=Name.getText();
                    Main.deleteEntry(String.valueOf(name));//TODO make it work
                }//move selected row to absolute row and delete that row to delete a selected row
                rubiksDataTableModel.updateResultSet(Main.getRs());
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.shutDown();
                System.exit(0);
            }
        });



    }
    //to edit an existing record
    //select record causes
        //fill text boxes with details of record
    //change text boxes
    //push update button






}
