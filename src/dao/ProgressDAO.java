package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dto.ProgressDTO;
import entity.ProgressBean;

public class ProgressDAO {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs;

    public void connect() {
        con = DatabaseSettings.getConnection();
    }

    public void disconnect() {
        try {
            if (ps != null)
                ps.close();
            if (con != null)
                con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressDTO select(int userId, int bookId) {
        ProgressDTO pdto = new ProgressDTO();
        String sql = "SELECT * FROM bookmap.progress WHERE user_id = ? AND book_id = ?";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProgressBean pb = new ProgressBean();
                pb.setBookId(rs.getInt("book_id"));
                pb.setProgressId(rs.getInt("progress_id"));
                pb.setUserId(rs.getInt("user_id"));
                pb.setTodayProgress(rs.getInt("today_progress"));
                pb.setCreatedAt(rs.getTimestamp("created_at"));
                pdto.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        return pdto;
    }

    /*
     * 本のタイトル名から本のIDを取得
     * （要修正）BookDAOに移動
     */
    public int searchBookId(int userId, String bookTitle) {
        int id = 0;
        String sql = "SELECT Distinct b.book_id FROM books b "
                + "LEFT OUTER JOIN user_books ub ON b.book_id = ub.book_id "
                + "LEFT OUTER JOIN progress p ON ub.user_id = p.user_id "
                + "WHERE ub.user_id = ? AND b.title = ?";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, bookTitle);
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt("book_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        return id;
    }

    /*
     * 最新データ取得
     */
    public List<String[]> getProgressData(int userId, int bookId) {
        String sql = "SELECT progress_id, today_progress, created_at FROM bookmap.progress "
                + "WHERE user_id = ? AND book_id = ? ORDER BY created_at DESC";
        connect();
        List<String[]> progressData = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy MM/dd");
                LocalDate date = createdAt.toLocalDateTime().toLocalDate();

                String progressId = String.valueOf(rs.getInt("progress_id"));
                String progressPages = String.valueOf(rs.getInt("today_progress")) + "P";
                String formattedDate = date.format(dateFormat);
                String differenceDate = getDifference(createdAt);
                progressData.add(new String[] { progressPages, differenceDate, formattedDate, progressId});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        return progressData;
    }
    
    public String getDifference(Timestamp createdAt) {
    	LocalDate today = LocalDate.now();
    	
    	//timestamp型をLocalDateTime型にし、さらにそれをLocalDate型に変換
        LocalDate pastDate = createdAt.toLocalDateTime().toLocalDate();
        
        int diffDays = today.getDayOfYear() - pastDate.getDayOfYear();
        int diffYears = today.getYear() - pastDate.getYear();
        if (diffYears > 0) {
        	diffDays += 365*diffYears;
        }
        if (diffDays == 0) {
        	return String.format("今日");
        	
        } else if (diffDays == 1){
        	return String.format("昨日");
        	
        } else if (diffDays == 2){
        	return String.format("一昨日");
        	
        } else if (diffDays <= 6) {
            return  String.format("%d日前", diffDays);
            
        } else if (diffDays <= 30) {
            return String.format("%d週間前", diffDays / 7);
            
        } else if (diffDays <= 365) {
            return String.format("%dか月前", diffDays / 30);
            
        } else {
            return String.format("%d年前", diffDays / 365);
        }
     }

    /*
     * 読んだ合計のページ数
     */
    public int selectCurrentPages(int userId, int bookId) {
        int result = 0;
        String sql = "SELECT today_progress FROM bookmap.progress WHERE user_id = ? AND book_id = ?";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProgressBean pb = new ProgressBean();
                pb.setTodayProgress(rs.getInt("today_progress"));
                result += pb.getTodayProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        return result;
    }

    /*
     * 今日のページ数登録
     */
    public void insertTodayPage(int userId, int bookId, int todayProgress) {
        String sql = "INSERT INTO bookmap.progress(user_id, book_id, today_progress) VALUES(?, ?, ?)";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setInt(3, todayProgress);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
    }

    /*
     * 最新データ1件削除
     */
    public void delete(int userId, int progressId) {
        String sql = "DELETE FROM bookmap.progress WHERE user_id = ? AND progress_id = ? ORDER by progress_id DESC LIMIT 1";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, progressId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
    }
}
