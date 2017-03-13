package tonydarko.mykhai.items;

public class ExtraBallItem {

    private String group;
    private String fio;
    private String fullBall;
    private String ball;

    public ExtraBallItem(String group, String lastName, String fullBall, String ball) {

        this.group = group;
        this.fio = lastName;
        this.fullBall = fullBall;
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


    public String getFullBall() {
        return fullBall;
    }

    public void setFullBall(String fullBall) {
        this.fullBall = fullBall;
    }

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }


}