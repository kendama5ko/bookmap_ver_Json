package controller;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.swing.JComponent;

import dao.UsersDAO;
import dto.UsersDTO;
import entity.UsersBean;

public class PasswordEncoder {
    private UsersDTO udto;
    private UsersDAO udao;
    private UsersBean ub;

    public static String sha256Encode(String password, String salt) {
        int iterateCount = 10000;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // パスワードとソルトを結合
            String preparedHash = password + salt;

            // byteに変換しハッシュ化
            byte[] hashedPassword = preparedHash.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < iterateCount; i++) {
                md.reset();
                hashedPassword = md.digest(hashedPassword);
            }
            // ハッシュ値を16進数の文字列に変換
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedPassword) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * salt
     */
    public static String generateSalt() {
        byte[] salt = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return byteToHexString(salt);
    }

    // // hashingメソッド内でsaltを生成せず、呼び出し元で生成してそれを引数としてhashingメソッドに渡す方が管理しやすい？
    // public static String hashing(String password) {
    // String salt = generateSalt();
    // return sha256Encode(password, salt);
    // }

    /*
     * ログイン
     */
    public boolean userCheck(String loginId, String password) {
        boolean user = false;
        ub = new UsersBean();
        udao = new UsersDAO();
        // udto = new UserAccountsDTO();
        try {
            // ログインIDでsqlを検索してuserに代入
            udto = udao.selectPassWithSalt(loginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // beanにデータをセット
        for (int i = 0; i < udto.size(); i++) {
            ub = udto.get(i);
        }
        // ログインIDの存在をチェック
        if (ub.getSalt() == null) {
            System.out.println("ログインIDが存在しません。");
        } else if (ub != null) {
            // beanからpasswordを取り出し、入力されたpasswordと照合
            String hashedPassword = ub.getHashedPassword();
            String salt = ub.getSalt();

            // 入力されたpasswordをsaltを使いhash化
            String inputPassword = sha256Encode(password, salt);

            // passwordと照合
            if (inputPassword.equals(hashedPassword)) {
                user = true;
            }
        }
        return user;
    }

    /*
     * userNameとハッシュ化したpasswordをデータベースに保存
     * 
     * （未実装）登録する前にselect文でユーザーIDを検索し、存在したら警告
     * 
     */
    public void subscribe(String loginId, String password) {
        String salt = generateSalt();
        String hashedPassword = sha256Encode(password, salt);
        udao = new UsersDAO();
        // user_idで検索して、idが存在しなければsaveUserToDBを実行しないようにする

        udao.saveUserToDB(loginId, hashedPassword, salt);
    }

    /*
     * byteをhexに変換（可読性を上げるため）
     */
    private static String byteToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    /*
     * loginIdからuserIdとprogressテーブルで最新データのbookIdを返す
     */
    public int[] getUserData(String loginId) {
        udao = new UsersDAO();
        return udao.searchIds(loginId);
    }
    public boolean isUsed(String loginId) {
        udao = new UsersDAO();
        boolean isUsed = 0 < udao.countloginId(loginId);
        return isUsed;
    }


    /*
     * *****よく確認してから実装すること*****
     * パスワードのハッシュ化の方式を変更する時に
     * これまでのユーザーの認証を確認してから、新しいパスワードの取り扱い方で新たにupdateする
     */
    public boolean changeHashSpec(String loginId, String password, String salt) {
        boolean user = false;
        int userId = 100000;
        // userIdの値をloginIdからDAOを呼び出し設定する

        user = userCheck(loginId, password);
        // userIdが変更加えた時の最大値以下であれば新たな方式へのupdate処理、最大値より上であれば通常の登録処理
        if (userId <= 100 && user) {
            /*
             * ここに変更するパスワードのハッシュ化を入力
             * 
             * ********************重要**********************************
             * 新しいpasswordとsaltと共にuser_idもupdateする
             * user_idは主キーなので、
             * 101と設定すれば自動でINCREMENTし、最大値になってくれる？(要確認)
             * **********************************************************
             */
        } else if (userId > 100 && user) {
            System.out.println("ログインしました");
        } else {    //万が一を考えて else if (user == false) にするべき？
            System.out.println("IDまたはパスワードが間違っています。");
        }
        return user;
    }
    
    /*
     * 各Controllerクラスに存在するのでまとめる必要あり
     */
	public void changeFont(JComponent component, Font font) {
	       component.setFont(font);

	   if (component instanceof Container) {
	       for (Component child : ((Container) component).getComponents()) {
	    	   if (child instanceof JComponent) {
	    		   changeFont((JComponent) child, font);
	    	   }
	       }
	   }
	}
}
