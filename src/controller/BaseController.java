package controller;

import javax.swing.*;
import javax.swing.JTable;
import javax.swing.JFormattedTextField;
import java.text.NumberFormat;


public class BaseController  {

    public void inputOnlyNumbers(JTable table, int columnIndex) {
        JFormattedTextField ftf = new JFormattedTextField(NumberFormat.getNumberInstance());
        ftf.setColumns(5);
        ftf.setHorizontalAlignment(JFormattedTextField.RIGHT);
        table.getColumnModel().getColumn(columnIndex).setCellEditor(new DefaultCellEditor(ftf));
    }
}
