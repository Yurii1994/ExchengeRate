package muv.exchangerate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import muv.exchangerate.adapter.TabsPagerFragmentAdapter;
import muv.exchangerate.constans.ConstantsURL;
import muv.exchangerate.data.Base;
import muv.exchangerate.data.DataAllBank;
import muv.exchangerate.data.DataNBU;
import muv.exchangerate.data.SaveLoadPreferences;
import muv.exchangerate.money.MoneyAllBank;

public class MainActivity extends AppCompatActivity
{
    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private static JsonXmlParser parser = new JsonXmlParser();

    private static SaveLoadPreferences saveLoadPreferences = new SaveLoadPreferences();
    public static List<DataAllBank> listUSD = new ArrayList<>();
    public static List<DataAllBank> listEUR = new ArrayList<>();
    public static List<DataAllBank> listRUB = new ArrayList<>();

    public static TabsPagerFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService();
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initTabs();

    }

    static class LoadingRate extends AsyncTask<Void, Void, Void> {

        Context context;
        String date;
        List<DataNBU> listNBU;
        List<DataAllBank> listAllBank;

        public LoadingRate(Context context, String date) {
            this.context = context;
            this.date = date;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                listNBU = parser.parseNBU(ConstantsURL.NBU_URL);
                listAllBank = parser.parseAllBank(ConstantsURL.ALL_BANK_URL);
            }
            catch (Exception e)
            {}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Base base = new Base(listAllBank, listNBU);
            base.createBase();
            saveLoadPreferences.clearPreferences("Date", context);
            saveLoadPreferences.saveStringPreferences("Date", "Date", date, context);

            List<DataAllBank> allBank = DataAllBank.listAll(DataAllBank.class);
            MoneyAllBank moneyAllBank = new MoneyAllBank(allBank);

            listUSD = moneyAllBank.getListMoney("USD");
            listEUR = moneyAllBank.getListMoney("EUR");
            listRUB = moneyAllBank.getListMoney("RUB");

            if (adapter != null)
            {
                adapter.setList(listUSD, listEUR, listRUB);
            }
        }
    }

    public static void getList(Context context)
    {
        Internet internet = new Internet();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dateDMY = day + ""+  month + "" + year;

        saveLoadPreferences.clearPreferences("Date", context);
        if (internet.isOnline(context))
        {
            listUSD = new ArrayList<>();
            listEUR = new ArrayList<>();
            listRUB = new ArrayList<>();
            if (adapter != null)
            {
                adapter.setList(listUSD, listEUR, listRUB);
            }

            if (saveLoadPreferences.loadStringPreferences("Date", "Date", context).equals("")
                    || !saveLoadPreferences.loadStringPreferences("Date", "Date", context).equals(dateDMY))
            {
                LoadingRate loadingRate = new LoadingRate(context, dateDMY);
                loadingRate.execute();
            }
            else
            {
                List<DataAllBank> allBank = DataAllBank.listAll(DataAllBank.class);
                MoneyAllBank moneyAllBank = new MoneyAllBank(allBank);
                listUSD = moneyAllBank.getListMoney("USD");
                listEUR = moneyAllBank.getListMoney("EUR");
                listRUB = moneyAllBank.getListMoney("RUB");

                if (adapter != null)
                {
                    adapter.setList(listUSD, listEUR, listRUB);
                }
            }
        }
        else
        {
            if (saveLoadPreferences.loadStringPreferences("Date", "Date", context).equals(dateDMY))
            {
                List<DataAllBank> allBank = DataAllBank.listAll(DataAllBank.class);
                MoneyAllBank moneyAllBank = new MoneyAllBank(allBank);
                listUSD = moneyAllBank.getListMoney("USD");
                listEUR = moneyAllBank.getListMoney("EUR");
                listRUB = moneyAllBank.getListMoney("RUB");

                if (adapter != null)
                {
                    adapter.setList(listUSD, listEUR, listRUB);
                }
            }
            else
            {
                listUSD = new ArrayList<>();
                listEUR = new ArrayList<>();
                listRUB = new ArrayList<>();

                if (adapter != null)
                {
                    adapter.setList(listUSD, listEUR, listRUB);
                }
            }
        }
    }

    public void startService() {
        startService(new Intent(this, Service.class));
    }

    private void initTabs()
    {
        getList(this);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new TabsPagerFragmentAdapter(this, getSupportFragmentManager(), listUSD, listEUR, listRUB);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_refresh:
                        getList(getApplicationContext());
                        break;
                    case R.id.menu_source:
                        Internet internet = new Internet();
                        if (internet.isOnline(getApplicationContext()))
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://finance.ua/ru/currency"));
                            startActivity(browserIntent);
                        }
                        break;
                    case R.id.menu_settings:
                        break;
                }
                return true;
            }
        });
    }
}
