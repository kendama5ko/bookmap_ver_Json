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

	public void ManageBookButtonAction(Window window) {
		if (mBooks == null) {
			mBooks = new ManageBooks(window);
		}
		mBooks.run();
		mBooks.updateFrame();
	}
	
	public void bookShelfComboAction(int bookId) {
	
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
