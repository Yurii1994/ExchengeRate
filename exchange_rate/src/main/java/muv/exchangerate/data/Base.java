package muv.exchangerate.data;

import java.util.Arrays;
import java.util.List;


public class Base
{
    private List<DataAllBank> listAllBank;
    private List<DataNBU> listNbu;
    private List<String> listBank = Arrays.asList("Альфа-Банк", "ВТБ Банк", "КРЕДИТ ДНІПРО", "Кредобанк",
            "Креді Агріколь Банк", "ОТП Банк", "Ощадбанк", "ПІВДЕННИЙ", "ПЛАТИНУМ БАНК (Platinum Bank TM)",
            "ПРАВЕКС-БАНК", "ПУМБ", "ПриватБанк", "Райффайзен Банк Аваль", "СБЕРБАНК",
            "УкрСиббанк", "Укргазбанк", "Укрексiмбанк", "Укрсоцбанк UniCredit Bank TM");

    public Base(List<DataAllBank> listAllBank, List<DataNBU> listNbu) {
        this.listAllBank = listAllBank;
        this.listNbu = listNbu;
    }

    public void createBase()
    {
        deleteBase();
        try {
            for (int i = 0; i < listNbu.size(); i++)
            {
                String title = "Національний банк України";
                String date = listNbu.get(i).getDate();
                String cur = listNbu.get(i).getCc();
                String buy = listNbu.get(i).getRate();
                DataAllBank base = new DataAllBank(title, date, cur, buy, "");
                base.save();
            }
            for (int i = 0; i < listAllBank.size(); i++)
            {

                String title = listAllBank.get(i).getTitle();
                if (getBank(title))
                {
                    String date = listAllBank.get(i).getDate();
                    String cur = listAllBank.get(i).getCur();
                    String buy = listAllBank.get(i).getBuy();
                    String sale = listAllBank.get(i).getSale();
                    DataAllBank base = new DataAllBank(title, date, cur, buy, sale);
                    base.save();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private boolean getBank(String title)
    {
        boolean state = false;
        for (int i = 0; i < listBank.size(); i++)
        {
            String bank = listBank.get(i);
            if (bank.equals(title))
            {
                state = true;
            }
        }
        return state;
    }

    public void deleteBase()
    {
        try
        {
            DataAllBank.deleteAll(DataAllBank.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
