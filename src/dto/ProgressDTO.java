package dto;

import java.io.Serializable;
import java.util.ArrayList;

import entity.ProgressBean;

public class ProgressDTO implements Serializable {
    private ArrayList<ProgressBean> pageList;

    public ProgressDTO() {
        pageList = new ArrayList<ProgressBean>();
    }

    public void add(ProgressBean pb) {
        pageList.add(pb);
    }

    public ProgressBean get(int i) {
        return pageList.get(i);
    }

    public int size() {
        return pageList.size();
    }
}
