package whut.com.myapp.bean;

public class AddAlarmItem {
    String alarmTime; // 闹钟时间
    int use;  // 这个闹钟是否使用

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getUse() {
        return use;
    }

    public void setUse(int use) {
        this.use = use;
    }

    public AddAlarmItem(String alarmTime, int use) {
        this.alarmTime = alarmTime;
        this.use = use;
    }
}
