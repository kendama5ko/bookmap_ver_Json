package window.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controller.ActionList;
import controller.ShowController;
import window.Window;
import window.sub.ManageBooks;

public class Classic extends Window {
    private static int todayProgress;
    private final JLabel bookTitleLabel;
    private final JLabel sumDaysAnsLabel;
    private final JLabel rPAnsLabel;
    private final JLabel avgPAnsLabel;
    private final DefaultTableModel progressModel;
    private final ShowController showC;
    private final JFrame frame;
    private final JLabel sumDaysLabel;
    private final JLabel remainPageLabel;
    private final JLabel avgPageLabel;
    private final JLabel todayPagesLabel;
    private final JLabel progressLabel;
    private final JTable progressDataTable;
    // private final JScrollPane booksScrollPane;
    private final JButton bookListButton;
    private final JButton logoButton;
    private final JButton inputButton;
    private final JButton topButton;
    private final JButton settingsButton;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JButton deleteButton;
    private final JButton changeUI;
    private final JTextField inputTodayPages;
    private final JProgressBar progressBar;
    private final JComboBox<String> bookShelfCombo;
    private final DefaultComboBoxModel<String> comboModel;
	protected int userId;

    private int bookId;
    private List<String> bookList;
    
    private ManageBooks mBooks;
    private GridBagConstraints gbc;
	private ActionList actionList;
    
    public Classic(int userId, int previousBookId) {

        this.frame = new JFrame();
        frame.setTitle("Book MAP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(1100, 100, 800, 700);

        var getP = frame.getContentPane();
        JPanel panel = new JPanel();

        this.userId = userId;
        this.bookId = previousBookId;
        showC = new ShowController(userId, bookId);
        actionList = new ActionList(this);
        Classic classic = this;
        
        GridBagLayout gbLayout = new GridBagLayout();
        panel.setLayout(gbLayout);
        gbc = new GridBagConstraints();

        // for (int i = 0; i < 6; i++) {
        // for (int j = 0; j < 12; j++) {
        // gbc.gridx = i;
        // gbc.gridy = j;
        // gbc.gridwidth = 1;
        // gbc.gridheight = 1;
        // gbc.anchor = GridBagConstraints.NORTHWEST;
        // JLabel lbl = new JLabel( ""
        // //"(" + i + ", " + j + ")"
        // );
        // gbLayout.setConstraints(lbl, gbc);
        // panel.add(lbl);
        // }
        // }

        /*
         * X == 4
         * X == 5
         */
        this.logoButton = new JButton("LOGO");
        logoButton.addActionListener(e -> {
            updateText(userId, bookId);
        });
        gbc.fill = GridBagConstraints.NONE;
        gridValue(0, 0, 1, 1);
        gbLayout.setConstraints(logoButton, gbc);
        panel.add(this.logoButton);

        this.bookTitleLabel = new JLabel(showC.getBookTitle(userId, bookId));
        bookTitleLabel.setFont(new Font("MS ゴシック", Font.PLAIN, 18));
        gridValue(4, 1, 2, 1);
        gbc.insets = new Insets(0, 20, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbLayout.setConstraints(bookTitleLabel, gbc);
        panel.add(this.bookTitleLabel);

        this.sumDaysLabel = new JLabel("読んだ日数");
        sumDaysLabel.setFont(new Font("MS ゴシック", Font.PLAIN, 18));
        // this.sumDaysLabel.setBackground(Color.green);
        this.sumDaysLabel.setOpaque(true);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gridValue(4, 2, 1, 1);
        gbLayout.setConstraints(sumDaysLabel, gbc);
        panel.add(this.sumDaysLabel);

        this.remainPageLabel = new JLabel("残りのページ");
        remainPageLabel.setFont(new Font("MS ゴシック", Font.PLAIN, 18));
        gridValue(4, 3, 1, 1);
        gbLayout.setConstraints(remainPageLabel, gbc);
        panel.add(this.remainPageLabel);

        this.avgPageLabel = new JLabel("1日の平均ページ");
        avgPageLabel.setFont(new Font("MS ゴシック", Font.PLAIN, 18));
        gbc.anchor = GridBagConstraints.WEST;
        gridValue(4, 4, 1, 1);
        gbLayout.setConstraints(avgPageLabel, gbc);
        panel.add(this.avgPageLabel);

        /*
         * Answer
         */
        this.sumDaysAnsLabel = new JLabel(showC.sumDays(userId, bookId) + "日");
        sumDaysAnsLabel.setFont(new Font("MS ゴシック", Font.BOLD, 30));
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gridValue(5, 2, 1, 1);
        gbc.insets = new Insets(0, 0, 0, 0);
        gbLayout.setConstraints(sumDaysAnsLabel, gbc);
        panel.add(this.sumDaysAnsLabel);

        this.rPAnsLabel = new JLabel(showC.remainPages(userId, bookId) + "P");
        rPAnsLabel.setFont(new Font("MS ゴシック", Font.BOLD, 30));
        gridValue(5, 3, 1, 1);
        gbLayout.setConstraints(rPAnsLabel, gbc);
        panel.add(this.rPAnsLabel);

        this.avgPAnsLabel = new JLabel(showC.average(userId, bookId) + "P");
        avgPAnsLabel.setFont(new Font("MS ゴシック", Font.BOLD, 30));
        gbc.anchor = GridBagConstraints.WEST;
        gridValue(5, 4, 1, 1);
        gbLayout.setConstraints(avgPAnsLabel, gbc);
        panel.add(this.avgPAnsLabel);

        /*
         * Table
         */
        progressModel = new DefaultTableModel();
        progressModel.addColumn("ページ数");
        progressModel.addColumn("日時");
        progressDataTable = new JTable(progressModel);
        List<String[]> tableData = new ArrayList<>();
        tableData = showC.progressData(userId, bookId);
        for (String[] row : tableData) {
            progressModel.addRow(row);
        }
        TableColumn pages = progressDataTable.getColumnModel().getColumn(0);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        // 各列に右寄せのレンダラーをセット
        progressDataTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        progressDataTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        pages.setPreferredWidth(40);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gridValue(4, 5, 1, 1);
        gbc.insets = new Insets(0, 15, 0, 15);
        gbLayout.setConstraints(progressDataTable, gbc);
        JScrollPane scrollPane = new JScrollPane(progressDataTable);
        scrollPane.setPreferredSize(new Dimension(170, 160));
        gbc.anchor = GridBagConstraints.NORTH;
        // panel.add(progressData.getTableHeader(), gbc);
        panel.add(scrollPane, gbc);
        // panel.add(progressData);

        /*
         * 削除ボタン
         * （実装予定）削除の確認のポップアップウィンドウ
         */
        this.deleteButton = new JButton("1件削除");
        deleteButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showC.deleteRecentData(userId, bookId);
                updateText(userId, bookId);
            }
        });
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.NONE;
        gridValue(4, 6, 1, 1);
        gbc.insets = new Insets(0, 15, 0, 15);
        gbLayout.setConstraints(this.deleteButton, gbc);
        panel.add(this.deleteButton);

        /*
         * Text Field-----------------------------------------------
         */

        this.todayPagesLabel = new JLabel("今日のページ数");
        todayPagesLabel.setFont(new Font("MS ゴシック", Font.PLAIN, 16));
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gridValue(4, 8, 1, 1);
        gbc.insets = new Insets(25, 10, 12, 0);
        // todayPageLabel.setPreferredSize(new Dimension(200, 50));
        gbLayout.setConstraints(todayPagesLabel, gbc);
        panel.add(this.todayPagesLabel);

        this.inputTodayPages = new JTextField("");
        inputTodayPages.setFont(new Font("MS ゴシック", Font.PLAIN, 16));
        this.inputTodayPages.setColumns(4);
        inputTodayPages.addKeyListener(new KeyListener() {
            // キーが半角数字でない場合、入力を無視する
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        inputTodayPages.setHorizontalAlignment(JTextField.CENTER);
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gridValue(5, 8, 1, 1);
        gbc.insets = new Insets(25, 10, 10, 25);
        gbLayout.setConstraints(inputTodayPages, gbc);
        panel.add(this.inputTodayPages);

        /*
         * Button--------------------------------------------------
         */
        this.inputButton = new JButton("入力");
        inputButton.setPreferredSize(new Dimension(60, 25));
        inputButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTodayPages.getText() == null) {
                    return;
                } else {
                    todayProgress = Integer.valueOf(inputTodayPages.getText());
                    showC.addRecentData(userId, bookId, todayProgress);
                }
                updateText(userId, bookId);
            }

        });

        gridValue(5, 8, 1, 1);
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbLayout.setConstraints(inputButton, gbc);
        panel.add(this.inputButton);

        this.topButton = new JButton("TOP");
        topButton.setPreferredSize(new Dimension(90, 25));
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gridValue(4, 10, 1, 1);
        gbc.insets = new Insets(10, 15, 5, 15);
        gbLayout.setConstraints(topButton, gbc);
        panel.add(this.topButton);

        this.settingsButton = new JButton("設定");
        settingsButton.setPreferredSize(new Dimension(90, 25));
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gridValue(5, 10, 1, 1);
        gbc.insets = new Insets(10, 15, 5, 15);
        gbLayout.setConstraints(settingsButton, gbc);
        // panel.add(this.settingsButton);

        this.previousButton = new JButton("前の本");
        previousButton.setPreferredSize(new Dimension(90, 25));
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gridValue(4, 11, 1, 1);
        gbc.insets = new Insets(5, 15, 15, 15);
        gbLayout.setConstraints(previousButton, gbc);
        panel.add(this.previousButton);

        this.nextButton = new JButton("次の本");
        nextButton.setPreferredSize(new Dimension(90, 25));
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gridValue(5, 11, 1, 1);
        gbc.insets = new Insets(5, 15, 15, 15);
        gbLayout.setConstraints(nextButton, gbc);
        panel.add(this.nextButton);

        /*
         * X == 2
         * X == 3
         */
        /*
         * /*
         * 本のリスト
         * JComboBox
         */
        bookList = showC.getBookList(userId, bookId);
        comboModel = new DefaultComboBoxModel<>();
        bookShelfCombo = new JComboBox<>(comboModel);
        for (String bl : bookList) {
            comboModel.addElement(bl);
        }
        bookShelfCombo.setSelectedIndex(-1);
        bookShelfCombo.setPreferredSize(new Dimension(190, 25));
        bookShelfCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String bookTitle;
                bookTitle = String.valueOf(bookShelfCombo.getSelectedItem());
                bookId = showC.getBookId(userId, bookTitle);
                updateText(userId, bookId);
            }
        });
        gridValue(2, 0, 2, 1);
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbLayout.setConstraints(bookShelfCombo, gbc);
        panel.add(this.bookShelfCombo);

        /*
         * 詳細ボタン
         * 本の追加と削除のウィンドウ表示
         */
        bookListButton = new JButton("詳細");
        bookListButton.setPreferredSize(new Dimension(65, 25));
        bookListButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	actionList.bookListButtonAction(userId, classic);
//                if (mBooks == null) {
//                    mBooks = new ManageBooks(userId, this);
//                }
//                mBooks.run();
//                mBooks.updateFrame(userId);
            }
        });
        gbc.anchor = GridBagConstraints.EAST;
        gbLayout.setConstraints(bookListButton, gbc);
        panel.add(this.bookListButton);

        /*
         * Progress Bar
         */
        int progress = (showC.progress(userId, bookId)); // 現在の達成率
        progressLabel = new JLabel();
        progressLabel.setText("<html><center>達成率<br>" + progress + "%</center></html>");
        progressLabel.setFont(new Font("MS ゴシック", Font.PLAIN, 26));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gridValue(2, 4, 2, 1);
        gbc.insets = new Insets(0, 0, 0, 0);
        gbLayout.setConstraints(progressLabel, gbc);
        panel.add(progressLabel);

        progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
        progressBar.setStringPainted(false);
        progressBar.setValue(progress);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gridValue(2, 2, 2, 8);
        gbc.insets = new Insets(50, 0, 0, 0);
        gbLayout.setConstraints(progressBar, gbc);
        panel.add(progressBar);

        changeUI = new JButton("UI");
        changeUI.setForeground(new Color(51, 153, 255));
        changeUI.setBackground(new Color(48, 48, 48));
        changeUI.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
                Window testUI = new TestUI(userId, bookId);
                testUI.run();
            }

        });
        GridBagConstraints gbc_changeUI = new GridBagConstraints();
        gbc_changeUI.anchor = GridBagConstraints.SOUTHWEST;
        gbc_changeUI.weighty = 1.0;
        gbc_changeUI.weightx = 1.0;
        gbc_changeUI.insets = new Insets(0, 10, 5, 5);
        gbc_changeUI.gridx = 5;
        gbc_changeUI.gridy = 10;
        panel.add(changeUI, gbc_changeUI);
        getP.add(panel);
    }

    public void gridValue(int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
    }

    public void run() {
        this.frame.setVisible(true);
    }

    public void stop() {
        this.frame.setVisible(false);
    }

    @Override
    public void updateText(int userId, int bookId) {
        bookTitleLabel.setText(showC.getBookTitle(userId, bookId));
        sumDaysAnsLabel.setText(showC.sumDays(userId, bookId) + "日");
        rPAnsLabel.setText(showC.remainPages(userId, bookId) + "P");
        avgPAnsLabel.setText(showC.average(userId, bookId) + "P");
        progressModel.setRowCount(0);
        List<String[]> tableData = new ArrayList<>();
        tableData = showC.progressData(userId, bookId);
        for (String[] row : tableData) {
            progressModel.addRow(row);
        }
    }



}
