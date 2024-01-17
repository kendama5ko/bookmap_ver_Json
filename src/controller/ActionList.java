package controller;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import window.Window;
import window.subFrame.ManageBooks;

public class ActionList {
	private ManageBooks mBooks;

	public ActionList(Window window) {
	}

	public void ManageBookButtonAction(int userId, Window window) {
		if (mBooks == null) {
			mBooks = new ManageBooks(userId, window);
		}
		mBooks.run();
		mBooks.updateFrame(userId);
	}
	
	public void bookShelfComboAction(int userId, int bookId) {
	
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
