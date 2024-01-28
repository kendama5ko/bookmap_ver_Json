package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
    ArrayNode arraybookShelfNode;
    ArrayNode arrayProgressDataNode;
    JsonNode checkNode;

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

    public JsonNode getProgressDataNode(String bookID) {
        for (JsonNode bookNode : bookShelfNode) {
            String tempID = bookNode.get("ID").asText();
            if (tempID.equals(bookID)) {
                progressDataNode = bookNode.get("進捗データ");
                return progressDataNode;
            }
        }
        return mapper.createObjectNode();
    }

    public List<Map<String, String>> searchBookList() {
        List<Map<String, String>> bookInfoList = new ArrayList<>();

        for (JsonNode bsNode : bookShelfNode) {
            Map<String, String> bookInfo = new HashMap<>();
            bookInfo.put("タイトル", bsNode.get("タイトル").asText());
            bookInfo.put("著者", bsNode.get("著者").asText());
            bookInfo.put("ジャンル", bsNode.get("ジャンル").asText());
            bookInfo.put("ページ数", bsNode.get("ページ数").asText());
            bookInfo.put("ID", bsNode.get("ID").asText());
            bookInfoList.add(bookInfo);
        }
        return bookInfoList;
    }

    public DefaultTableModel setDataFromJson(String bookID, DefaultTableModel progressModel) {
        progressDataNode = getProgressDataNode(bookID);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        for (JsonNode progressNode : progressDataNode) {

            checkNode = progressNode.get("読んだページ数");
            if (checkNode != null) {
                int pageCount = progressNode.get("読んだページ数").asInt();
                String date = progressNode.get("日付").asText();
                String createdAtString = progressNode.get("created_at").asText();

                OffsetDateTime offsetDateTime = OffsetDateTime.parse(createdAtString, formatter);

                // OffsetDateTimeからInstantを取得
                Instant createdAtInstant = offsetDateTime.toInstant();
                long createdAtTimestamp = createdAtInstant.toEpochMilli();

                progressModel.addRow(new Object[] { pageCount, date, createdAtTimestamp });
            }
        }
        return progressModel;
    }

    public void addBookToJson(Map<String, String> bookInfoMap) {
        arraybookShelfNode = (ArrayNode) bookShelfNode;

        ObjectNode newBookNode = mapper.createObjectNode();
        for (Map.Entry<String, String> entry : bookInfoMap.entrySet()) {
            newBookNode.put(entry.getKey(), entry.getValue());
        }
        newBookNode.putArray("進捗データ");

        arraybookShelfNode.add(newBookNode);
        writeToJsonFile(node);
    }

    public void addProgressData(String bookID, String currentDate, int todayProgress, String createdAt) {
        arrayProgressDataNode = (ArrayNode) getProgressDataNode(bookID);

        ObjectNode newProgressNode = mapper.createObjectNode();
        newProgressNode.put("日付", currentDate);
        newProgressNode.put("読んだページ数", todayProgress);
        newProgressNode.put("created_at", createdAt);
        arrayProgressDataNode.add(newProgressNode);

        writeToJsonFile(node);
    }

    public String editBookData(String bookID, String columnName, String editedData) {
        arraybookShelfNode = (ArrayNode) node.get("本棚");

        for (JsonNode bookNode : arraybookShelfNode) {
            String tempID = bookNode.get("ID").asText();

            // 更新したい本のノードを見つけたらタイトルを書き換える
            if (tempID.equals(bookID)) {
                ((ObjectNode) bookNode).put(columnName, editedData);
                break;
            }
        }
        writeToJsonFile(node);
        return updatedMessage(columnName);
    }

    private String updatedMessage(String columnName) {
        switch (columnName) {
            case "タイトル":
                return "タイトルが変更されました。";
            case "著者":
                return "著者名が変更されました";
            case "ジャンル":
                return "ジャンル名が変更されました";
            case "ページ数":
                return "ページ数が変更されました";
            default:
                return columnName;
        }
    }

    public void deleteProgressData(String bookID, long createdAt) {
        // 進捗データから選択した行を削除
        // arrayProgressDataNode = (ArrayNode) node.get("本棚").get(0).get("進捗データ");
        arrayProgressDataNode = (ArrayNode) getProgressDataNode(bookID);

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

    public int getCurrentPages(String bookID) {
        int currentPages = 0;
        progressDataNode = getProgressDataNode(bookID);
        for (JsonNode data : progressDataNode) {
            JsonNode pageCountNode = data.get("読んだページ数");

            if (pageCountNode != null && !pageCountNode.isNull()) {
                currentPages += pageCountNode.asInt();
            }
        }
        return currentPages;
    }

    public int getTotalPages(String bookID) {
        int totalPages = 0;
        for (JsonNode book : bookShelfNode) {
            if (book.get("ID").asText().equals(bookID)) {
                totalPages = book.get("ページ数").asInt();
            }
        }
        return totalPages;
    }

    public List<String> getDates(String bookID) {
        List<String> dates = new ArrayList<>();
        progressDataNode = getProgressDataNode(bookID);

        for (JsonNode date : progressDataNode) {
            checkNode = date.get("日付");
            if (checkNode != null) {
                dates.add(date.get("日付").asText());
            }
        }
        return dates;
    }

    public void deleteBook(String bookID) {
        this.bookShelfNode = node.get("本棚");
        arraybookShelfNode = (ArrayNode) bookShelfNode;
        int index = 0;
        for (int i = 0; i < arraybookShelfNode.size(); i++) {
            JsonNode node = arraybookShelfNode.get(i);
            if (node.get("ID").asText().equals(bookID)) {
                index = i;
                break;
            }
        }
        // 要素を削除 要素がない時は-1が返ってくる
        if (index != -1) {
            arraybookShelfNode.remove(index);
        }
        writeToJsonFile(node);
    }
}
