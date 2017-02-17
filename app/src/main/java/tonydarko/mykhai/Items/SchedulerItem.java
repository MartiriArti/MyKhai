package tonydarko.mykhai.Items;

public class SchedulerItem {

    public String fio;
    public String group;
    public String para;
    public String fioPrepod;
    public String type;
    public String date;

    public SchedulerItem(String fio, String group, String para, String type, String date,String fioPrepod) {
        this.fio = fio;
        this.group = group;
        this.para = para;
        this.type = type;
        this.date = date;
        this.fioPrepod = fioPrepod;

    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getParaFioPrepod() {
        return fioPrepod;
    }

    public void setParaFioPrepod(String fioPrepod) {
        this.fioPrepod = fioPrepod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
