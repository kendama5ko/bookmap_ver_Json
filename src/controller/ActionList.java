package controller;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;

import window.Window;
import window.mainFrame.BookInfo;
import window.subFrame.ManageBooks;

public class ActionList {
	private ManageBooks mBooks;

	public ActionList(Window window) {
	}

	public void ManageBookButtonAction(Window window) {
		if (mBooks == null) {
			mBooks = new ManageBooks(window);
		}
		mBooks.run();
		mBooks.updateFrame();
	}

	public BookInfo bookShelfComboAction(JComboBox<BookInfo> bookShelfCombo) {
		String bookID;
		String bookTitle;
		BookInfo selectedBook = (BookInfo) bookShelfCombo.getSelectedItem();
		// JComboBoxが未選択(null)でないことを確認してからIDを取得
		if (selectedBook != null) {
			bookID = selectedBook.getID();
			bookTitle = selectedBook.getTitle();
		} else {
			bookID = "";
			bookTitle = "";
		}
		BookInfo bookInfo = new BookInfo(bookID, bookTitle);
		return bookInfo;
	}

	// public void changeFontColor(List<Component> fontComponents) {
	// List<Component> fontComponents = Arrays.asList(
	// ManageBookButton, bookTitleLabel, bookShelfCombo, changeUI,
	// remainPagesLabel, avgPagesAnsLabel, progressDataTable,
	// inputTodayPages, inputButton, totalDaysAnsLabel, deleteButton);

	// int red = Integer.valueOf(inputRed.getText());
	// int green = Integer.valueOf(inputGreen.getText());
	// int blue = Integer.valueOf(inputBlue.getText());
	// setFontColor(fontComponents, red, green, blue);
	// }

	// public void changeBackGround(List<Component> BackGroundComponents) {
	// List<Component> BackGroundComponents = Arrays.asList(
	// ManageBookButton, bookShelfCombo, changeUI, scrollPane,
	// progressBar, inputTodayPages, inputButton, deleteButton);
	// int red = Integer.valueOf(inputRed.getText());
	// int green = Integer.valueOf(inputGreen.getText());
	// int blue = Integer.valueOf(inputBlue.getText());
	// setBackGroundColor(BackGroundComponents, red, green, blue);
	// }

	public void setFontColor(List<Component> component, int red, int green, int blue) {
		for (Component comp : component) {
			comp.setForeground(new Color(red, green, blue));
		}

	}

	public void setBackGroundColor(List<Component> component, int red, int green, int blue) {
		for (Component comp : component) {
			comp.setBackground(new Color(red, green, blue));
		}

	}

}
