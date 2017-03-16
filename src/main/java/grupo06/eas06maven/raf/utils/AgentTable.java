/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo06.eas06maven.raf.utils;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import grupo06.eas06maven.raf.utils.TableListener.*;

/**
 *
 * @author Michel
 */
public class AgentTable extends JTable{
    
    private final DefaultTableModel modelAgentsTable;
    private final TableListener rowSelectedListener;
    
    public AgentTable(TableListener listener, String[] columnsNames, boolean clickable){
        super (new DefaultTableModel(DATA, columnsNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        if(clickable){
            this.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    rowClick(e);
            }
            });
        }
        
        this.modelAgentsTable = (DefaultTableModel) this.dataModel;   
        this.rowSelectedListener = listener;
    }

    
    public void putLog(String[] rowData){
        int r = modelAgentsTable.getRowCount();
        for(int i = 0; i < r; i++){
            if(modelAgentsTable.getValueAt(i, 0) == ""){
                modelAgentsTable.insertRow(i, rowData);
                if(i > 0) modelAgentsTable.removeRow(i+1);
                else modelAgentsTable.removeRow(i+2);
                return;
            }else{
                if(modelAgentsTable.getValueAt(i, 0).equals(rowData[0])){
                    modelAgentsTable.setValueAt(rowData[1], i, 1);
                    modelAgentsTable.setValueAt(rowData[2], i, 2);
                    return;
                }
            }
        }
        modelAgentsTable.addRow(rowData);
    }
    
    public void deleteData(){ 
        int rows = modelAgentsTable.getRowCount(); 
        for(int i = rows - 1; i >=0; i--){
           modelAgentsTable.removeRow(i); 
        }
    }
 
    private void rowClick(java.awt.event.MouseEvent evt) {
        String cadena="";
        int row = this.rowAtPoint(evt.getPoint());
        if (row >= 0 && this.isEnabled())
        {
            for (int i=0; i < this.getColumnCount();i++)
            {
               cadena=cadena + " " +  this.getValueAt(row,i).toString();
            }
        }
        cadena = cadena.split(" ")[1];
        TableEvent e = new TableEvent(this,cadena);
        rowSelectedListener.raSelectedChange(e);
    }
    
    private static final String[][] DATA = {
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""},
        {"","", ""}
    };
    
}
