package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dao.ProgressDAO;

class getDifferenceTest {
	private static ProgressDAO pdao;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}
	

	@Test
	void test() {
		ProgressDAO pdao = new ProgressDAO();
		Timestamp createdAt = Timestamp.valueOf("2024-1-01 10:34:56");

        // 期待される結果を指定
        String expected = "今日"; // 期待される結果に置き換えてください

        // テスト実行
        String result = pdao.getDifference(createdAt);

        // 結果を検証
        assertEquals(expected, result);
	}
}
