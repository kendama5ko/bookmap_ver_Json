package controller;

import java.awt.Color;
import java.io.File;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.JsonDAO;

public class MainFrameController {

    File dataFile;
    ObjectMapper mapper;
    JsonNode node;
    JsonDAO jdao;
    DefaultComboBoxModel<String> comboModel;
    List<String> bookList;

    public MainFrameController() {
        this.dataFile = Paths.get("lib/data/testData.json").toFile();
        this.mapper = new ObjectMapper();
        this.jdao = new JsonDAO();
    }

    public DefaultComboBoxModel<String> setBookList() {
        comboModel = new DefaultComboBoxModel<>();
        bookList = new ArrayList<String>();

        bookList = jdao.searchBookList();
		for (String bList : this.bookList) {
			comboModel.addElement(bList);
		}
        // int newIndexStart = 0; // 新しい要素の最初のインデックス
		// int newIndexEnd = bookList.size() - 1; // 新しい要素の最後のインデックス

		// // fireIntervalAdded を呼び出して変更を通知
		// fireIntervalAdded(this, newIndexStart, newIndexEnd);
		return comboModel;
    }

    public DefaultTableModel reloadProgressModel(DefaultTableModel progressModel) {
        progressModel.setRowCount(0);
        jdao.setDataFromJson(progressModel);

        // timestampで降順にソート
        Collections.sort(progressModel.getDataVector(), (o1, o2) -> {
            Long rowNumber1 = (Long) o1.get(2);
            Long rowNumber2 = (Long) o2.get(2);
            return rowNumber2.compareTo(rowNumber1);
        });
        return progressModel;
    }

    public JTable progressDataTableSettings(JTable progressDataTable) {
        // progressDataTable.setBackground(new Color(225, 238, 251));
        progressDataTable.setForeground(new Color(15, 15, 15));

        progressDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumn pagesColumn = progressDataTable.getColumnModel().getColumn(0);
        TableColumn dateColumn = progressDataTable.getColumnModel().getColumn(1);
        TableColumn relativelyDateColumn = progressDataTable.getColumnModel().getColumn(2);
        TableColumn progressIdColumn = progressDataTable.getColumnModel().getColumn(3);
        pagesColumn.setPreferredWidth(42);
        dateColumn.setPreferredWidth(45);

        // ツールチップ用と削除時の検索用のカラムなので非表示にする
        relativelyDateColumn.setWidth(0);
        relativelyDateColumn.setMinWidth(0);
        relativelyDateColumn.setMaxWidth(0);
        relativelyDateColumn.setPreferredWidth(0);
        progressIdColumn.setWidth(0);
        progressIdColumn.setMinWidth(0);
        progressIdColumn.setMaxWidth(0);
        progressIdColumn.setPreferredWidth(0);

        progressDataTable = setRightRenderer(progressDataTable);
        return progressDataTable;
    }

    public JTable setRightRenderer(JTable progressDataTable) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        progressDataTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        progressDataTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        return progressDataTable;
    }

    public void addRecentData(int userId, int bookId, int totalPages) {
        // 現在時刻を取得
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy MM/dd");
        DateTimeFormatter ISO8601 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        String createdAt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).format(ISO8601);
        String currentDate = ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).format(dateFormat);

        // 進捗データを追加
        jdao.addProgressData(currentDate, totalPages, createdAt);
    }

    public void deleteSelectedData(long createdAt) {
        jdao.deleteProgressData(createdAt);
    }

    /*
     * setText Label, Button
     */
    public String setRemainPageLabel(String bookTitle) {
        int currentPages = jdao.getCurrentPages(bookTitle);
        int totalPages = jdao.getTotalPages(bookTitle);

        if (currentPages > totalPages) {
			return "Finish!!";
		}
        return " " + currentPages + "P / "	+ totalPages + "P";
    }

    public int getTotalDays(String bookTitle) {
        int sumDays = 0;
        String previousDate = null;

        List<String> dates = jdao.getDates(bookTitle);
        for (String date : dates) {
            if (!date.equals(previousDate)) {
                sumDays++;
                previousDate = date;
            }
        }
        return sumDays;
    }

    public String setAvgPagesLabel(String bookTitle) {
        int totalDays = getTotalDays(bookTitle);
        int currentPages = jdao.getCurrentPages(bookTitle);

        if (totalDays <= 0) {
            return "0P / day";
        } else {
            int avgPages = currentPages/ totalDays;
            return String.valueOf(avgPages) + "P / day";
        }
    }
}
