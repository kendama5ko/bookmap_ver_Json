package entity;

import java.util.List;

public class UserProgress {
    public String title;
    public String author;
    public String genre;
    public int totalPages;
    private List<ProgressData> progressData;

    // @Override
    // public String toString() {
    // return "UserProgress [title=" + title + ", author=" + author
    // + ", genre=" + genre + ", totalPages=" + totalPages + "]";
    // }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ProgressData> getProgressData() {
        return progressData;
    }

    public void setProgressData(List<ProgressData> progressData) {
        this.progressData = progressData;
    }
}
