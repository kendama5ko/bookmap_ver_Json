package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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

    public JsonDAO() {
        this.mapper = new ObjectMapper();
        this.dataFile = Paths.get("lib/data/testData.json").toFile();

        // 更新されたJSONデータを整形してファイルに書き込む
        prettyPrint = mapper.writerWithDefaultPrettyPrinter();
        try {
            this.node = mapper.readTree(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToJsonFile(JsonNode node) {
        try {
            // 更新されたJSONデータをファイルに書き込む
            prettyPrint.writeValue(dataFile, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel setDataFromJson(DefaultTableModel progressModel) {
        JsonNode progressDataNode = node.get("本棚").get(0).get("進捗データ");
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

    public void addProgressData(String currentDate, int totalPages, String createdAt) {
        ArrayNode progressDataNode = (ArrayNode) node.get("本棚").get(0).get("進捗データ");
        
        ObjectNode newProgressNode = mapper.createObjectNode();
        newProgressNode.put("日付", currentDate);
        newProgressNode.put("読んだページ数", totalPages);
        newProgressNode.put("created_at", createdAt);
        progressDataNode.add(newProgressNode);

        writeToJsonFile(node);
    }

    public void deleteProgressData(long createdAt) {
        // 進捗データから選択した行を削除
        ArrayNode progressDataNode = (ArrayNode) node.get("本棚").get(0).get("進捗データ");

        for (int i = 0; i < progressDataNode.size(); i++) {
            JsonNode progressNode = progressDataNode.get(i);
            String createdAtString = progressNode.get("created_at").asText();
            Instant createdAtInstant = Instant.parse(createdAtString);
            long columnValue = createdAtInstant.toEpochMilli();

            if (columnValue == createdAt) {
                progressDataNode.remove(i);
                break; // 行が見つかったらループを抜ける
            }
        }
        writeToJsonFile(node);
    }
}
