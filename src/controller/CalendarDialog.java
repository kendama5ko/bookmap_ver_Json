package controller;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import com.toedter.calendar.JCalendar;

import dao.JsonDAO;

public class CalendarDialog {

    private JsonDAO jdao;
    private JDialog dialog;
    private JCalendar calendar;
    private JLabel editingLabel;
    private JTable progressDataTable;
    private int dialogX = 0;
    private int dialogY = 0;
    private boolean wasMoved;

    public CalendarDialog(JTable progressDataTable) {
        initializeUI(progressDataTable);
    }

    private void initializeUI(JTable progressDataTable) {
        this.jdao = new JsonDAO();
        this.dialog = new JDialog();
        this.calendar = new JCalendar();
        this.editingLabel = new JLabel();
        this.progressDataTable = progressDataTable;

        // ダイアログの設定
        dialog.setTitle("Select Date");
        dialog.setSize(300, 300);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(calendar);

        // ダイアログを閉じた時にセルの編集も終了
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                progressDataTable.removeEditor();
            }
        });

        // ダイアログが移動した時に座標を記憶
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                saveDialogPosition();
            }
        });
    }

    /*
     * カレンダーの日付が選択された時の挙動
     */
    public void onDateSelected(DefaultTableModel progressModel) {
        calendar.getDayChooser().addPropertyChangeListener("day", evt -> {

            // 日付を取得し、フォーマットの形式に変換
            Date selectedDate = calendar.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM/dd");
            String formattedDate = dateFormat.format(selectedDate);

            // 選択された日付を JTable のセルにセット
            int selectedRow = progressDataTable.getSelectedRow();
            int selectedColumn = progressDataTable.getSelectedColumn();
            progressModel.setValueAt(formattedDate, selectedRow, selectedColumn);

            // [重要] 編集状態を解除する（これをしないと編集状態のままになり、日付が入らない）
            progressDataTable.removeEditor();
            //dialog.dispose();
        });
    }

    /*
     * dateカラムを選択時のセルエディターの設定
     */
    public TableColumn onDateColumnSelected() {
        // テーブルのdateカラムのセルエディタを設定（1ではなく変数で受け取る方が保守性が高い？)
        TableColumn dateColumn = progressDataTable.getColumnModel().getColumn(1);

        dateColumn.setCellEditor(new TableCellEditor() {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                    int column) {
                // // JCalendarを指定した日付で開く
                String selectedData = (String) table.getValueAt(row, column);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM/dd");
                Date date = null;
                try {
                    date = dateFormat.parse(selectedData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // イベントリスナーを一時的に削除
                PropertyChangeListener[] listeners = calendar.getDayChooser().getPropertyChangeListeners();
                for (PropertyChangeListener listener : listeners) {
                    calendar.getDayChooser().removePropertyChangeListener(listener);
                }

                calendar.setDate(date);

                // イベントリスナーを再追加
                for (PropertyChangeListener listener : listeners) {
                    calendar.getDayChooser().addPropertyChangeListener(listener);
                }

                // セルが編集中の場合のみ表示する
                if (isSelected || table.isCellSelected(row, column)) {
                    // 日付を編集中にセルに表示されるテキスト
                    editingLabel.setText("日付を選択");
                    editingLabel.setHorizontalAlignment(JLabel.CENTER);
                    return editingLabel;
                    // 編集中でない場合は何も表示しない
                } else {
                    return null;
                }
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
                saveDialogPosition();
                dialog.dispose(); // ダイアログを閉じる
                progressDataTable.removeEditor();
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

        return dateColumn;
    }

    public void openCalendarSetting() {
        progressDataTable.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int selectedColumn = progressDataTable.columnAtPoint(e.getPoint());

                // カラム1がクリックされたときだけ処理を実行
                if (selectedColumn == 1 && wasMoved) { // 
                    saveDialogPosition();
                    dialog.setLocation(dialogX, dialogY);
                    dialog.setVisible(true);
                } else if (selectedColumn == 1 && !wasMoved) {

                    // クリックされた少し左上にdailogを表示
                    dialogX = e.getXOnScreen() + 40;
                    dialogY = e.getYOnScreen() - 105;
                    GraphicsDevice gd = progressDataTable.getGraphicsConfiguration().getDevice();
                    int width = gd.getDisplayMode().getWidth();
                    
                    // 画面上部や左にdialogが見切れないようにする
                    if (dialogX > width - 340) {
                        dialogX = width - 340;
                    }
                    if (dialogY < 0) {
                        dialogY = 0;
                    }
                    dialog.setLocation(dialogX, dialogY);
                    dialog.setVisible(true);
                } else if (selectedColumn != 1) {
                    dialog.dispose();
                }
            }
        });
    }

    public void saveDialogPosition() {
        this.dialogX = dialog.getLocation().x;
        this.dialogY = dialog.getLocation().y;
        wasMoved = true;
    }

    public void dialogDispose() {
        dialog.dispose();
    }
}
