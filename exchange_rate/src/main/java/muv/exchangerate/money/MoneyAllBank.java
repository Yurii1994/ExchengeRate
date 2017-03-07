package muv.exchangerate.money;

import java.util.ArrayList;
import java.util.List;

import muv.exchangerate.data.DataAllBank;

public class MoneyAllBank
{
    List<DataAllBank> listAllBank;

    public MoneyAllBank(List<DataAllBank> listDataAllBank) {
        this.listAllBank = listDataAllBank;
    }

    public List<DataAllBank> getListMoney(String money)
    {
        List<DataAllBank> list = new ArrayList<>();
        DataAllBank data;
        for (int i = 0; i < listAllBank.size(); i++)
        {
            if (listAllBank.get(i).getCur().equals(money))
            {
                data = new DataAllBank();
                data.setTitle(listAllBank.get(i).getTitle());
                data.setCur(listAllBank.get(i).getCur());
                data.setBuy(listAllBank.get(i).getBuy());
                data.setDate(listAllBank.get(i).getDate());
                data.setSale(listAllBank.get(i).getDate());
                list.add(data);
            }
        }
        return list;
    }
}
