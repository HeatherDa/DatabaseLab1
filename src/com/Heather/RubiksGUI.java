package com.Heather;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

/**
 * Created by cryst on 4/28/2016.
 */
public class RubiksGUI extends JFrame {
    private JPanel rootPanel;
    private JTextField Name;
    private JTextField time;
    private JButton updateEntryButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JTable rubikscubeTable;

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


        updateEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!editEntry){//not editing an entry, so adding one
                    Main.addEntry();
                }else{//editEntry is true, so changing an existing entry

                }

                //get selection from JTable
                //if
            }
        });




    }
    //to edit an existing record
    //select record causes
        //fill text boxes with details of record
    //change text boxes
    //push update button






}
