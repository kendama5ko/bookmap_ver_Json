package controller;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import dao.BookShelfDAO;
import dao.BooksDAO;
import dao.JsonDAO;
import window.sub.ManageBooks;

public class ManageBooksController {
	ManageBooks mBooks;
	BooksDAO bdao;
	BookShelfDAO bsdao;
	JsonDAO jdao;
	List<Map<String, String>> bookInfoList;

	public ManageBooksController(ManageBooks mBooks) {
		this.mBooks = mBooks;
		this.jdao = new JsonDAO();
	}

	/*
	 * BookShelfのTableModelの受け取り
	 */
	public DefaultTableModel getBookShelfModel(DefaultTableModel model) {
		bookInfoList = new ArrayList<>();
		bookInfoList = jdao.searchBookList();
		for (Map<String, String> bookInfo : bookInfoList) {
			model.addRow(new Object[] {
					bookInfo.get("タイトル"),
					bookInfo.get("著者"),
					bookInfo.get("ジャンル"),
					bookInfo.get("総ページ数"),
					bookInfo.get("ID")
			});
		}
		return model;
	}

	/*
	 * 本棚に本を追加
	 */
	public String addBook(List<String> bookInfoList) {
		// HashMapに変換
		Map<String, String> bookInfoMap = new LinkedHashMap<>();

		String[] keys = { "タイトル", "著者", "ジャンル", "総ページ数" };
		for (int i = 0; i < keys.length; i++) {
			bookInfoMap.put(keys[i], bookInfoList.get(i));
		}

		// UUIDを生成して追加
		bookInfoMap.put("ID", UUID.randomUUID().toString());
		jdao.addBookToJson(bookInfoMap);

		return "登録しました。";
	}

	public void deleteBookByTable(String bookTitle) {
		jdao.deleteBook(bookTitle);
	}

	public String editBookData(int bookId, String columnName, String editedData) {
		bsdao = new BookShelfDAO();
		return bsdao.updateBookData(bookId, columnName, editedData);
	}

	public void updatedProcess(TableModelEvent e) {
		// 選択されたセルを特定
		int sortedRow = e.getFirstRow();
		int originalRow = mBooks.getBookListTable().convertRowIndexToModel(sortedRow);
		int column = e.getColumn();
		String columnName = mBooks.getBooksModel().getColumnName(column);

		// 用意していたモデルの複製と、変更されたモデルの値を比較
		Object editedDataObject = mBooks.getBooksModel().getValueAt(originalRow, column);
		Object oldDataObject = mBooks.getCopyOfBooksModel().getValueAt(originalRow, column);

		// originalRowからbookIdを取得するメソッド
		int bookId = Integer.parseInt(mBooks.getBooksModel().getValueAt(originalRow, 4).toString());

		// editBookDataのoriginalRowの引数をbookIdに変える
		if (editedDataObject instanceof String && !editedDataObject.equals(oldDataObject)) {
			String editedData = (String) editedDataObject;
			String updatedMessage = editBookData(bookId, columnName, editedData);
			mBooks.displayUpdatedMessage(updatedMessage);
		}
	}

	public void setTraversalOrder(List<Component> order, JFrame frame) {
		frame.setFocusTraversalPolicy(new FocusTraversalPolicy() {

			@Override
			public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
				int index = (order.indexOf(aComponent) + 1) % order.size();
				return order.get(index);
			}

			@Override
			public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
				int index = order.indexOf(aComponent) - 1;
				if (index < 0) {
					index = order.size() - 1;
				}
				return order.get(index);
			}

			@Override
			public Component getFirstComponent(Container focusCycleRoot) {
				return order.get(0);
			}

			@Override
			public Component getLastComponent(Container focusCycleRoot) {
				return order.get(order.size() - 1);
			}

			@Override
			public Component getDefaultComponent(Container focusCycleRoot) {
				return getFirstComponent(focusCycleRoot);
			}

		});
	}

	public void changeFont(JComponent component, Font font) {
		component.setFont(font);

		if (component instanceof Container) {
			for (Component child : ((Container) component).getComponents()) {
				if (child instanceof JComponent) {
					changeFont((JComponent) child, font);
				}
			}
		}
	}

}
