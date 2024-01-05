package dto;
import java.io.Serializable;
import java.util.ArrayList;

import entity.GenresBean;

public class GenresDTO implements Serializable{
    private ArrayList<GenresBean> genreList;

    public GenresDTO() {
        genreList = new ArrayList<GenresBean>();
    }
    public void add(GenresBean gb) {
        genreList.add(gb);
    }

    public GenresBean get(int i) {
        return genreList.get(i);
    }

    public int size() {
        return genreList.size();
    }
}
