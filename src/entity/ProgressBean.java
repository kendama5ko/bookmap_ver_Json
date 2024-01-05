package entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ProgressBean implements Serializable {
    private int progressId;
    private int userId;
    private int todayProgress;
    private int bookId;
    private Timestamp createdAt;

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTodayProgress() {
        return todayProgress;
    }

    public void setTodayProgress(int todayProgress) {
        this.todayProgress = todayProgress;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

}
