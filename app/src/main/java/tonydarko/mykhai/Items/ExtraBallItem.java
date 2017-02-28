package tonydarko.mykhai.Items;


public class ExtraBallItem {

    public String group;
    public String fio;
    public String fullBall;
    public String ball;

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

    public String getFirstName() {
        return fio;
    }

    public void setFirstName(String firstName) {
        this.fio = firstName;
    }

    public String getOtchistvo() {
        return fullBall;
    }

    public void setOtchistvo(String otchistvo) {
        this.fullBall = otchistvo;
    }

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }


}
