package window;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.table.DefaultTableModel;

import controller.ActionList;
import controller.Controller;
import window.sub.ManageBooks;

public abstract class Window {
	protected int todayProgress;
	public Controller controller = new Controller();
	public ActionList actionList;
	
	protected JFrame frame;
	protected JPanel panel;
	protected JButton bookListButton;
	protected JButton inputButton;
	protected JButton deleteButton;
	protected JButton changeUI;
	protected JLabel bookTitleLabel;
	protected JLabel sumDaysAnsLabel;
	protected JLabel remainPagesLabel;
	protected JLabel avgPAnsLabel;
	protected JLabel progressLabel;
	protected JTable progressDataTable = new JTable();
	protected JTextField inputTodayPages;
	protected JProgressBar progressBar;
	protected JScrollPane scrollPane;
	protected DefaultTableModel progressModel;
	protected DefaultComboBoxModel<String> comboModel;
	protected JComboBox<String> bookShelfCombo = new JComboBox<String>();
	protected int userId;
	protected int bookId;
	protected List<String> bookList;
	protected ManageBooks mBooks;
	protected JTextField inputRed;
	protected JTextField inputGreen;
	protected JTextField inputBlue;
	public JComboBox<String> getBookShelfCombo() {
		return bookShelfCombo;
	}

	public void setBookShelfCombo(JComboBox<String> bookShelfCombo) {
		this.bookShelfCombo = bookShelfCombo;
	}

	abstract public void run();

	abstract protected void stop();

	abstract public void updateText(int userId, int bookId);

	public void allowOnlyNumbers(Component component) {

		component.addKeyListener(new KeyListener() {
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
	}

	public void updateBookShlefCombo() {
		int beforeIndex = bookShelfCombo.getSelectedIndex();

		comboModel = controller.setBookList(userId);
		bookShelfCombo.setModel(comboModel);

		bookShelfCombo.setSelectedIndex(beforeIndex);
	}

	public void updateBookShlefCombo(int deletedIndex) {
		int beforeIndex = bookShelfCombo.getSelectedIndex(); //選択していたアイテムの要素番号を取得

		comboModel = controller.setBookList(userId);
		bookShelfCombo.setModel(comboModel);

		//選択されていたindexより削除されたindexが大きい場合はそのままのindex
		if (beforeIndex < deletedIndex) {
			bookShelfCombo.setSelectedIndex(beforeIndex);

			//選択されていたindexより削除されたindexが小さい場合は削除された分1つ繰り上がるので -1	
		} else if (beforeIndex > deletedIndex) {
			bookShelfCombo.setSelectedIndex(beforeIndex - 1);

			//indexが同じなら未選択にする
		} else if (beforeIndex == deletedIndex) {
			bookShelfCombo.setSelectedItem(null);
		}
	}
}
