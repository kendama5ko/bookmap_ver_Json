package window.sub;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controller.ManageBooksController;
import window.Window;

public class ManageBooks {
	private Window window;
	private final JFrame manageFrame;
	private final DefaultTableModel booksModel;

	private final DefaultTableModel copyOfBooksModel;
	private final JScrollPane booksScrollPane;
	private final JTable bookListTable;
	//private final JTable copyOfBookListTable;
	private final JButton addBookButton;
	private final JButton deleteBookButton;
	private final JLabel titleLabel;
	private final JLabel authorLabel;
	private final JLabel genreLabel;
	private final JLabel totalPagesLabel;
	private final JLabel errorMessageLabel;
	private final JLabel updatedMessageLabel;
	private final JTextField inputTitle;
	private final JTextField inputAuthor;
	private final JTextField inputGenre;
	private final JTextField inputTotalPages;
	private String addTitle;
	private String addAuthor;
	private String addGenre;
	private int addTotalPages;
	private String updatedMessage;
	LabelTimer labelTimer;
	Timer timer;
	int sec = 0;
	ManageBooksController mbController;

	public ManageBooks(int userId, Window window) {
		/*
		 * Frame, Panel
		 */
		this.window = window;
		this.manageFrame = new JFrame();
		manageFrame.setTitle("Books");
		manageFrame.setBounds(900, 200, 510, 360);

		Container getBooksPanel = manageFrame.getContentPane();
		JPanel bPanel = new JPanel();
		SpringLayout sLayout = new SpringLayout();
		bPanel.setLayout(sLayout);
		bPanel.setBackground(new Color(250, 250, 250));

		mbController = new ManageBooksController(this);
		/*
		 * Table
		 */

		// modelにカラムを追加しDAOから受け取ったデータを入れる
		booksModel = new DefaultTableModel();
		copyOfBooksModel = new DefaultTableModel(); // 変更がなければSQLを発行しないようにするためのデータ比較用
		addColumn(booksModel);
		addColumn(copyOfBooksModel);

		List<String[]> booksData = new ArrayList<>();
		booksData = mbController.getBookShelfList(userId);
		for (String[] bd : booksData) {
			booksModel.addRow(bd);
			copyOfBooksModel.addRow(bd);
		}

		// ModelをTableに入れる
		bookListTable = new JTable(booksModel);
		//copyOfBookListTable = new JTable(copyOfBooksModel);
		tableSettings(bookListTable);

		labelTimer = new LabelTimer();
		booksModel.addTableModelListener(new TableModelListener() {
			/*
			 * Tableの編集処理
			 * row,columnをDAOに送ってその結果をJLabelで受け取る
			 */
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					mbController = new ManageBooksController(ManageBooks.this);
					mbController.updatedProcess(e);
					updateFrame(userId);
				}
			}
		});

		// TableをScrollPaneに入れる
		booksScrollPane = new JScrollPane(bookListTable);
		booksScrollPane.setPreferredSize(new Dimension(400, 100));
		sLayout.putConstraint(SpringLayout.NORTH, booksScrollPane, 30, SpringLayout.NORTH, bPanel);
		sLayout.putConstraint(SpringLayout.WEST, booksScrollPane, 30, SpringLayout.WEST, bPanel);
		sLayout.putConstraint(SpringLayout.EAST, booksScrollPane, -30, SpringLayout.EAST, bPanel);
		bPanel.add(booksScrollPane);

		/*
		 * Button
		 */
		addBookButton = new JButton("追加");
		addBookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addTitle = inputTitle.getText();
				addAuthor = inputAuthor.getText();
				addGenre = inputGenre.getText();
				String totalPagesText = inputTotalPages.getText();
				if (numberVaridator(totalPagesText)) {
					try {
						addTotalPages = Integer.parseInt(totalPagesText);
						String result = mbController.addBook(userId, addTitle, addAuthor, addGenre, addTotalPages);
						System.out.println(result);
						clearText(bPanel);
						window.updateBookShlefCombo();

					} catch (NumberFormatException ne) {
						ne.printStackTrace();
						updatedMessage = "数字を入力してください。";
					}
				} else {
					updatedMessage = "ページ数を入力、または5桁以内で入力してください。";
				}
				updateFrame(userId);
				displayUpdatedMessage(updatedMessage);
			}
		});
		bPanel.add(addBookButton);

		deleteBookButton = new JButton("削除");
		deleteBookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = bookListTable.getSelectedRow();
				if (selectedRow != -1) {

					// 選択された行とそのbook_idを特定
					int modelRow = bookListTable.convertRowIndexToModel(selectedRow);
					int bookId = Integer.parseInt(booksModel.getValueAt(modelRow, 4).toString());
					String bookTitle = booksModel.getValueAt(modelRow, 0).toString();

					// 本当に削除しますか？のポップアップ
					int userAnswer = JOptionPane.showConfirmDialog(null,
							"「 " + bookTitle + " 」の進捗データは失われます。本当に削除しますか？", "注意", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (userAnswer == JOptionPane.YES_OPTION) {
						mbController.deleteBookByTable(userId, bookId);
						updateFrame(userId);
						window.updateBookShlefCombo(modelRow);
					} else if (userAnswer == JOptionPane.NO_OPTION) {
						return;
					}
				}
			}
		});
		bPanel.add(deleteBookButton);

		/*
		 * Label
		 */
		titleLabel = new JLabel("タイトル");
		sLayout.putConstraint(SpringLayout.SOUTH, titleLabel, 46, SpringLayout.SOUTH, booksScrollPane);
		sLayout.putConstraint(SpringLayout.WEST, titleLabel, 30, SpringLayout.WEST, bPanel);
		bPanel.add(titleLabel);

		authorLabel = new JLabel("著者");
		sLayout.putConstraint(SpringLayout.NORTH, deleteBookButton, 7, SpringLayout.NORTH, authorLabel);
		sLayout.putConstraint(SpringLayout.SOUTH, authorLabel, 31, SpringLayout.SOUTH, titleLabel);
		sLayout.putConstraint(SpringLayout.WEST, authorLabel, 30, SpringLayout.WEST, bPanel);
		bPanel.add(authorLabel);

		genreLabel = new JLabel("ジャンル");
		sLayout.putConstraint(SpringLayout.SOUTH, genreLabel, 29, SpringLayout.SOUTH, authorLabel);
		sLayout.putConstraint(SpringLayout.WEST, genreLabel, 30, SpringLayout.WEST, bPanel);
		bPanel.add(genreLabel);

		totalPagesLabel = new JLabel("ページ数");
		sLayout.putConstraint(SpringLayout.SOUTH, totalPagesLabel, 30, SpringLayout.SOUTH, genreLabel);
		sLayout.putConstraint(SpringLayout.WEST, totalPagesLabel, 30, SpringLayout.WEST, bPanel);
		bPanel.add(totalPagesLabel);

		errorMessageLabel = new JLabel("※数字のみ");
		sLayout.putConstraint(SpringLayout.SOUTH, errorMessageLabel, 29, SpringLayout.SOUTH, genreLabel);
		sLayout.putConstraint(SpringLayout.EAST, errorMessageLabel, -133, SpringLayout.EAST, bPanel);
		bPanel.add(errorMessageLabel);

		updatedMessageLabel = new JLabel();
		updatedMessageLabel.setVisible(false);
		sLayout.putConstraint(SpringLayout.SOUTH, updatedMessageLabel, 20, SpringLayout.SOUTH, booksScrollPane);
		sLayout.putConstraint(SpringLayout.EAST, updatedMessageLabel, -30, SpringLayout.EAST, bPanel);
		bPanel.add(updatedMessageLabel);

		/*
		 * TextField
		 */
		inputTitle = new JTextField();
		titleLabel.setLabelFor(inputTitle);
		inputTitle.setPreferredSize(new Dimension(200, 24));
		sLayout.putConstraint(SpringLayout.SOUTH, inputTitle, 50, SpringLayout.SOUTH, booksScrollPane);
		sLayout.putConstraint(SpringLayout.WEST, inputTitle, 58, SpringLayout.WEST, titleLabel);
		bPanel.add(inputTitle);

		inputAuthor = new JTextField();
		inputAuthor.setPreferredSize(new Dimension(200, 24));
		sLayout.putConstraint(SpringLayout.SOUTH, inputAuthor, 29, SpringLayout.SOUTH, inputTitle);
		sLayout.putConstraint(SpringLayout.WEST, inputAuthor, 58, SpringLayout.WEST, authorLabel);
		bPanel.add(inputAuthor);

		inputGenre = new JTextField();
		inputGenre.setPreferredSize(new Dimension(200, 24));
		sLayout.putConstraint(SpringLayout.SOUTH, inputGenre, 29, SpringLayout.SOUTH, inputAuthor);
		sLayout.putConstraint(SpringLayout.WEST, inputGenre, 58, SpringLayout.WEST, genreLabel);
		bPanel.add(inputGenre);

		inputTotalPages = new JTextField();
		sLayout.putConstraint(SpringLayout.SOUTH, inputTotalPages, 29, SpringLayout.SOUTH, inputGenre);
		sLayout.putConstraint(SpringLayout.WEST, errorMessageLabel, 6, SpringLayout.EAST, inputTotalPages);

		inputTotalPages.setPreferredSize(new Dimension(200, 24));
		inputTotalPages.addKeyListener(new KeyListener() {
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
		sLayout.putConstraint(SpringLayout.SOUTH, inputTotalPages, 29, SpringLayout.SOUTH, inputGenre);
		sLayout.putConstraint(SpringLayout.WEST, inputTotalPages, 58, SpringLayout.WEST, totalPagesLabel);
		bPanel.add(inputTotalPages);
		getBooksPanel.add(bPanel, BorderLayout.CENTER);

		JButton uiButton = new JButton("UI");
		sLayout.putConstraint(SpringLayout.NORTH, uiButton, 0, SpringLayout.NORTH, inputTotalPages);
		sLayout.putConstraint(SpringLayout.SOUTH, uiButton, -1, SpringLayout.SOUTH, inputTotalPages);
		sLayout.putConstraint(SpringLayout.WEST, uiButton, 0, SpringLayout.WEST, addBookButton);
		sLayout.putConstraint(SpringLayout.EAST, uiButton, 0, SpringLayout.EAST, booksScrollPane);
		bPanel.add(uiButton);
		
		sLayout.putConstraint(SpringLayout.NORTH, addBookButton, 0, SpringLayout.NORTH, inputTitle);
		sLayout.putConstraint(SpringLayout.SOUTH, addBookButton, 0, SpringLayout.SOUTH, inputTitle);
		sLayout.putConstraint(SpringLayout.EAST, addBookButton, -30, SpringLayout.EAST, bPanel);
		
		sLayout.putConstraint(SpringLayout.NORTH, deleteBookButton, 0, SpringLayout.NORTH, inputAuthor);
		sLayout.putConstraint(SpringLayout.SOUTH, deleteBookButton, 00, SpringLayout.SOUTH, inputAuthor);
		sLayout.putConstraint(SpringLayout.EAST, deleteBookButton, 0, SpringLayout.EAST, booksScrollPane);
		
		
		List<Component> order = new ArrayList<>();
		order.add(inputTitle);
		order.add(inputAuthor);
		order.add(inputGenre);
		order.add(inputTotalPages);
		order.add(addBookButton);
		order.add(deleteBookButton);

		mbController.setTraversalOrder(order, manageFrame);

		addBookButton.setBackground(new Color(225, 238, 251));
		deleteBookButton.setBackground(new Color(225, 238, 251));
		uiButton.setBackground(new Color(225, 238, 251));
		
		Font allFont = new Font("メイリオ", Font.PLAIN, 12);
		mbController.changeFont(bPanel, allFont);
	}

	/*
	 * 本のTableを編集後、5秒間updatedMessageLabelを表示
	 */
	class LabelTimer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			updatedMessageLabel.setVisible(false);
		}
	}

	// 追加buttonを押した後にJTableの内容を更新
	public void updateFrame(int userId) {
		booksModel.setRowCount(0);
		copyOfBooksModel.setRowCount(0);
		List<String[]> booksData = new ArrayList<>();
		booksData = mbController.getBookShelfList(userId);
		for (String[] bd : booksData) {
			booksModel.addRow(bd);
			copyOfBooksModel.addRow(bd);
		}
	}

	public void tableSettings(JTable table) {
		bookListTable.setAutoCreateRowSorter(true);
		bookListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		bookListTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		bookListTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		bookListTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

		TableColumn title = bookListTable.getColumnModel().getColumn(0);
		TableColumn author = bookListTable.getColumnModel().getColumn(1);
		TableColumn genre = bookListTable.getColumnModel().getColumn(2);
		TableColumn pages = bookListTable.getColumnModel().getColumn(3);
		title.setPreferredWidth(120);
		author.setPreferredWidth(60);
		genre.setPreferredWidth(50);
		pages.setPreferredWidth(30);
		
		// book_idのカラムは非表示にする
		TableColumn bookIdColumn = bookListTable.getColumnModel().getColumn(4);
		bookIdColumn.setMinWidth(0);
		bookIdColumn.setMaxWidth(0);
		bookIdColumn.setWidth(0);
		bookIdColumn.setPreferredWidth(0);
	}

	public void addColumn(DefaultTableModel model) {
		model.addColumn("タイトル");
		model.addColumn("著者");
		model.addColumn("ジャンル");
		model.addColumn("ページ数");
		model.addColumn("book_id");
	}

	public boolean numberVaridator(String totalPagesText) {
		boolean check = false;
		if (totalPagesText.matches("[0-9０-９]*") && totalPagesText.length() <= 5 && !totalPagesText.isEmpty()) {
			check = true;
		}
		return check;
	}

	public void clearText(JPanel panel) {
		for (Component component : panel.getComponents()) {
			if (component instanceof JTextField) {
				((JTextField) component).setText("");
			}
		}
	}

	public DefaultTableModel getBooksModel() {
		return booksModel;
	}

	public DefaultTableModel getCopyOfBooksModel() {
		return copyOfBooksModel;
	}

	public JTable getBookListTable() {
		return bookListTable;
	}

	public void displayUpdatedMessage(String updateMessage) {
		updatedMessageLabel.setText(updatedMessage);
		updatedMessageLabel.setVisible(true);
		timer = new Timer(5000, labelTimer);
		timer.start();
	}

	public void run() {
		this.manageFrame.setVisible(true);
	}
}
