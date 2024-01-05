package window.main;

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
import java.util.Arrays;
import java.util.List;

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
import javax.swing.table.TableCellRenderer;

import controller.ActionList;
import controller.Controller;
import window.Window;

public class TestUI extends Window {

	/**
	 * Create the frame.
	 */
	public TestUI(int userId, int previousBookId) {
        /*
         * JFrame
         */
		frame = new JFrame();
		frame.setTitle("Book MAP");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(1000, 200, 714, 381);

		Container getP = frame.getContentPane();
		this.userId = userId;
		this.bookId = previousBookId;

		Window testUI = this;
		actionList = new ActionList(this);
		controller = new Controller();


		/*
		 * GridBagLayout
		 */
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 70, 261, 96, 84, 147, 19, 0 };
		gbl_panel.rowHeights = new int[] { 54, 21, 83, 55, 36, 25, 43 };
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
		adjustableFontSize(userId, bookId);
		bookTitleLabel.setForeground(new Color(40, 40, 40));

		GridBagConstraints gbc_bookTitleLabel = new GridBagConstraints();
		gbc_bookTitleLabel.gridwidth = 2;
		gbc_bookTitleLabel.weighty = 1.0;
		gbc_bookTitleLabel.weightx = 1.0;
		gbc_bookTitleLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookTitleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_bookTitleLabel.gridx = 0;
		gbc_bookTitleLabel.gridy = 0;
		panel.add(bookTitleLabel, gbc_bookTitleLabel);

		bookListButton = new JButton("管理");
		bookListButton.setFont(new Font("メイリオ", Font.BOLD, 11));
		bookListButton.setToolTipText("本の追加や削除");
		bookListButton.setForeground(new Color(40, 40, 40));
		bookListButton.setBackground(new Color(225, 238, 251));
		bookListButton.setPreferredSize(new Dimension(65, 25));
		bookListButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionList.bookListButtonAction(userId, testUI);
			}
		});

		GridBagConstraints gbc_bookListButton = new GridBagConstraints();
		gbc_bookListButton.anchor = GridBagConstraints.EAST;
		gbc_bookListButton.weightx = 1.0;
		gbc_bookListButton.weighty = 1.0;
		gbc_bookListButton.insets = new Insets(0, 0, 5, 5);
		gbc_bookListButton.gridx = 3;
		gbc_bookListButton.gridy = 0;
		panel.add(bookListButton, gbc_bookListButton);

		comboModel = controller.setBookList(userId);
		bookShelfCombo = new JComboBox<>(comboModel);
		bookShelfCombo.setForeground(new Color(40, 40, 40));
		bookShelfCombo.setBackground(new Color(250, 250, 255));
		bookShelfCombo.setSelectedIndex(-1);
		bookShelfCombo.setMinimumSize(new Dimension(130, 30));
		bookShelfCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String bookTitle = String.valueOf(bookShelfCombo.getSelectedItem());
				bookId = controller.getBookId(userId, bookTitle);
				updateText(userId, bookId);
			}
		});
		GridBagConstraints gbc_bookShelfCombo = new GridBagConstraints();
		gbc_bookShelfCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_bookShelfCombo.weighty = 1.0;
		gbc_bookShelfCombo.weightx = 1.0;
		gbc_bookShelfCombo.insets = new Insets(0, 10, 5, 5);
		gbc_bookShelfCombo.gridx = 4;
		gbc_bookShelfCombo.gridy = 0;
		panel.add(bookShelfCombo, gbc_bookShelfCombo);

		remainPagesLabel = new JLabel(controller.setRemainPageLabel(userId, bookId));
		remainPagesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		remainPagesLabel.setBackground(new Color(235, 245, 255));
		remainPagesLabel.setOpaque(true);
		remainPagesLabel.setForeground(new Color(40, 40, 40));
		remainPagesLabel.setFont(new Font("メイリオ", Font.PLAIN, 38));
		GridBagConstraints gbc_rPAnsLabel = new GridBagConstraints();
		gbc_rPAnsLabel.weightx = 1.0;
		gbc_rPAnsLabel.anchor = GridBagConstraints.NORTHEAST;
		gbc_rPAnsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_rPAnsLabel.gridx = 1;
		gbc_rPAnsLabel.gridy = 2;
		panel.add(remainPagesLabel, gbc_rPAnsLabel);

		getP.add(panel, BorderLayout.CENTER);

		progressModel = new DefaultTableModel();
		progressModel.addColumn("ページ数");
		progressModel.addColumn("日付");
		progressModel.addColumn("日付");
		progressModel.addColumn("ID");
		progressModel = controller.reloadProgressModel(progressModel, userId, bookId);
		progressDataTable = new JTable(progressModel) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component toolTip = super.prepareRenderer(renderer, row, column);
				controller.setDateTooltip(progressModel, toolTip, row, column);
				return toolTip;
			}
		};
		progressDataTable = controller.progressDataTableSettings(progressDataTable);

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

		progressLabel = new JLabel(controller.setProgressLabel(userId, bookId));
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
		progressBar.setValue(controller.setProgress(userId, bookId));
		progressBar.setBackground(new Color(250, 250, 255));
		progressBar.setForeground(new Color(40, 150, 243));
		progressBar.setFont(new Font("MS UI Gothic", Font.PLAIN, 36));
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.weighty = 1.0;
		gbc_progressBar.weightx = 1.0;
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.gridheight = 4;
		gbc_progressBar.insets = new Insets(0, 10, 5, 5);
		gbc_progressBar.gridx = 4;
		gbc_progressBar.gridy = 2;
		panel.add(progressBar, gbc_progressBar);

		avgPAnsLabel = new JLabel(controller.setAvgPagesLabel(userId, bookId));
		avgPAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		avgPAnsLabel.setForeground(new Color(40, 40, 40));
		avgPAnsLabel.setFont(new Font("メイリオ", Font.PLAIN, 20));
		GridBagConstraints gbc_avgPAnsLabel = new GridBagConstraints();
		gbc_avgPAnsLabel.weightx = 1.0;
		gbc_avgPAnsLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_avgPAnsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_avgPAnsLabel.gridx = 1;
		gbc_avgPAnsLabel.gridy = 5;
		panel.add(avgPAnsLabel, gbc_avgPAnsLabel);

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
					controller.addRecentData(userId, bookId, todayProgress);
					inputTodayPages.setText(null);
				}
				updateText(userId, bookId);
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

		sumDaysAnsLabel = new JLabel("Total Days  " + controller.sumDays(userId, bookId) + "days");
		sumDaysAnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		sumDaysAnsLabel.setForeground(new Color(40, 40, 40));
		sumDaysAnsLabel.setFont(new Font("メイリオ", Font.PLAIN, 20));
		GridBagConstraints gbc_sumDaysAnsLabel = new GridBagConstraints();
		gbc_sumDaysAnsLabel.weightx = 1.0;
		gbc_sumDaysAnsLabel.fill = GridBagConstraints.BOTH;
		gbc_sumDaysAnsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_sumDaysAnsLabel.gridx = 1;
		gbc_sumDaysAnsLabel.gridy = 4;
		panel.add(sumDaysAnsLabel, gbc_sumDaysAnsLabel);

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

					// 選択された行とそのbook_idを特定
					int modelRow = progressDataTable.convertRowIndexToModel(selectedRow);
					int progressId = Integer.parseInt(progressModel.getValueAt(modelRow, 3).toString());

					// 本当に削除しますか？のポップアップ
					int userAnswer = JOptionPane.showConfirmDialog(null,
							"選択された進捗データを本当に削除しますか？", "注意", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (userAnswer == JOptionPane.YES_OPTION) {
						controller.deleteSelectedData(userId, progressId);
						updateText(userId, bookId);

					}
				}
			}
		});
		GridBagConstraints gbc_deleteButton = new GridBagConstraints();
		gbc_deleteButton.anchor = GridBagConstraints.EAST;
		gbc_deleteButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_deleteButton.weighty = 1.0;
		gbc_deleteButton.weightx = 1.0;
		gbc_deleteButton.insets = new Insets(0, 0, 5, 5);
		gbc_deleteButton.gridx = 3;
		gbc_deleteButton.gridy = 5;
		panel.add(deleteButton, gbc_deleteButton);
		
		Font mainFont = new Font("メイリオ", Font.PLAIN, 12);
		controller.changeFont(panel, mainFont);

	}

	public void run() {
		this.frame.setVisible(true);
	}

	public void stop() {
		this.frame.setVisible(false);
	}

	public void adjustableFontSize(int userId, int bookId) {
		String title = controller.setBookTitle(userId, bookId);
		bookTitleLabel.setText(title);
		
		int variable = title.length() / 10;
		if (variable == 0) {
			bookTitleLabel.setFont(new Font("メイリオ", Font.PLAIN, 30));
		} else {
			int fontSize = 20 / variable + 10;
			bookTitleLabel.setFont(new Font("メイリオ", Font.PLAIN, fontSize));
		}
	}

	public void changeFontColor() {
		List<Component> fontComponents = Arrays.asList(
				bookListButton, bookTitleLabel, bookShelfCombo, changeUI,
				remainPagesLabel, avgPAnsLabel, progressDataTable,
				inputTodayPages, inputButton, sumDaysAnsLabel, deleteButton);

		int red = Integer.valueOf(inputRed.getText());
		int green = Integer.valueOf(inputGreen.getText());
		int blue = Integer.valueOf(inputBlue.getText());
		actionList.setFontColor(fontComponents, red, green, blue);
	}

	public void changeBackGround() {
		List<Component> BackGroundComponents = Arrays.asList(
				bookListButton, bookShelfCombo, changeUI, scrollPane,
				progressBar, inputTodayPages, inputButton, deleteButton);
		int red = Integer.valueOf(inputRed.getText());
		int green = Integer.valueOf(inputGreen.getText());
		int blue = Integer.valueOf(inputBlue.getText());
		actionList.setBackGroundColor(BackGroundComponents, red, green, blue);
	}

	public void updateText(int userId, int bookId) {
		adjustableFontSize(userId, bookId);
		remainPagesLabel.setText(controller.setRemainPageLabel(userId, bookId));
		avgPAnsLabel.setText(controller.setAvgPagesLabel(userId, bookId));
		sumDaysAnsLabel.setText("Total Days  " + controller.sumDays(userId, bookId) + "days");
		progressModel = controller.reloadProgressModel(progressModel, userId, bookId);
		progressBar.setValue(controller.setProgress(userId, bookId));
		progressLabel.setText(controller.setProgressLabel(userId, bookId));
		panel.repaint();

	}

}
