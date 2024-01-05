package window.main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controller.ActionList;
import controller.ShowController;
import window.Window;
import window.sub.ManageBooks;

public class layouttest extends Window {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_1;
	private JFrame frame;
    private int bookId;
    private List<String> bookList;
    private ManageBooks mBooks;
    private final JButton bookListButton;
    private final JLabel bookTitleLabel;
    private final JLabel sumDaysAnsLabel;
    private final JLabel rPAnsLabel;
    private final JLabel avgPAnsLabel;
    private final DefaultTableModel progressModel;
    private final ShowController showC;
    int userId;
	/**
	 * Create the frame.
	 */
	public layouttest() {
		frame = new JFrame();
        frame.setTitle("Book MAP");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(1100, 100, 611, 700);
//		
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
//		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		var getP = frame.getContentPane();
		JPanel panel = new JPanel();
		panel.setBackground(new Color(48, 48, 48));
//		contentPane.add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		this.userId = 1;
		bookId = 1;
		showC = new ShowController(userId, bookId);
		layouttest laytest = this;
		ActionList actionList = new ActionList(this);
		
		bookListButton = new JButton("詳細");
		sl_panel.putConstraint(SpringLayout.WEST, bookListButton, 250, SpringLayout.WEST, panel);
        bookListButton.setPreferredSize(new Dimension(65, 25));
        bookListButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (mBooks == null) {
                    mBooks = new ManageBooks(userId, laytest);
                }
                mBooks.run();
                mBooks.updateFrame(userId);
            }
        });
		panel.add(bookListButton);
		
		JProgressBar progressBar = new JProgressBar();
		sl_panel.putConstraint(SpringLayout.SOUTH, bookListButton, -10, SpringLayout.NORTH, progressBar);
		sl_panel.putConstraint(SpringLayout.EAST, progressBar, -270, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.WEST, progressBar, 39, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, progressBar, 303, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, progressBar, -35, SpringLayout.SOUTH, panel);
		progressBar.setValue(30);
		progressBar.setStringPainted(true);
		progressBar.setOrientation(SwingConstants.VERTICAL);
		progressBar.setForeground(new Color(51, 153, 255));
		progressBar.setFont(new Font("MS UI Gothic", Font.PLAIN, 36));
		panel.add(progressBar);
		
		bookTitleLabel = new JLabel();
		sl_panel.putConstraint(SpringLayout.NORTH, bookTitleLabel, 31, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, bookTitleLabel, -295, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, bookTitleLabel, -585, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, bookTitleLabel, -36, SpringLayout.EAST, panel);
		bookTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		bookTitleLabel.setText(/*showC.getBookTitle(userId, bookId)*/"スッキリJava入門");
		bookTitleLabel.setForeground(new Color(51, 153, 255));
		bookTitleLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 30));
		panel.add(bookTitleLabel);
		
		rPAnsLabel = new JLabel(/*showC.remainPages(userId, bookId) +*/"<html><u>　" + "400P / " /*+ showC.totalPages(bookId) */+ "600P</u></html>");
		sl_panel.putConstraint(SpringLayout.NORTH, bookListButton, 120, SpringLayout.SOUTH, rPAnsLabel);
		sl_panel.putConstraint(SpringLayout.NORTH, rPAnsLabel, 25, SpringLayout.SOUTH, bookTitleLabel);
		sl_panel.putConstraint(SpringLayout.WEST, rPAnsLabel, -295, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, rPAnsLabel, -36, SpringLayout.EAST, panel);
		rPAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		rPAnsLabel.setForeground(new Color(51, 153, 255));
		rPAnsLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 40));
		rPAnsLabel.setBackground(UIManager.getColor("InternalFrame.inactiveBorderColor"));
		panel.add(rPAnsLabel);
		
		sumDaysAnsLabel = new JLabel(showC.sumDays(userId, bookId) + "日");
		sl_panel.putConstraint(SpringLayout.WEST, sumDaysAnsLabel, 471, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, sumDaysAnsLabel, -36, SpringLayout.EAST, panel);
		sumDaysAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		sumDaysAnsLabel.setForeground(new Color(51, 153, 255));
		sumDaysAnsLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		panel.add(sumDaysAnsLabel);
		
		avgPAnsLabel = new JLabel(showC.average(userId, bookId) + "P / day");
		sl_panel.putConstraint(SpringLayout.NORTH, sumDaysAnsLabel, 6, SpringLayout.SOUTH, avgPAnsLabel);
		sl_panel.putConstraint(SpringLayout.NORTH, avgPAnsLabel, 164, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, avgPAnsLabel, 417, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, avgPAnsLabel, -36, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, rPAnsLabel, -16, SpringLayout.NORTH, avgPAnsLabel);
		sl_panel.putConstraint(SpringLayout.SOUTH, avgPAnsLabel, -452, SpringLayout.SOUTH, panel);
		avgPAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		avgPAnsLabel.setForeground(new Color(51, 153, 255));
		avgPAnsLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		panel.add(avgPAnsLabel);
		
		bookList = showC.getBookList(userId, bookId);
        DefaultComboBoxModel<Object> comboModel = new DefaultComboBoxModel<>();
        JComboBox<Object> bookShelfCombo = new JComboBox<>(comboModel);
        sl_panel.putConstraint(SpringLayout.WEST, bookShelfCombo, 0, SpringLayout.WEST, progressBar);
        sl_panel.putConstraint(SpringLayout.EAST, bookShelfCombo, -351, SpringLayout.EAST, panel);
        sl_panel.putConstraint(SpringLayout.NORTH, bookShelfCombo, 268, SpringLayout.NORTH, panel);
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
		panel.add(bookShelfCombo);
		
		JButton btnNewButton_1 = new JButton("入力");
		sl_panel.putConstraint(SpringLayout.WEST, btnNewButton_1, -69, SpringLayout.EAST, bookTitleLabel);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewButton_1, 0, SpringLayout.EAST, bookTitleLabel);
		panel.add(btnNewButton_1);
		
		textField_1 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewButton_1, 17, SpringLayout.SOUTH, textField_1);
		sl_panel.putConstraint(SpringLayout.WEST, textField_1, -69, SpringLayout.EAST, bookTitleLabel);
		sl_panel.putConstraint(SpringLayout.SOUTH, textField_1, -145, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, textField_1, 0, SpringLayout.EAST, bookTitleLabel);
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("削除");
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewButton_2, 0, SpringLayout.NORTH, btnNewButton_1);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewButton_2, -30, SpringLayout.WEST, btnNewButton_1);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnNewButton_2);

		getP.add(panel, BorderLayout.CENTER);
		
		progressModel = new DefaultTableModel();
        progressModel.addColumn("ページ数");
        progressModel.addColumn("日時");
        JTable progressDataTable = new JTable(progressModel);
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
        
		
        JScrollPane scrollPane = new JScrollPane(progressDataTable);
        sl_panel.putConstraint(SpringLayout.EAST, bookListButton, -66, SpringLayout.WEST, scrollPane);
        sl_panel.putConstraint(SpringLayout.WEST, btnNewButton_2, 0, SpringLayout.WEST, scrollPane);
        sl_panel.putConstraint(SpringLayout.NORTH, textField_1, 12, SpringLayout.SOUTH, scrollPane);
        sl_panel.putConstraint(SpringLayout.SOUTH, sumDaysAnsLabel, -29, SpringLayout.NORTH, scrollPane);
        sl_panel.putConstraint(SpringLayout.WEST, scrollPane, -204, SpringLayout.EAST, panel);
        sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 274, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, -178, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, bookTitleLabel);
		panel.add(scrollPane);
	}

    public void run() {
        this.frame.setVisible(true);
    }

    public void updateText(int userId, int bookId) {
        bookTitleLabel.setText(showC.getBookTitle(userId, bookId));
        sumDaysAnsLabel.setText(showC.sumDays(userId, bookId) + "日");
        rPAnsLabel.setText(showC.remainPages(userId, bookId) + "P / " + showC.totalPages(bookId) + "P");
        avgPAnsLabel.setText(showC.average(userId, bookId) + "P");
        progressModel.setRowCount(0);
        List<String[]> tableData = new ArrayList<>();
        tableData = showC.progressData(userId, bookId);
        for (String[] row : tableData) {
            progressModel.addRow(row);
        }
    }

	@Override
	protected void stop() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
