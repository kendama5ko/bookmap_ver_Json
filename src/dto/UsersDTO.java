package dto;

import java.io.Serializable;
import java.util.ArrayList;

import entity.UsersBean;

public class UsersDTO implements Serializable{
    private ArrayList<UsersBean> userList;
    
    public UsersDTO() {
        userList = new ArrayList<UsersBean>();
    }

    public void add(UsersBean ub) {
        userList.add(ub);
    }

    public UsersBean get(int i) {
        return userList.get(i);
    }

    public int size() {
        return userList.size();
    }
}
