package dto;

import java.io.Serializable;
import java.util.ArrayList;

import entity.BooksBean;

public class BooksDTO implements Serializable {
    private ArrayList<BooksBean> bookList;

    public BooksDTO() {
        bookList = new ArrayList<BooksBean>();
    }

    public void add(BooksBean bb) {
        bookList.add(bb);
    }

    public BooksBean get(int i) {
        return bookList.get(i);
    }

    public int size() {
        return bookList.size();
    }
}
