package muv.exchangerate.money;

import java.util.List;
import muv.exchangerate.data.DataNBU;

public class MoneyNBU
{
    List<DataNBU> listNBU;

    public MoneyNBU(List<DataNBU> listDataNBU) {
        this.listNBU = listDataNBU;
    }

    public DataNBU getMoney(String money)
    {
        DataNBU data = new DataNBU();
        for (int i = 0; i < listNBU.size(); i++)
        {
            if (listNBU.get(i).getCc().equals(money))
            {
                data.setCc(listNBU.get(i).getCc());
                data.setRate(listNBU.get(i).getRate());
                data.setDate(listNBU.get(i).getDate());
            }
        }
        return data;
    }
}
