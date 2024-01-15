package window.main;


public class BookInfo {
    private String ID;
    private String title;

    public BookInfo(String iD, String title) {
        this.ID = iD;
        this.title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        this.ID = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;  // コンボボックスに表示される文字列
    }

}