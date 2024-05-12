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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
//import javax.swing.UIManager;
import javax.swing.table.TableColumn;

import controller.ActionList;
import controller.CalendarDialog;
import controller.MainFrameController;
import dao.JsonDAO;
import window.Window;

import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.toedter.calendar.JCalendar;

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

		/*
		 * 本のタイトル
		 */
		bookTitleLabel = new JLabel();
		bookTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		bookTitleLabel.setForeground(new Color(40, 40, 40));
		adjustableFontSize(bookTitle);

		GridBagConstraints gbc_bookTitleLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_bookTitleLabel, 0, 0, 1.0, 1.0);
		gbc_bookTitleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_bookTitleLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookTitleLabel.gridwidth = 2;
		panel.add(bookTitleLabel, gbc_bookTitleLabel);

		/*
		 * 管理ボタン
		 */
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
		mfc.setGridPosition(gbc_manageBookButton, 3, 0, 1.0, 1.0);
		gbc_manageBookButton.anchor = GridBagConstraints.EAST;
		gbc_manageBookButton.insets = new Insets(0, 0, 5, 5);
		panel.add(ManageBookButton, gbc_manageBookButton);

		/*
		 * 本のリスト
		 */
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
				BookInfo bookInfo = actionList.bookShelfComboAction(bookShelfCombo);
				String tempBookID = bookInfo.getID();
				String tempBookTitle = bookInfo.getTitle();

				// 本が選択されている時にupdateが行われた場合、引き続き選択する
				if (!tempBookID.equals("")) {
					bookID = tempBookID;
					bookTitle = tempBookTitle;
				}
				updateText(bookID);
			}
		});
		GridBagConstraints gbc_bookShelfCombo = new GridBagConstraints();
		mfc.setGridPosition(gbc_bookShelfCombo, 4, 0, 1.0, 1.0);
		gbc_bookShelfCombo.insets = new Insets(0, 5, 5, 5);
		gbc_bookShelfCombo.fill = GridBagConstraints.HORIZONTAL;
		panel.add(bookShelfCombo, gbc_bookShelfCombo);

		getP.add(panel, BorderLayout.CENTER);

		/*
		 * 注意メッセージラベル
		 */
		JLabel announceMessageLabel = new JLabel("進捗");
		announceMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		announceMessageLabel.setHorizontalTextPosition(JLabel.CENTER);
		announceMessageLabel.setOpaque(true);
		announceMessageLabel.setBackground(new Color(235, 245, 255));
		announceMessageLabel.setForeground(new Color(40, 40, 40));
		announceMessageLabel.setFont(new Font("メイリオ", Font.PLAIN, 12));

		GridBagConstraints gbc_announceMessageLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_announceMessageLabel, 2, 1, 1.0, 1.0);
		gbc_announceMessageLabel.insets = new Insets(5, 9, 0, 8);
		gbc_announceMessageLabel.gridwidth = 2;
		gbc_announceMessageLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_announceMessageLabel.anchor = GridBagConstraints.EAST;
		panel.add(announceMessageLabel, gbc_announceMessageLabel);



		/*
		 * 進捗テーブル
		 */
		progressModel = new DefaultTableModel();
		copyProgressModel = new DefaultTableModel();
		
		// modelにカラムを名前と共に追加
		String[] columnNameList = {"ページ数", "日付", "日付", "ID"};
		addColumn(copyProgressModel, columnNameList);
		addColumn(progressModel, columnNameList);

		// modelにデータを入れる
		progressModel = mfc.reloadProgressModel(bookID, progressModel);
		copyProgressModel = mfc.reloadProgressModel(bookID, copyProgressModel);		//更新されたかのチェック用
		
		progressModel.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					// 更新されたセルのrowとcolumnを取得
					int row = e.getFirstRow();
					int column = e.getColumn();
					
					// 更新するカラム名と行に応じたcreatedAt（一意となる主キー）を取得
					String columnName = progressModel.getColumnName(column);
					Long createdAtLong = (Long) progressModel.getValueAt(row, 2);

					// 入力されたデータを取得（incetanceはString型）
					Object editedDataObject = progressModel.getValueAt(row, column);
					// 入力される前のデータ（カラムによって型は違う）
					Object oldDataObject = copyProgressModel.getValueAt(row, column);
					
					boolean isChanged = false;
					// 中身がIntegerだった場合editedDataObject(中身はString)と比較することができないのでチェック
					if (oldDataObject instanceof Integer) {
						// editedDataObjectをObject→String→Integerに変換し比較
						isChanged = Integer.parseInt((String)editedDataObject) != (int)oldDataObject ? true : false;
					} else if (editedDataObject != oldDataObject) {
						isChanged = true;
					}

					// データが変更されていれば更新
					if (isChanged) {
						mfc.updateData(bookID, columnName, createdAtLong, editedDataObject);
					}
					updateText(bookID);
				}
			}
		});
		progressDataTable = new JTable(progressModel) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component toolTip = super.prepareRenderer(renderer, row, column);
				// controller.setDateTooltip(progressModel, toolTip, row, column);
				return toolTip;
			}
		};
		progressDataTable = mfc.progressDataTableSettings(progressDataTable);
		mfc.inputOnlyNumbers(progressDataTable, 0);

		// progressDataTable.getTableHeader().setFont(mainFont);
		JTableHeader progressDataHeader = progressDataTable.getTableHeader(); // changeFontでフォントを一括で変更するため
		panel.add(progressDataHeader);

		scrollPane = new JScrollPane(progressDataTable);
		scrollPane.setPreferredSize(new Dimension(150, 140));
		scrollPane.setBackground(new Color(225, 238, 251));
		

		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		mfc.setGridPosition(gbc_scrollPane, 2, 2, 1.0, 1.0);
		gbc_scrollPane.insets = new Insets(0, 10, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 2;
		panel.add(scrollPane, gbc_scrollPane);
		
		/*
		 * カレンダーの表示に関する設定
		 */
		calendarDialog = new CalendarDialog(progressDataTable);
		calendarDialog.onDateSelected(progressModel);
		calendarDialog.onDateColumnSelected();
		calendarDialog.openCalendarSetting();

		/*
		 * 進捗度
		 */
		progressLabel = new JLabel(mfc.setProgressLabel(bookID));
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setFont(new Font("メイリオ", Font.PLAIN, 34));
		progressLabel.setForeground(new Color(40, 40, 40));

		GridBagConstraints gbc_progressLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_progressLabel, 4, 4, 1.0, 1.0);
		gbc_progressLabel.insets = new Insets(0, 10, 5, 5);
		gbc_progressLabel.gridheight = 1;
		panel.add(progressLabel, gbc_progressLabel);

		/*
		 * 進捗バー
		 */
		progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
		progressBar.setValue(mfc.setProgress(bookID));
		progressBar.setBackground(new Color(250, 250, 255));
		progressBar.setForeground(new Color(40, 150, 243));
		progressBar.setFont(new Font("MS UI Gothic", Font.PLAIN, 36));

		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		mfc.setGridPosition(gbc_progressBar, 4, 2, 1.0, 1.0);
		gbc_progressBar.insets = new Insets(0, 5, 8, 5);
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.gridheight = 4;
		panel.add(progressBar, gbc_progressBar);

		/*
		 * ブックマークアイコン
		 */
		bookmarkIconLabel = new JLabel();
		ImageIcon bookIcon = new ImageIcon("lib/images/bookmark4.png");
		mfc.setOriginalIcon(bookmarkIconLabel, bookIcon, 60, 50);
		// bookmarkIconLabel.setBackground(new Color(235, 245, 255));
		// bookmarkIconLabel.setOpaque(true);

		GridBagConstraints gbc_bookmarkIconLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_bookmarkIconLabel, 0, 2, 1.0, 1.0);
		gbc_bookmarkIconLabel.insets = new Insets(0, 7, 0, 5);
		gbc_bookmarkIconLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookmarkIconLabel.anchor = GridBagConstraints.NORTHEAST;
		panel.add(bookmarkIconLabel, gbc_bookmarkIconLabel);

		/*
		 * 残りのページ数
		 */
		remainPagesLabel = new JLabel(mfc.setRemainPageLabel(bookID));
		remainPagesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		remainPagesLabel.setBackground(new Color(235, 245, 255));
		remainPagesLabel.setForeground(new Color(40, 40, 40));
		remainPagesLabel.setFont(new Font("メイリオ", Font.PLAIN, 38));
		remainPagesLabel.setOpaque(true);

		GridBagConstraints gbc_rPAnsLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_rPAnsLabel, 0, 2, 1.0, 1.0);
		gbc_rPAnsLabel.insets = new Insets(0, 7, 0, 5);
		gbc_rPAnsLabel.anchor = GridBagConstraints.NORTHEAST;
		gbc_rPAnsLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_rPAnsLabel.gridwidth = 2;
		panel.add(remainPagesLabel, gbc_rPAnsLabel);

		/*
		 * 平均ページ数アイコン
		 */
		averageIconLabel = new JLabel();
		ImageIcon averageIcon = new ImageIcon("lib/images/studying.png");
		mfc.setOriginalIcon(averageIconLabel, averageIcon, 42, 35);

		GridBagConstraints gbc_averageIconLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_averageIconLabel, 1, 4, 1.0, 1.0);
		gbc_averageIconLabel.insets = new Insets(0, 30, 4, 5);
		gbc_averageIconLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_averageIconLabel.anchor = GridBagConstraints.EAST;
		panel.add(averageIconLabel, gbc_averageIconLabel);

		/*
		 * 1日の平均ページ数
		 */
		avgPagesAnsLabel = new JLabel(mfc.setAvgPagesLabel(bookID));
		avgPagesAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		avgPagesAnsLabel.setBackground(new Color(235, 245, 255));
		avgPagesAnsLabel.setForeground(new Color(40, 40, 40));
		avgPagesAnsLabel.setFont(new Font("メイリオ", Font.PLAIN, 14));
		avgPagesAnsLabel.setOpaque(true);

		GridBagConstraints gbc_avgPagesAnsLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_avgPagesAnsLabel, 1, 4, 1.0, 1.0);
		gbc_avgPagesAnsLabel.insets = new Insets(0, 30, 7, 5);
		gbc_avgPagesAnsLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_avgPagesAnsLabel.anchor = GridBagConstraints.EAST;
		panel.add(avgPagesAnsLabel, gbc_avgPagesAnsLabel);

		/*
		 * カレンダーアイコン
		 */
		calendarIconLabel = new JLabel();
		ImageIcon calendarIcon = new ImageIcon("lib/images/calendar.png");
		mfc.setOriginalIcon(calendarIconLabel, calendarIcon, 50, 44);

		GridBagConstraints gbc_calendarIconLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_calendarIconLabel, 1, 5, 1.0, 1.0);
		gbc_calendarIconLabel.insets = new Insets(0, 28, 0, 5);
		gbc_calendarIconLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_calendarIconLabel.anchor = GridBagConstraints.EAST;
		panel.add(calendarIconLabel, gbc_calendarIconLabel);

		/*
		 * 残りの日数
		 */
		atThisPaceLabel = new JLabel(mfc.setAtThisPaceLabel(bookID));
		atThisPaceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		atThisPaceLabel.setHorizontalTextPosition(JLabel.RIGHT);
		atThisPaceLabel.setBackground(new Color(235, 245, 255));
		atThisPaceLabel.setForeground(new Color(40, 40, 40));
		atThisPaceLabel.setFont(new Font("メイリオ", Font.PLAIN, 14));
		atThisPaceLabel.setOpaque(true);

		GridBagConstraints gbc_atThisPaceLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_atThisPaceLabel, 1, 5, 1.0, 1.0);
		gbc_atThisPaceLabel.insets = new Insets(0, 30, 3, 5);
		gbc_atThisPaceLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_atThisPaceLabel.anchor = GridBagConstraints.EAST;
		panel.add(atThisPaceLabel, gbc_atThisPaceLabel);

		/*
		 * 読んだ日の合計日数（現在非表示）
		 */
		totalDaysAnsLabel = new JLabel("読んだ日数  " + mfc.getTotalDays(bookID) + "日");
		totalDaysAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		totalDaysAnsLabel.setForeground(new Color(150, 150, 150));
		totalDaysAnsLabel.setFont(new Font("メイリオ", Font.PLAIN, 15));

		GridBagConstraints gbc_totalDaysAnsLabel = new GridBagConstraints();
		mfc.setGridPosition(gbc_totalDaysAnsLabel, 1, 5, 1.0, 1.0);
		gbc_totalDaysAnsLabel.insets = new Insets(10, 0, 4, 5);
		gbc_totalDaysAnsLabel.fill = GridBagConstraints.BOTH;
		// panel.add(totalDaysAnsLabel, gbc_totalDaysAnsLabel);

		/*
		 * ページ数入力フィールド
		 */
		inputTodayPages = new JTextField();
		inputTodayPages.setForeground(new Color(40, 40, 40));
		inputTodayPages.setBackground(new Color(255, 255, 255));
		inputTodayPages.setCaretColor(new Color(0, 0, 0));
		inputTodayPages.setHorizontalAlignment(SwingConstants.RIGHT);
		inputTodayPages.setToolTipText("ここに読んだページ数を入力（数字のみ）");
		allowOnlyNumbers(inputTodayPages);

		GridBagConstraints gbc_inputTodayPages = new GridBagConstraints();
		mfc.setGridPosition(gbc_inputTodayPages, 2, 4, 1.0, 1.0);
		gbc_inputTodayPages.insets = new Insets(1, 50, 5, 5);
		gbc_inputTodayPages.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputTodayPages.ipady = 7;
		panel.add(inputTodayPages, gbc_inputTodayPages);

		/*
		 * 記録するボタン
		 */
		inputButton = new JButton("記録する");
		inputButton.setFont(new Font("メイリオ", Font.PLAIN, 11));
		inputButton.setToolTipText("左のフィールドに本日読んだページ数を入力してください");
		inputButton.setForeground(new Color(40, 40, 40));
		inputButton.setBackground(new Color(225, 238, 251));
		inputButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String inputtedText = inputTodayPages.getText();

				if (inputtedText == null || inputtedText.equals("")) {
					return;
				} else {
					todayProgress = Integer.valueOf(inputtedText);
					mfc.addRecentData(bookID, todayProgress);
					inputTodayPages.setText(null);
				}
				mfc.reloadProgressModel(bookID, progressModel);
				updateText(bookID);
			}

		});

		ActionListener[] inputButtonEvent = inputButton.getActionListeners(); // inputButtonのActionListenerの配列を入れる
		inputTodayPages.addActionListener(inputButtonEvent[0]);

		GridBagConstraints gbc_inputButton = new GridBagConstraints();
		mfc.setGridPosition(gbc_inputButton, 3, 4, 1.0, 1.0);
		gbc_inputButton.insets = new Insets(0, 0, 5, 5);
		gbc_inputButton.anchor = GridBagConstraints.EAST;
		gbc_inputButton.fill = GridBagConstraints.HORIZONTAL;
		panel.add(inputButton, gbc_inputButton);

		/*
		 * 削除ボタン
		 */
		deleteButton = new JButton("1件削除");
		deleteButton.setFont(new Font("メイリオ", Font.PLAIN, 11));
		deleteButton.setToolTipText("上の表から選んだデータを1件削除します");
		deleteButton.setBackground(new Color(225, 238, 251));
		deleteButton.setForeground(new Color(40, 40, 40));
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = progressDataTable.getSelectedRow();

				// 本当に削除しますか？のポップアップ
				// 必ずselectedRowを先に置く 理由:ポップアップが選択していなくても出るようになるため
				if (selectedRow != -1 && actionList.userAnswerIsYes()) {
					long createdAt = actionList.getCreatedAt(progressDataTable, selectedRow);

					// データの削除と表示の更新
					mfc.deleteSelectedData(bookID, createdAt);
					mfc.reloadProgressModel(bookID, progressModel);
					updateText(bookID);
				}
			}
		});
		GridBagConstraints gbc_deleteButton = new GridBagConstraints();
		mfc.setGridPosition(gbc_deleteButton, 3, 5, 1.0, 1.0);
		gbc_deleteButton.insets = new Insets(0, 0, 0, 5);
		gbc_deleteButton.anchor = GridBagConstraints.EAST;
		gbc_deleteButton.fill = GridBagConstraints.HORIZONTAL;
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
		copyProgressModel = mfc.reloadProgressModel(bookID, copyProgressModel);
		progressBar.setValue(mfc.setProgress(bookID));
		progressLabel.setText(mfc.setProgressLabel(bookID));
		panel.repaint();
		calendarDialog.dialogDispose();
	}
}
