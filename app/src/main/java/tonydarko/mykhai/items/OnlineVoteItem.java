package tonydarko.mykhai.items;


public class OnlineVoteItem {

    private String numZayava;
    private String fioStudent;
    private String fioPrepod;
    private String predmet;
    private String group;
    private String date;

    public OnlineVoteItem(String numZayava, String fioStudent, String fioPrepod, String predmet, String group, String date) {
        this.numZayava = numZayava;
        this.fioStudent = fioStudent;
        this.fioPrepod = fioPrepod;
        this.predmet = predmet;
        this.group = group;
        this.date = date;
    }

    public String getNumZayava() {
        return numZayava;
    }

    public void setNumZayava(String numZayava) {
        this.numZayava = numZayava;
    }

    public String getFioStudent() {
        return fioStudent;
    }

    public void setFioStudent(String fioStudent) {
        this.fioStudent = fioStudent;
    }

    public String getFioPrepod() {
        return fioPrepod;
    }

    public void setFioPrepod(String fioPrepod) {
        this.fioPrepod = fioPrepod;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
