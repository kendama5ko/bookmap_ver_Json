package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Controllerクラスからの処理を受け取り、Jsonファイルの読み書きを行います。
 * 
 * @version 1.0
 */
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
    /*
     * JsonDAOクラスの新しいインスタンスを生成します。
     * ファイルパスを設定し、ObjectMapperで読み込み、JsonNodeに変換します。
     */
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

    /**
     * 引数として渡されたJsonNodeをJSONファイルに書き込みます。
     * 
     * @param node
     */
    public void writeToJsonFile(JsonNode node) {
        try {
            // 更新されたJSONデータをファイルに書き込む
            prettyPrint.writeValue(dataFile, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定された書籍IDに関連する進捗データノードを取得します。
     * 
     * @param bookID
     * @return 指定された書籍IDに関連する進捗データノード
     */
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

    /**
     * 書籍情報のリストを検索し、各書籍の情報をMapのリストとして返します。
     * 
     * @return 書籍情報のMapのリスト
     */
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

    /**
     * 指定された書籍IDに関連する進捗データを読み込み、DefaultTableModelにデータをセットします。
     * 
     * @param bookID        書籍ID
     * @param progressModel データをセットするDefaultTableModel
     * @return データがセットされたDefaultTableModel
     */
    public DefaultTableModel getDataFromJson(String bookID, DefaultTableModel progressModel) {
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

                progressModel.addRow(new Object[] { pageCount, date, createdAtTimestamp, bookID});
            }
        }
        return progressModel;
    }

    /**
     * 新規の書籍情報をJSONファイルに追加します。
     * 
     * @param bookInfoMap 書籍情報のMap
     */
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

    /**
     * 指定された書籍IDの新規の進捗データをJSONファイルに追加します。
     * 
     * @param bookID        書籍ID
     * @param currentDate   現在の日付
     * @param todayProgress 本日の進捗ページ数
     * @param createdAt     作成日時
     */
    public void addProgressData(String bookID, String currentDate, int todayProgress, String createdAt) {
        arrayProgressDataNode = (ArrayNode) getProgressDataNode(bookID);

        ObjectNode newProgressNode = mapper.createObjectNode();
        newProgressNode.put("日付", currentDate);
        newProgressNode.put("読んだページ数", todayProgress);
        newProgressNode.put("created_at", createdAt);
        arrayProgressDataNode.add(newProgressNode);

        writeToJsonFile(node);
    }

    /**
     * 現在時刻を取得
     * 
     * @return フォーマットされていない現在時刻
     */
    public String getCurrentDate() {
        DateTimeFormatter ISO8601 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String createdAt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).format(ISO8601);

        return createdAt;
    }

    
    public void setEditedDataToJson(String bookID, String columnName, Long createdAtLong, String editedData) {
        arrayProgressDataNode = (ArrayNode) getProgressDataNode(bookID);

        for (JsonNode PDnode : arrayProgressDataNode) {
            String createdAtString = PDnode.get("created_at").asText();
            Instant createdAtInstant = Instant.parse(createdAtString);
            long originalCreatedAt = createdAtInstant.toEpochMilli();
            
            String createdAt = getCurrentDate(); 

            // 更新したい本のノードを見つけたらタイトルを書き換える
            if (originalCreatedAt == createdAtLong) {
                // 編集されたカラムが"ページ数"だった場合
                if (columnName.equals("ページ数")) {
                    columnName = "読んだページ数";
                    // editDataをintに変換
                    int editedDataInt = Integer.parseInt(editedData);
                    ((ObjectNode) PDnode).put(columnName, editedDataInt);

                } else if (columnName.equals("日付")) {
                    ((ObjectNode) PDnode).put(columnName, editedData);
                }
                ((ObjectNode) PDnode).put("created_at", createdAt);
            }
        }
        writeToJsonFile(node);
    }

    /**
     * カレンダーから日付を編集して進捗データを更新します。
     * 
     * @param bookID     書籍ID
     * @param editedData 編集後の日付データ
     */
    // public void editDateAtProgressDataTableFromCalendar(String bookID, Long createdAtLong, String editedData) {
        
    //     this.bookShelfNode = node.get("本棚");
    //     arrayProgressDataNode = (ArrayNode) getProgressDataNode(bookID);

    //     for (JsonNode PDnode : arrayProgressDataNode) {
    //         String createdAtString = PDnode.get("created_at").asText();
    //         Instant createdAtInstant = Instant.parse(createdAtString);
    //         long originalCreatedAt = createdAtInstant.toEpochMilli();
            
    //         String createdAt = getCurrentDate(); 

    //         // 更新したい本のノードを見つけたらタイトルを書き換える
    //         if (originalCreatedAt == createdAtLong) {
    //             ((ObjectNode) PDnode).put("日付", editedData);
    //             ((ObjectNode) PDnode).put("created_at", createdAt);
    //         }
    //     }
    //     writeToJsonFile(node);
    // }

    /**
     * ManageFrameの本棚の編集。
     * 指定された書籍IDに関連する書籍データの特定の列を編集します。
     * 
     * @param bookID     書籍ID
     * @param columnName 編集する列の名前
     * @param editedData 編集後のデータ
     * @return 列の名前に応じた更新完了のメッセージ
     */
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

    /**
     * 更新された列の名前に応じて適切な更新メッセージを返します。
     * 
     * @param columnName 列名
     * @return 更新メッセージ
     */
    private String updatedMessage(String columnName) {
        return switch (columnName) {
            case "タイトル" -> "タイトルが変更されました";
            case "著者" -> "著者名が変更されました";
            case "ジャンル" -> "ジャンル名が変更されました";
            case "ページ数" -> "ページ数が変更されました";
            default -> columnName;
        };
    }

    /**
     * 指定された書籍IDと作成日時に基づいて進捗データを削除します。
     * MainFrameの進捗を1件削除する
     * 
     * @param bookID    書籍ID
     * @param createdAt 作成日時
     */
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

    /**
     * 指定された書籍IDに関連する現在のページ数を取得します。
     * 
     * @param bookID 書籍ID
     * @return 現在のページ数
     */
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

    /**
     * 指定された書籍IDに関連する総ページ数を取得します。
     * 
     * @param bookID 書籍ID
     * @return 総ページ数
     */
    public int getTotalPages(String bookID) {
        int totalPages = 0;
        for (JsonNode book : bookShelfNode) {
            if (book.get("ID").asText().equals(bookID)) {
                totalPages = book.get("ページ数").asInt();
            }
        }
        return totalPages;
    }

    /**
     * 指定された書籍IDに記録されている全ての日付のリストを取得します。
     * MainFrameControllerのgetTotalDaysで、読んだ日の合計の日数を計算するため。
     * 
     * @param bookID 書籍ID
     * @return 日付のリスト
     */
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

    /**
     * 指定された書籍IDに関連する書籍データを削除します。
     * ManageBooksにある本棚から1件削除
     * 
     * @param bookID 書籍ID
     */
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
