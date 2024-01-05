package dto;

import java.io.Serializable;
import java.util.ArrayList;

import entity.AuthorsBean;

public class AuthorsDTO implements Serializable{
    private ArrayList<AuthorsBean> authorList;

    public AuthorsDTO() {
        authorList = new ArrayList<AuthorsBean>();
    }

    public void add(AuthorsBean ab) {
        authorList.add(ab);
    }

    public AuthorsBean get(int i) {
        return authorList.get(i);
    }

    public int size() {
        return authorList.size();
    }
}
