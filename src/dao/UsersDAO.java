package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dto.UsersDTO;
import entity.UsersBean;

public class UsersDAO {
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
    public int countloginId(String loginId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM users WHERE login_id = ?";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loginId);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        return count;
    }

    /*
     * passwordとsaltをudtoに格納
     */
    public UsersDTO selectPassWithSalt(String loginId) throws Exception {
        UsersDTO udto = new UsersDTO();
        String sql = "SELECT hashed_password, salt FROM bookmap.users WHERE login_id = ?";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loginId);
            rs = ps.executeQuery();
            while (rs.next()) {
                UsersBean ub = new UsersBean();
                ub.setHashedPassword(rs.getString("hashed_password"));
                ub.setSalt(rs.getString("salt"));
                udto.add(ub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        return udto;
    }

    /*
     * loginIdからuserIdとbookIdを取得
     */
    public int[] searchIds(String loginId) {
        int[] ids = new int[2];
        String sql = "SELECT ub.user_id, ub.book_id FROM user_books ub " +
                "INNER JOIN users u ON ub.user_id = u.user_id " +
                "LEFT JOIN progress p ON ub.user_id = p.user_id " +
                "WHERE u.login_id = ? " +
                "ORDER BY p.created_at DESC LIMIT 1";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loginId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ids[0] = rs.getInt("user_id");
                ids[1] = rs.getInt("book_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        return ids;
    }

    /*
     * User登録
     */
    public void saveUserToDB(String loginId, String password, String salt) {
        String sql = "INSERT INTO bookmap.users(login_id, hashed_password, salt) VALUES(?, ?, ?)";
        connect();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loginId);
            ps.setString(2, password);
            ps.setString(3, salt);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
    }
}
