package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.JsonDAO;
import window.mainFrame.BookInfo;

public class MainFrameController extends DefaultComboBoxModel<String> {

    Properties properties;
    File dataFile;
    ObjectMapper mapper;
    JsonNode node;
    JsonDAO jdao;
    DefaultComboBoxModel<BookInfo> comboModel;
    List<Map<String, String>> bookInfoList;
    BookInfo[] bookTitleList;

    public MainFrameController() {
        this.dataFile = Paths.get("lib/data/testData.json").toFile();
        this.mapper = new ObjectMapper();
        this.jdao = new JsonDAO();
        this.properties = new Properties();
    }

    public DefaultComboBoxModel<BookInfo> setBookList() {
        this.jdao = new JsonDAO();
        bookInfoList = jdao.searchBookList();
        bookTitleList = new BookInfo[bookInfoList.size()];
        int i = 0;
        for (Map<String, String> book : bookInfoList) {
            BookInfo bookTitle = new BookInfo(book.get("ID"), book.get("タイトル"));
            bookTitleList[i] = bookTitle;
            i++;
        }
        comboModel = new DefaultComboBoxModel<>();
        
        comboModel.removeAllElements();
        for (BookInfo bookTitle : bookTitleList) {
            comboModel.addElement(bookTitle);
        }
        
        // int newIndexStart = 0; // 新しい要素の最初のインデックス
        // int newIndexEnd = bookInfoList.size() - 1; // 新しい要素の最後のインデックス

        // // fireIntervalAdded を呼び出して変更を通知
        // fireIntervalAdded(this, newIndexStart, newIndexEnd);

        return comboModel;
    }

    public String[] getLastBook() {
        try {
            properties.load(new InputStreamReader(new FileInputStream("bookmap.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String lastBookID = properties.getProperty("lastBookID");
        String lastBookTitle = properties.getProperty("lastBookTitle");
        String[] lastBookInfo = {lastBookID, lastBookTitle};

        return lastBookInfo;
    }

    public void setlastBook(String bookID, String bookTitle) {
        properties.setProperty("lastBookID", bookID);
        properties.setProperty("lastBookTitle", bookTitle);
        try {
            properties.store(new OutputStreamWriter(new FileOutputStream("bookmap.properties"), "UTF-8"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel reloadProgressModel(String bookID, DefaultTableModel progressModel) {
        this.jdao = new JsonDAO();
        progressModel.setRowCount(0);
        jdao.setDataFromJson(bookID, progressModel);

        // timestampで降順にソート
        Collections.sort(progressModel.getDataVector(), (o1, o2) -> {
            Long rowNumber1 = (Long) o1.get(2); //created_atはcolumn3なのでindex2を指定
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

    public void addRecentData(String bookID, int bookId, int todayProgress) {
        this.jdao = new JsonDAO();
        // 現在時刻を取得
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy MM/dd");
        DateTimeFormatter ISO8601 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        String createdAt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).format(ISO8601);
        String currentDate = ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).format(dateFormat);

        // 進捗データを追加
        jdao.addProgressData(bookID, currentDate, todayProgress, createdAt);
    }

    public void deleteSelectedData(String bookID, long createdAt) {
        jdao.deleteProgressData(bookID, createdAt);
    }

    /*
     * setText Label, Button
     */
    public String setRemainPageLabel(String bookID) {
        this.jdao = new JsonDAO();
        int currentPages = jdao.getCurrentPages(bookID);
        int totalPages = jdao.getTotalPages(bookID);

        if (currentPages > totalPages) {
            return "Finish!!";
        }
        return " " + currentPages + "P / " + totalPages + "P";
    }

    public int getTotalDays(String bookID) {
        this.jdao = new JsonDAO();
        int sumDays = 0;
        String previousDate = null;

        List<String> dates = jdao.getDates(bookID);
        for (String date : dates) {
            if (!date.equals(previousDate)) {
                sumDays++;
                previousDate = date;
            }
        }
        return sumDays;
    }

    public String setAvgPagesLabel(String bookID) {
        this.jdao = new JsonDAO();
        int totalDays = getTotalDays(bookID);
        int currentPages = jdao.getCurrentPages(bookID);

        if (totalDays <= 0) {
            return "0P / day";
        } else {
            int avgPages = currentPages / totalDays;
            return String.valueOf(avgPages) + "P / day";
        }
    }

    public int setProgress(String bookID) {
        this.jdao = new JsonDAO();
        int currentPages = jdao.getCurrentPages(bookID);
        int totalPages = jdao.getTotalPages(bookID);

        if (totalPages == 0) {
            return 0;
        } else if (currentPages > totalPages) {
            return 100;
        } else {
            return (currentPages * 100) / totalPages;// 現在の達成率
        }
    }

    public String setProgressLabel(String bookID) {
        return setProgress(bookID) + "％";
    }

    public void changeFont(JComponent component, Font font) {
        if (component instanceof JComponent && !(component instanceof JLabel)) {
            component.setFont(font);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                if (child instanceof JComponent) {
                    changeFont((JComponent) child, font);
                }
            }
        }
    }
}
