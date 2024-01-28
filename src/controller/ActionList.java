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

	// public void changeFontColor(List<Component> fontComponents) {
		// List<Component> fontComponents = Arrays.asList(
		// 		ManageBookButton, bookTitleLabel, bookShelfCombo, changeUI,
		// 		remainPagesLabel, avgPagesAnsLabel, progressDataTable,
		// 		inputTodayPages, inputButton, totalDaysAnsLabel, deleteButton);

		// int red = Integer.valueOf(inputRed.getText());
		// int green = Integer.valueOf(inputGreen.getText());
		// int blue = Integer.valueOf(inputBlue.getText());
		// setFontColor(fontComponents, red, green, blue);
	// }

	// public void changeBackGround(List<Component> BackGroundComponents) {
		// List<Component> BackGroundComponents = Arrays.asList(
		// 		ManageBookButton, bookShelfCombo, changeUI, scrollPane,
		// 		progressBar, inputTodayPages, inputButton, deleteButton);
		// int red = Integer.valueOf(inputRed.getText());
		// int green = Integer.valueOf(inputGreen.getText());
		// int blue = Integer.valueOf(inputBlue.getText());
		// setBackGroundColor(BackGroundComponents, red, green, blue);
	// }
	
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
