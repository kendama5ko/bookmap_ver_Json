package controller;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;

import dao.ProgressDAO;
import window.Window;
import window.sub.ManageBooks;

public class ActionList {
	private Window window;
	private ManageBooks mBooks;
	private ProgressDAO pdao;
	private Controller controller;
	
	public ActionList(Window window) {
		this.window = window;
	}

	public void bookListButtonAction(int userId, Window window) {
		if (mBooks == null) {
			mBooks = new ManageBooks(userId, window);
		}
		mBooks.run();
		mBooks.updateFrame(userId);
	}
	
	public void bookShelfComboAction(int userId, int bookId) {
		pdao = new ProgressDAO();
		controller = new Controller();
		String bookTitle;
		JComboBox<String> bookShelfCombo = new JComboBox<>(controller.setBookList(userId));
		bookTitle = String.valueOf(bookShelfCombo.getSelectedItem());
		bookId = pdao.searchBookId(userId, bookTitle);
		
	}

	public void setFontColor(List<Component>  component,  int red, int green, int blue) {
		for (Component comp : component) {
		    comp.setForeground(new Color(red, green, blue));
		}
		
	}
	public void setBackGroundColor(List<Component>  component,  int red, int green, int blue) {
		for (Component comp : component) {
		    comp.setBackground(new Color(red, green, blue));
		}
		
	}

}
