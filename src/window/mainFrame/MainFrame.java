package window.mainFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
//import javax.swing.UIManager;

import controller.ActionList;
import controller.MainFrameController;
import window.Window;

public class MainFrame extends Window {

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		/*
		 * JFrame
		 */
		frame = new JFrame();
		frame.setTitle("Book MAP");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(1000, 200, 714, 381);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// ウィンドウが閉じるときに実行したいメソッドを呼び出す
				mfc.setlastBook(bookID, bookTitle);
			}
		});
		// try {
		// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		Container getP = frame.getContentPane();

		Window testUI = this;
		actionList = new ActionList(this);
		mfc = new MainFrameController();
		String lastBookInfo[] = new String[2];
		lastBookInfo = mfc.getLastBook();
		bookID = lastBookInfo[0];
		bookTitle = lastBookInfo[1];

		/*
		 * GridBagLayout
		 */
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 166, 165, 96, 84, 147, 19, 0 };
		gbl_panel.rowHeights = new int[] { 54, 21, 83, 36, 55, 25, 43 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0 };

		/*
		 * JPanel
		 */
		panel = new JPanel();
		panel.setBackground(new Color(250, 250, 250));
		panel.setLayout(gbl_panel);

		/*
		 * Component
		 */
		bookTitleLabel = new JLabel();
		bookTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		bookTitleLabel.setForeground(new Color(40, 40, 40));
		adjustableFontSize(bookTitle);

		GridBagConstraints gbc_bookTitleLabel = new GridBagConstraints();
		gbc_bookTitleLabel.gridwidth = 2;
		gbc_bookTitleLabel.weighty = 1.0;
		gbc_bookTitleLabel.weightx = 1.0;
		gbc_bookTitleLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookTitleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_bookTitleLabel.gridx = 0;
		gbc_bookTitleLabel.gridy = 0;
		panel.add(bookTitleLabel, gbc_bookTitleLabel);

		ManageBookButton = new JButton("管理");
		ManageBookButton.setFont(new Font("メイリオ", Font.BOLD, 11));
		ManageBookButton.setToolTipText("本の追加や削除");
		ManageBookButton.setForeground(new Color(40, 40, 40));
		ManageBookButton.setBackground(new Color(225, 238, 251));
		ManageBookButton.setPreferredSize(new Dimension(65, 25));
		ManageBookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionList.ManageBookButtonAction(testUI);
			}
		});

		GridBagConstraints gbc_manageBookButton = new GridBagConstraints();
		gbc_manageBookButton.anchor = GridBagConstraints.EAST;
		gbc_manageBookButton.weightx = 1.0;
		gbc_manageBookButton.weighty = 1.0;
		gbc_manageBookButton.insets = new Insets(0, 0, 5, 5);
		gbc_manageBookButton.gridx = 3;
		gbc_manageBookButton.gridy = 0;
		panel.add(ManageBookButton, gbc_manageBookButton);

		comboModel = mfc.setBookList();

		bookShelfCombo = new JComboBox<>(comboModel);
		bookShelfCombo.setForeground(new Color(40, 40, 40));
		bookShelfCombo.setBackground(new Color(250, 250, 255));
		bookShelfCombo.setSelectedIndex(-1);
		bookShelfCombo.setMaximumSize(new Dimension(130, 30));
		bookShelfCombo.setMinimumSize(new Dimension(130, 30));
		bookShelfCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bookTitle = String.valueOf(bookShelfCombo.getSelectedItem());
				BookInfo selectedBook = (BookInfo) bookShelfCombo.getSelectedItem();
				// JComboBoxが未選択(null)でないことを確認してからIDを取得
				if (selectedBook != null) {
					bookID = selectedBook.getID();
					bookTitle = selectedBook.getTitle();
				} else {
					bookTitle = "";
				}
				updateText(bookID);
			}
		});
		GridBagConstraints gbc_bookShelfCombo = new GridBagConstraints();
		gbc_bookShelfCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookShelfCombo.weighty = 1.0;
		gbc_bookShelfCombo.weightx = 1.0;
		gbc_bookShelfCombo.insets = new Insets(0, 5, 5, 5);
		gbc_bookShelfCombo.gridx = 4;
		gbc_bookShelfCombo.gridy = 0;
		panel.add(bookShelfCombo, gbc_bookShelfCombo);

		getP.add(panel, BorderLayout.CENTER);

		progressModel = new DefaultTableModel();
		progressModel.addColumn("ページ数");
		progressModel.addColumn("日付");
		progressModel.addColumn("日付");
		progressModel.addColumn("ID");
		progressModel = mfc.reloadProgressModel(bookID, progressModel);
		progressDataTable = new JTable(progressModel) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component toolTip = super.prepareRenderer(renderer, row, column);
				// controller.setDateTooltip(progressModel, toolTip, row, column);
				return toolTip;
			}
		};
		progressDataTable = mfc.progressDataTableSettings(progressDataTable);
		// progressDataTable.getTableHeader().setFont(mainFont);
		JTableHeader progressDataHeader = progressDataTable.getTableHeader(); // changeFontでフォントを一括で変更するため
		panel.add(progressDataHeader);

		scrollPane = new JScrollPane(progressDataTable);
		scrollPane.setPreferredSize(new Dimension(150, 140));
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		scrollPane.setBackground(new Color(225, 238, 251));
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.weighty = 1.0;
		gbc_scrollPane_1.weightx = 1.0;
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.insets = new Insets(0, 10, 5, 5);
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 2;
		panel.add(scrollPane, gbc_scrollPane_1);

		progressLabel = new JLabel(mfc.setProgressLabel(bookID));
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setFont(new Font("メイリオ", Font.PLAIN, 34));
		progressLabel.setForeground(new Color(40, 40, 40));
		GridBagConstraints gbc_progressLabel = new GridBagConstraints();
		gbc_progressLabel.weighty = 1.0;
		gbc_progressLabel.weightx = 1.0;
		gbc_progressLabel.gridheight = 1;
		gbc_progressLabel.insets = new Insets(0, 10, 5, 5);
		gbc_progressLabel.gridx = 4;
		gbc_progressLabel.gridy = 4;
		panel.add(progressLabel, gbc_progressLabel);

		progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
		progressBar.setValue(mfc.setProgress(bookID));
		progressBar.setBackground(new Color(250, 250, 255));
		progressBar.setForeground(new Color(40, 150, 243));
		progressBar.setFont(new Font("MS UI Gothic", Font.PLAIN, 36));
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.weighty = 1.0;
		gbc_progressBar.weightx = 1.0;
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.gridheight = 4;
		gbc_progressBar.insets = new Insets(0, 5, 8, 5);
		gbc_progressBar.gridx = 4;
		gbc_progressBar.gridy = 2;
		panel.add(progressBar, gbc_progressBar);

		bookmarkIconLabel = new JLabel();
		ImageIcon bookIcon = new ImageIcon("lib/images/bookmark4.png");
		mfc.setOriginalIcon(bookmarkIconLabel, bookIcon, 60, 50);
		// bookmarkIconLabel.setBackground(new Color(235, 245, 255));
		// bookmarkIconLabel.setOpaque(true);
		GridBagConstraints gbc_bookmarkIconLabel = new GridBagConstraints();
		gbc_bookmarkIconLabel.weightx = 1.0;
		gbc_bookmarkIconLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookmarkIconLabel.anchor = GridBagConstraints.NORTHEAST;
		gbc_bookmarkIconLabel.insets = new Insets(0, 7, 0, 5);
		gbc_bookmarkIconLabel.gridx = 0;
		gbc_bookmarkIconLabel.gridy = 2;
		panel.add(bookmarkIconLabel, gbc_bookmarkIconLabel);

		remainPagesLabel = new JLabel(mfc.setRemainPageLabel(bookID));
		remainPagesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		remainPagesLabel.setBackground(new Color(235, 245, 255));
		remainPagesLabel.setOpaque(true);
		remainPagesLabel.setForeground(new Color(40, 40, 40));
		remainPagesLabel.setFont(new Font("メイリオ", Font.PLAIN, 38));
		GridBagConstraints gbc_rPAnsLabel = new GridBagConstraints();
		gbc_rPAnsLabel.weightx = 1.0;
		gbc_rPAnsLabel.anchor = GridBagConstraints.NORTHEAST;
		gbc_rPAnsLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_rPAnsLabel.insets = new Insets(0, 7, 0, 5);
		gbc_rPAnsLabel.gridwidth = 2;
		gbc_rPAnsLabel.gridx = 0;
		gbc_rPAnsLabel.gridy = 2;
		panel.add(remainPagesLabel, gbc_rPAnsLabel);

		averageIconLabel = new JLabel();
		ImageIcon averageIcon = new ImageIcon("lib/images/studying.png");
		mfc.setOriginalIcon(averageIconLabel, averageIcon, 42, 35);
		GridBagConstraints gbc_averageIconLabel = new GridBagConstraints();
		gbc_averageIconLabel.weightx = 1.0;
		gbc_averageIconLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_averageIconLabel.anchor = GridBagConstraints.EAST;
		gbc_averageIconLabel.insets = new Insets(0, 30, 4, 5);
		gbc_averageIconLabel.gridx = 1;
		gbc_averageIconLabel.gridy = 4;
		panel.add(averageIconLabel, gbc_averageIconLabel);

		avgPagesAnsLabel = new JLabel(mfc.setAvgPagesLabel(bookID));
		avgPagesAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		avgPagesAnsLabel.setBackground(new Color(235, 245, 255));
		avgPagesAnsLabel.setOpaque(true);
		avgPagesAnsLabel.setForeground(new Color(40, 40, 40));
		avgPagesAnsLabel.setFont(new Font("メイリオ", Font.PLAIN, 14));
		GridBagConstraints gbc_avgPagesAnsLabel = new GridBagConstraints();
		gbc_avgPagesAnsLabel.weightx = 1.0;
		gbc_avgPagesAnsLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_avgPagesAnsLabel.anchor = GridBagConstraints.EAST;
		gbc_avgPagesAnsLabel.insets = new Insets(0, 30, 7, 5);
		gbc_avgPagesAnsLabel.gridx = 1;
		gbc_avgPagesAnsLabel.gridy = 4;
		panel.add(avgPagesAnsLabel, gbc_avgPagesAnsLabel);

		calendarIconLabel = new JLabel();
		ImageIcon calendarIcon = new ImageIcon("lib/images/calendar.png");
		mfc.setOriginalIcon(calendarIconLabel, calendarIcon, 50, 44);
		GridBagConstraints gbc_calendarIconLabel = new GridBagConstraints();
		gbc_calendarIconLabel.weightx = 1.0;
		gbc_calendarIconLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_calendarIconLabel.anchor = GridBagConstraints.EAST;
		gbc_calendarIconLabel.insets = new Insets(0, 28, 0, 5);
		gbc_calendarIconLabel.gridx = 1;
		gbc_calendarIconLabel.gridy = 5;
		panel.add(calendarIconLabel, gbc_calendarIconLabel);

		atThisPaceLabel = new JLabel(mfc.setAtThisPaceLabel(bookID));
		atThisPaceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		atThisPaceLabel.setHorizontalTextPosition(JLabel.RIGHT);
		atThisPaceLabel.setBackground(new Color(235, 245, 255));
		atThisPaceLabel.setOpaque(true);
		atThisPaceLabel.setForeground(new Color(40, 40, 40));
		atThisPaceLabel.setFont(new Font("メイリオ", Font.PLAIN, 14));
		GridBagConstraints gbc_atThisPaceLabel = new GridBagConstraints();
		gbc_atThisPaceLabel.weightx = 1.0;
		gbc_atThisPaceLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_atThisPaceLabel.anchor = GridBagConstraints.EAST;
		gbc_atThisPaceLabel.insets = new Insets(0, 30, 3, 5);
		gbc_atThisPaceLabel.gridx = 1;
		gbc_atThisPaceLabel.gridy = 5;
		panel.add(atThisPaceLabel, gbc_atThisPaceLabel);

		totalDaysAnsLabel = new JLabel("読んだ日数  " + mfc.getTotalDays(bookID) + "日");
		totalDaysAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		totalDaysAnsLabel.setForeground(new Color(150, 150, 150));
		totalDaysAnsLabel.setFont(new Font("メイリオ", Font.PLAIN, 15));
		GridBagConstraints gbc_totalDaysAnsLabel = new GridBagConstraints();
		gbc_totalDaysAnsLabel.weightx = 1.0;
		gbc_totalDaysAnsLabel.fill = GridBagConstraints.BOTH;
		gbc_totalDaysAnsLabel.insets = new Insets(10, 0, 4, 5);
		gbc_totalDaysAnsLabel.gridx = 1;
		gbc_totalDaysAnsLabel.gridy = 5;
		// panel.add(totalDaysAnsLabel, gbc_totalDaysAnsLabel);

		inputTodayPages = new JTextField();
		inputTodayPages.setForeground(new Color(40, 40, 40));
		inputTodayPages.setBackground(new Color(255, 255, 255));
		inputTodayPages.setCaretColor(new Color(0, 0, 0));
		inputTodayPages.setHorizontalAlignment(SwingConstants.RIGHT);
		inputTodayPages.setToolTipText("ここに読んだページ数を入力（数字のみ）");
		allowOnlyNumbers(inputTodayPages);
		GridBagConstraints gbc_inputTodayPages = new GridBagConstraints();
		gbc_inputTodayPages.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputTodayPages.weighty = 1.0;
		gbc_inputTodayPages.weightx = 1.0;
		gbc_inputTodayPages.insets = new Insets(1, 50, 5, 5);
		gbc_inputTodayPages.ipady = 7;
		gbc_inputTodayPages.gridx = 2;
		gbc_inputTodayPages.gridy = 4;
		panel.add(inputTodayPages, gbc_inputTodayPages);

		inputButton = new JButton("記録する");
		inputButton.setFont(new Font("メイリオ", Font.PLAIN, 11));
		inputButton.setToolTipText("左のフィールドに本日読んだページ数を入力してください");
		inputButton.setForeground(new Color(40, 40, 40));
		inputButton.setBackground(new Color(225, 238, 251));
		inputButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputTodayPages.getText() == null || inputTodayPages.getText().equals("")) {
					return;
				} else {
					todayProgress = Integer.valueOf(inputTodayPages.getText());
					mfc.addRecentData(bookID, bookId, todayProgress);
					inputTodayPages.setText(null);
				}
				mfc.reloadProgressModel(bookID, progressModel);
				updateText(bookID);
			}

		});

		ActionListener[] inputButtonEvent = inputButton.getActionListeners(); // inputButtonのActionListenerの配列を入れる
		inputTodayPages.addActionListener(inputButtonEvent[0]);
		GridBagConstraints gbc_inputButton = new GridBagConstraints();
		gbc_inputButton.anchor = GridBagConstraints.EAST;
		gbc_inputButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputButton.weighty = 1.0;
		gbc_inputButton.weightx = 1.0;
		gbc_inputButton.insets = new Insets(0, 0, 5, 5);
		gbc_inputButton.gridx = 3;
		gbc_inputButton.gridy = 4;
		panel.add(inputButton, gbc_inputButton);

		deleteButton = new JButton("1件削除");
		deleteButton.setFont(new Font("メイリオ", Font.PLAIN, 11));
		deleteButton.setToolTipText("上の表から選んだデータを1件削除します");
		deleteButton.setBackground(new Color(225, 238, 251));
		deleteButton.setForeground(new Color(40, 40, 40));
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = progressDataTable.getSelectedRow();
				if (selectedRow != -1) {

					// 選択された行からcreatedAtを取得
					long createdAt = (long) progressDataTable.getValueAt(selectedRow, 2);

					// 本当に削除しますか？のポップアップ
					int userAnswer = JOptionPane.showConfirmDialog(null,
							"選択された進捗データを本当に削除しますか？", "注意", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (userAnswer == JOptionPane.YES_OPTION) {
						mfc.deleteSelectedData(bookID, createdAt);
					}
					mfc.reloadProgressModel(bookID, progressModel);
					updateText(bookID);
				}
			}
		});
		GridBagConstraints gbc_deleteButton = new GridBagConstraints();
		gbc_deleteButton.anchor = GridBagConstraints.EAST;
		gbc_deleteButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_deleteButton.weighty = 1.0;
		gbc_deleteButton.weightx = 1.0;
		gbc_deleteButton.insets = new Insets(0, 0, 0, 5);
		gbc_deleteButton.gridx = 3;
		gbc_deleteButton.gridy = 5;
		panel.add(deleteButton, gbc_deleteButton);

		Font mainFont = new Font("メイリオ", Font.PLAIN, 12);
		mfc.changeFont(panel, mainFont);

	}

	public void run() {
		this.frame.setVisible(true);
	}

	public void stop() {
		this.frame.setVisible(false);
	}

	public void adjustableFontSize(String bookTitle) {
		bookTitleLabel.setText(bookTitle);
		bookTitleLabel.setToolTipText(bookTitle);
		if (bookTitle == null) {
			bookTitleLabel.setText("");
			return;
		}
		int textLength = bookTitle.length();
		int fontSize;

		if (textLength <= 5) {
			fontSize = 30;
		} else if (textLength <= 12) {
			fontSize = 26;
		} else if (textLength <= 16) {
			fontSize = 22;
		} else if (textLength <= 25) {
			fontSize = 18;
		} else {
			fontSize = 14;
		}
		bookTitleLabel.setFont(new Font("メイリオ", Font.PLAIN, fontSize));
	}

	public void updateText(String bookID) {
		adjustableFontSize(bookTitle);
		remainPagesLabel.setText(mfc.setRemainPageLabel(bookID));
		avgPagesAnsLabel.setText(mfc.setAvgPagesLabel(bookID));
		atThisPaceLabel.setText(mfc.setAtThisPaceLabel(bookID));
		totalDaysAnsLabel.setText("読んだ日数  " + mfc.getTotalDays(bookID) + "日");
		progressModel = mfc.reloadProgressModel(bookID, progressModel);
		progressBar.setValue(mfc.setProgress(bookID));
		progressLabel.setText(mfc.setProgressLabel(bookID));
		panel.repaint();
	}
}
