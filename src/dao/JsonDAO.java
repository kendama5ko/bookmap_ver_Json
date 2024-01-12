package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.*;


import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonDAO {
    private File dataFile;
    private ObjectMapper mapper;
    private JsonNode node;
    ObjectWriter prettyPrint;
    JsonNode bookDataNode;
    JsonNode bookShelfNode;
    JsonNode progressDataNode = null;
    ArrayNode arrayProgressDataNode;

    public JsonDAO() {
        this.dataFile = Paths.get("lib/data/testData.json").toFile();
        this.mapper = new ObjectMapper();
        // 更新されたJSONデータを整形してファイルに書き込む
        prettyPrint = mapper.writerWithDefaultPrettyPrinter();
        try {
            this.node = mapper.readTree(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bookShelfNode = node.get("本棚");
    }

    public void writeToJsonFile(JsonNode node) {
        try {
            // 更新されたJSONデータをファイルに書き込む
            prettyPrint.writeValue(dataFile, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonNode getProgressDataNode(String bookTitle) {
        progressDataNode = bookShelfNode;
        for (JsonNode bookNode : bookShelfNode) {
            String tempBook = bookNode.get("タイトル").asText();
            if (tempBook.equals(bookTitle)) {
                progressDataNode = bookNode.get("進捗データ");
            }
        }
        return progressDataNode;
    }

    public List<Map<String, String>> searchBookList() {
        List<Map<String, String>> bookInfoList = new ArrayList<>();
    
        for (JsonNode bsNode : bookShelfNode) {
            Map<String, String> bookInfo = new HashMap<>();
            bookInfo.put("タイトル", bsNode.get("タイトル").asText());
            bookInfo.put("著者", bsNode.get("著者").asText());
            bookInfo.put("ジャンル", bsNode.get("ジャンル").asText());
            bookInfo.put("総ページ数", bsNode.get("総ページ数").asText());
            bookInfo.put("UUID", bsNode.get("UUID").asText());
            bookInfoList.add(bookInfo);
        }
        return bookInfoList;
    }

    public DefaultTableModel setDataFromJson(String bookTitle, DefaultTableModel progressModel) {
        // JsonNode progressDataNode = node.get("本棚").get(0).get("進捗データ");
        progressDataNode = getProgressDataNode(bookTitle);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        for (JsonNode progressNode : progressDataNode) {
            int pageCount = progressNode.get("読んだページ数").asInt();
            String date = progressNode.get("日付").asText();
            String createdAtString = progressNode.get("created_at").asText();

            OffsetDateTime offsetDateTime = OffsetDateTime.parse(createdAtString, formatter);

            // OffsetDateTimeからInstantを取得
            Instant createdAtInstant = offsetDateTime.toInstant();
            long createdAtTimestamp = createdAtInstant.toEpochMilli();

            progressModel.addRow(new Object[] { pageCount, date, createdAtTimestamp });
        }
        return progressModel;
    }

    public void addProgressData(String bookTitle, String currentDate, int totalPages, String createdAt) {
        // arrayProgressDataNode = (ArrayNode) node.get("本棚").get(0).get("進捗データ");
        arrayProgressDataNode = (ArrayNode) getProgressDataNode(bookTitle);

        ObjectNode newProgressNode = mapper.createObjectNode();
        newProgressNode.put("日付", currentDate);
        newProgressNode.put("読んだページ数", totalPages);
        newProgressNode.put("created_at", createdAt);
        arrayProgressDataNode.add(newProgressNode);

        writeToJsonFile(node);
    }

    public void deleteProgressData(String bookTitle, long createdAt) {
        // 進捗データから選択した行を削除
        // arrayProgressDataNode = (ArrayNode) node.get("本棚").get(0).get("進捗データ");
        arrayProgressDataNode = (ArrayNode) getProgressDataNode(bookTitle);


        for (int i = 0; i < arrayProgressDataNode.size(); i++) {
            JsonNode progressNode = arrayProgressDataNode.get(i);
            String createdAtString = progressNode.get("created_at").asText();
            Instant createdAtInstant = Instant.parse(createdAtString);
            long originalCreatedAt = createdAtInstant.toEpochMilli();

            if (originalCreatedAt == createdAt) {
                arrayProgressDataNode.remove(i);
                break; // 行が見つかったらループを抜ける
            }
        }
        writeToJsonFile(node);
    }



    public int getCurrentPages(String bookTitle) {
        int currentPages = 0;
        progressDataNode = getProgressDataNode(bookTitle);
        for (JsonNode data : progressDataNode) {
            currentPages += data.get("読んだページ数").asInt();
        }
        return currentPages;
    }

    public int getTotalPages(String bookTitle) {
        int totalPages = 0;
        for (JsonNode book : bookShelfNode) {
            if (book.get("タイトル").asText().equals(bookTitle)) {
                totalPages = book.get("総ページ数").asInt();
            }
        }
        return totalPages;
    }

    public List<String> getDates(String bookTitle) {
        List<String> dates = new ArrayList<>();
        progressDataNode = getProgressDataNode(bookTitle);
        for (JsonNode date : progressDataNode) {
            dates.add(date.get("日付").asText());
        }
        return dates;
    }
}
