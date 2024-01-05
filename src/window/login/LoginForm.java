package window.login;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.PasswordEncoder;
import window.main.Classic;
import window.main.TestUI;

public class LoginForm {
    private final JFrame loginForm;
    private JLabel userName;
    private JLabel password;
    private JLabel messageLabel;
    private JTextField loginIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel loginPanel;
    private JButton rootLoginButton;
    private JButton subscribeButton;
    Classic classic;
    private int[] userData = new int[2];
    private int userId;
    private int previousBookId;
    PasswordEncoder passEnc = new PasswordEncoder();

    public LoginForm() {
        /*
         * JFrame
         */
        loginForm = new JFrame();
        loginForm.setTitle("Please Login");
        loginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginForm.setBounds(700, 300, 525, 310);
        loginForm.setResizable(false);

        /*
         * JPanel
         */
        Container getP = loginForm.getContentPane();
        loginPanel = new JPanel();
        loginPanel.setBackground(new Color(250, 250, 250));
        loginPanel.setLayout(null);

        /*
         * JLabel
         */
        userName = new JLabel("LOGIN ID");
        userName.setForeground(Color.BLACK);
        userName.setFont(new Font("メイリオ", Font.PLAIN, 19));
        userName.setBounds(60, 70, 193, 52);
        loginPanel.add(userName);

        password = new JLabel("PASSWORD");
        password.setForeground(Color.BLACK);
        password.setFont(new Font("メイリオ", Font.PLAIN, 19));
        password.setBounds(60, 120, 193, 52);
        loginPanel.add(password);

        messageLabel = new JLabel("welcome to Bookmap");
        messageLabel.setFont(new Font("メイリオ", Font.PLAIN, 13));
        messageLabel.setBounds(0, 230, 500, 52);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        loginPanel.add(messageLabel);

        /*
         * JButton
         */
        subscribeButton = new JButton("Sign up");
        subscribeButton.setFont(new Font("メイリオ", Font.PLAIN, 12));
        subscribeButton.setForeground(new Color(40, 40, 40));
        subscribeButton.setBackground(new Color(225, 238, 251));
        subscribeButton.setBounds(280, 180, 80, 30);
        subscribeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String message = null;
                String loginId = loginIdField.getText().stripTrailing();
                if (loginId.isEmpty() || loginId == null) {
                    message = "LoginIDは1文字以上でお願いします。";

                } else if (passEnc.isUsed(loginId)) {
                    message = "既に存在するIDです。別のIDへの変更をお願いします。";
                } else {
                    message = verifyPassword();
                }
                messageLabel.setText(message);
            }
        });

        loginPanel.add(subscribeButton);

        rootLoginButton = new JButton("root");
        rootLoginButton.setFont(new Font("メイリオ", Font.PLAIN, 12));
        rootLoginButton.setForeground(new Color(40, 40, 40));
        rootLoginButton.setBackground(new Color(225, 238, 251));
        rootLoginButton.setBounds(415, 230, 85, 30);
        rootLoginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Window window = new Window(1,1); //デバッグ用 後で登録ボタンに変更
                // window.run();
                loginForm.setVisible(false);
                TestUI testUI = new TestUI(1, 1);
                testUI.run();
            }
        });
        loginPanel.add(rootLoginButton);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("メイリオ", Font.BOLD, 12));
        loginButton.setForeground(new Color(40, 40, 40));
        loginButton.setBackground(new Color(225, 238, 251));
        loginButton.setBounds(180, 180, 80, 30);
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PasswordEncoder passEnc = new PasswordEncoder();

                // フィールドのテキストを変数に
                String loginId = loginIdField.getText().stripTrailing(); // 末尾の空白を全て削除
                char[] passwordArray = passwordField.getPassword();
                String password = new String(passwordArray);

                // textareaに文字が入力されているかの確認
                if (!loginId.isEmpty()) {
                    // userCheck()にloginIdとpasswordを渡して照合
                    boolean userIsVerified = passEnc.userCheck(loginId, password);
                    if (userIsVerified) {
                        System.out.println("ログインしました。");
                        userData = passEnc.getUserData(loginId);
                        login(loginId);
                    } else {
                        System.out.println("IDまたはパスワードが間違っています。");
                    }
                } else {
                    System.out.println("ログインIDを入力してください");
                }
            }
        });
        loginPanel.add(loginButton);
        getP.add(loginPanel);

        
//        Font allFont = new Font("メイリオ", Font.PLAIN, 12);
//        passEnc.changeFont(loginPanel, allFont);
        /*
         * JTextField
         */
        loginIdField = new JTextField();
        loginIdField.setFont(new Font("Tahoma", Font.PLAIN, 21));
        loginIdField.setBounds(180, 77, 180, 35);
        loginPanel.add(loginIdField);

        passwordField = new JPasswordField();
        ActionListener[] loginEvent = loginButton.getActionListeners(); // loginButtonのActionListenerの配列を入れる
        passwordField.addActionListener(loginEvent[0]);
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 21));
        passwordField.setBounds(180, 127, 180, 35);
        loginPanel.add(passwordField);
        
    }

    public String verifyPassword() {
        String message = null;
        char[] passwordArray = passwordField.getPassword();
        String password = new String(passwordArray);
        String inputPassword = JOptionPane.showInputDialog(loginForm, "もう一度同じパスワードを入力してください", "パスワードの確認",
                JOptionPane.QUESTION_MESSAGE);
        if (inputPassword == null || inputPassword.isEmpty()) {
            // 何もせずにウィンドウを閉じるだけ
        	
        } else if (inputPassword.equals(password)) {
            message = "登録されました。";
            passEnc.subscribe(loginIdField.getText(), password);
            login(loginIdField.getText());
        } else if (!inputPassword.equals(password)) {
            message = "パスワードが違います";
        }
        return message;
    }

    public void login(String loginId) {
        userData = passEnc.getUserData(loginId);
        userId = userData[0];
        previousBookId = userData[1];
        loginForm.setVisible(false);
        TestUI testUI = new TestUI(userId, previousBookId);
        testUI.run();
    }

    public void run() {
        loginForm.setVisible(true);
    }

}
