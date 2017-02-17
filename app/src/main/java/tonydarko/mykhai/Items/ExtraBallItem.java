package tonydarko.mykhai.Items;


public class ExtraBallItem {

    public String group;
    public String lastName;
    public String firstName;
    public String otchistvo;
    public String ball;

    public ExtraBallItem(String group, String lastName, String firstName, String otchistvo, String ball) {

        this.group = group;
        this.lastName = lastName;
        this.firstName = firstName;
        this.otchistvo = otchistvo;
        this.ball = ball;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtchistvo() {
        return otchistvo;
    }

    public void setOtchistvo(String otchistvo) {
        this.otchistvo = otchistvo;
    }

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }


}
