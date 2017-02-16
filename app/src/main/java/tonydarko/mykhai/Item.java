package tonydarko.mykhai;


public class Item {

    String group;
    String fio;
    String ball;

    public Item(String group, String fio, String ball) {
        this.group = group;
        this.fio = fio;
        this.ball = ball;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }
}
