package muv.exchangerate.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import muv.exchangerate.data.DataAllBank;
import muv.exchangerate.fragment.AbstractTabFragment;
import muv.exchangerate.fragment.USDFragment;
import muv.exchangerate.fragment.EURFragment;
import muv.exchangerate.fragment.RUBFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter
{
    private Map<Integer, AbstractTabFragment> tabs;

    public List<DataAllBank> listUSD = new ArrayList<>();
    public List<DataAllBank> listEUR = new ArrayList<>();
    public List<DataAllBank> listRUB = new ArrayList<>();

    private USDFragment usdFragment;
    private EURFragment eurFragment;
    private RUBFragment rubFragment;

    public void setList(List<DataAllBank> listUSD, List<DataAllBank> listEUR, List<DataAllBank> listRUB) {
        usdFragment.refreshData(listUSD);
        eurFragment.refreshData(listEUR);
        rubFragment.refreshData(listRUB);
    }

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm,
                                    List<DataAllBank> listUSD, List<DataAllBank> listEUR, List<DataAllBank> listRUB) {
        super(fm);
        this.listUSD = listUSD;
        this.listEUR = listEUR;
        this.listRUB = listRUB;
        initTabMap(context);
    }


    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabMap(Context context) {
        tabs = new HashMap<>();
        usdFragment = USDFragment.getInstance(context, listUSD);
        eurFragment = EURFragment.getInstance(context, listEUR);
        rubFragment = RUBFragment.getInstance(context, listRUB);
        tabs.put(0, usdFragment);
        tabs.put(1, eurFragment);
        tabs.put(2, rubFragment);
    }
}
