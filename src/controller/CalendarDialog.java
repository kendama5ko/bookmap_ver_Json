package controller;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import com.toedter.calendar.JCalendar;

public class CalendarDialog {

    private JCalendar calendar;
    private JDialog dialog;
    private JLabel editingLabel;

    public CalendarDialog(JTable table) {
        initializeUI(table);
    }

    private void initializeUI(JTable table) {

        dialog = new JDialog();
        calendar = new JCalendar();
        editingLabel = new JLabel();
        // ダイアログの設定
        dialog.setTitle("Select Date");
        dialog.setSize(300, 300);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(calendar);

        // ダイアログを閉じた時にセルの編集も終了
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                table.removeEditor();
            }
        });
    }

    /*
     * カレンダーの日付が選択された時の挙動
     */
    public void onDateSelected(JTable table) {
        calendar.getDayChooser().addPropertyChangeListener("day", evt -> {
            // 日付を取得し、フォーマットの形式に変換
            Date selectedDate = calendar.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM/dd");
            String formattedDate = dateFormat.format(selectedDate);

            // 選択された日付を JTable のセルにセット
            int selectedRow = table.getSelectedRow();
            int selectedColumn = table.getSelectedColumn();
            table.setValueAt(formattedDate, selectedRow, selectedColumn);

            // [重要] 編集状態を解除する（これをしないと編集状態のままになり、日付が入らない）
            table.removeEditor();
            dialog.dispose();
        });
    }

    /*
     * dateカラムを選択時のセルエディターの設定
     */
    public void onDateColumnSelected(JTable table) {
        // テーブルのdateカラムのセルエディタを設定（1ではなく変数で受け取る方が保守性が高い？)
        TableColumn dateColumn = table.getColumnModel().getColumn(1);

        dateColumn.setCellEditor(new TableCellEditor() {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                    int column) {

                table.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        // クリックされた少し左上にdailogを表示
                        int x = e.getXOnScreen() - 350;
                        int y = e.getYOnScreen() - 200;
                        
                        // 画面上部や左にdialogが見切れないようにする
                        if (e.getXOnScreen() < 350) {
                            x = 0;
                        }
                        if (e.getYOnScreen() < 200) {
                            y = 0;
                        }
                        dialog.setLocation(x, y);
                        if (!dialog.isVisible()) {
                            dialog.setVisible(true);
                        }
                    }
                });
                // 日付を編集中にセルに表示されるテキスト
                editingLabel.setText("日付を選択");
                editingLabel.setHorizontalAlignment(JLabel.CENTER);
                return editingLabel;
            }

            @Override
            public Object getCellEditorValue() {
                return null;
            }

            @Override
            public boolean isCellEditable(EventObject anEvent) {
                return true;
            }

            @Override
            public boolean shouldSelectCell(EventObject anEvent) {
                return true;
            }

            @Override
            public boolean stopCellEditing() {
                dialog.dispose(); // ダイアログを閉じる
                table.removeEditor();
                return true;
            }

            @Override
            public void cancelCellEditing() {
                dialog.dispose(); // ダイアログを閉じる
            }

            @Override
            public void addCellEditorListener(CellEditorListener l) {
            }

            @Override
            public void removeCellEditorListener(CellEditorListener l) {
            }
        });
    }
}
