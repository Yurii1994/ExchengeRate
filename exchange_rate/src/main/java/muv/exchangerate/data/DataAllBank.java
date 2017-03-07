package muv.exchangerate.data;


import com.orm.SugarRecord;

public class DataAllBank  extends SugarRecord
{
    private String title;
    private String date;
    private String cur;
    private String buy;
    private String sale;

    public DataAllBank() {
    }

    public DataAllBank(String title, String date, String id, String buy, String sale) {
        this.title = title;
        this.date = date;
        this.cur = id;
        this.buy = buy;
        this.sale = sale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
