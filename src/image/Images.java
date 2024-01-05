package image;

public class Images {
    private double windowWidth; // ウィンドウの幅
    private double windowHeight; // ウィンドウの高さ
    private double imageWidth; // 画像の幅
    private double imageHeight; // 画像の高さ

    private double widthRatio;
    private double heightRatio;
    private double ratio;

    int nWidth;
    int nHeight;

    public int getNewWidth() {
        this.nWidth = (int) (imageWidth * ratio);
        return nWidth;
    }

    public int getNewHeight() {
        this.nHeight = (int) (imageHeight * ratio);
        return nHeight;
    }

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(double windowHeight) {
        this.windowHeight = windowHeight;
    }

    public void setImageWidth(double imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(double imageHeight) {
        this.imageHeight = imageHeight;
    }


    public void setSize(double wWidth,
            double wHeight, double iWidth, double iHeight) {
        this.windowWidth = wWidth;
        this.windowHeight = wHeight;
        this.imageWidth = iWidth;
        this.imageHeight = iHeight;
        this.widthRatio = windowWidth / imageWidth;
        this.heightRatio = windowHeight / imageHeight;
        this.ratio = Math.min(widthRatio, heightRatio);
    }

}
