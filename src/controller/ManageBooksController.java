package controller;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import java.text.NumberFormat;
import java.text.ParseException;

import dao.JsonDAO;
import window.subFrame.ManageBooks;

public class ManageBooksController {
	ManageBooks mBooks;
	JsonDAO jdao;
	List<Map<String, String>> bookInfoList;
	String updatedMessage;

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
					bookInfo.get("ページ数"),
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

		String[] keys = { "タイトル", "著者", "ジャンル", "ページ数" };
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].equals("ページ数")) {
				String convertedTotalPages = convertToHalfWidth(bookInfoList.get(i));
				bookInfoMap.put(keys[i], convertedTotalPages);
			} else {
				bookInfoMap.put(keys[i], bookInfoList.get(i));
			}
		}

		// UUIDを生成して追加
		bookInfoMap.put("ID", UUID.randomUUID().toString());
		jdao.addBookToJson(bookInfoMap);

		return "登録しました。";
	}

	public void deleteBookByTable(String bookID) {
		jdao.deleteBook(bookID);
	}

	public void updatedProcess(TableModelEvent e) {
		String bookID = getBookID(e);
		String columnName = getColumnName(e);

		Object editedDataObject = getDataObject(e, mBooks.getBooksModel());		//変更されたデータをobject化
		Object oldDataObject = getDataObject(e, mBooks.getCopyOfBooksModel());	//比較用の更新前のデータをobject化

		// 新旧のデータを比較　trueなら更新
		if (!editedDataObject.equals(oldDataObject)) { // && editedDataObject instanceof String
			updateData(bookID, columnName, editedDataObject);
			mBooks.displayUpdatedMessage(updatedMessage);
		}
	}

	public String getBookID(TableModelEvent e) {
		return mBooks.getBooksModel().getValueAt(getOriginalRow(e), 4).toString();
	}

	public String getColumnName(TableModelEvent e) {
		int column = e.getColumn();
		String columnName = mBooks.getBooksModel().getColumnName(column);
		return columnName;
	}

	public Object getDataObject(TableModelEvent e, DefaultTableModel booksModel) {
		int originalRow = getOriginalRow(e);
		int column = e.getColumn();
		return booksModel.getValueAt(originalRow, column);
	}

	public int getOriginalRow(TableModelEvent e) {
		int sortedRow = e.getFirstRow();
		int originalRow = mBooks.getBookListTable().convertRowIndexToModel(sortedRow);
		return originalRow;
	}

	public String updateData(String bookID, String columnName, Object editedDataObject) {
		String editedData = (String) editedDataObject;

		if (columnName.equals("ページ数")) {
			editedData = convertToHalfWidth(editedData);
		}
		updatedMessage = jdao.editBookData(bookID, columnName, editedData);
		return updatedMessage;
	}


	private String convertToHalfWidth(String fullwidthNumber) {
		try {
			// 日本のロケールに基づくNumberFormatを取得
			NumberFormat format = NumberFormat.getInstance(Locale.JAPAN);

			// parseメソッドを使用して文字列をNumberに変換し、そのままStringに変換して返す
			return format.parse(fullwidthNumber).toString();
		} catch (ParseException e) {
			// パースに失敗した場合のエラー処理
			e.printStackTrace();
			System.out.println("数字を全角から半角に変換できませんでした。");
			return fullwidthNumber; // デフォルト値を返すか、適切な処理を行う
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
