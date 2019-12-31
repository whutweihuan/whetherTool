package whut.com.myapp.bean;

public class Whether {
    double shidu; //湿度
    double zhiliang; // 空气质量
    int st ; // 状态，0代表晴天，1代表阴天，2代表下雨

    public double getShidu() {
        return shidu;
    }

    public void setShidu(double shidu) {
        this.shidu = shidu;
    }

    public double getZhiliang() {
        return zhiliang;
    }

    public void setZhiliang(double zhiliang) {
        this.zhiliang = zhiliang;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }
}
