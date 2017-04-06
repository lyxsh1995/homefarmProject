package bean;

/**
 * Created by Administrator on 2017/3/9.
 */

public class DEjson
{
    private String ID;

    private String SQLString;

    private String exchTime;

    private String EQID;

    public void setID(String ID){
        this.ID = ID;
    }
    public String getID(){
        return this.ID;
    }
    public void setSQLString(String SQLString){
        this.SQLString = SQLString;
    }
    public String getSQLString(){
        return this.SQLString;
    }
    public void setExchTime(String exchTime){
        this.exchTime = exchTime;
    }
    public String getExchTime(){
        return this.exchTime;
    }
    public void setEQID(String EQID){
        this.EQID = EQID;
    }
    public String getEQID(){
        return this.EQID;
    }
}
