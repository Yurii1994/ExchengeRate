package muv.exchangerate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import muv.exchangerate.Internet;
import muv.exchangerate.MainActivity;
import muv.exchangerate.R;
import muv.exchangerate.adapter.RUBAdapter;
import muv.exchangerate.adapter.TabsPagerFragmentAdapter;
import muv.exchangerate.data.DataAllBank;

public class RUBFragment extends AbstractTabFragment
{
    private static final int LAYOUT = R.layout.fragment_usd_eur_rub;

    private RUBAdapter adapter;
    private ProgressWheel progressWheel;
    private ImageView imageView;

    private List<DataAllBank> list = new ArrayList<>();

    public void setList(List<DataAllBank> list) {
        this.list = list;
    }

    public static RUBFragment getInstance(Context context, List<DataAllBank> list) {
        Bundle bundle = new Bundle();
        RUBFragment fragment = new RUBFragment();
        fragment.setArguments(bundle);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.rub));
        fragment.setList(list);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_usd_eur_rub);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RUBAdapter(list, context);
        recyclerView.setAdapter(adapter);

        progressWheel = (ProgressWheel)view.findViewById(R.id.progress_bar);
        imageView = (ImageView)view.findViewById(R.id.not_internet);

        return view;
    }

    public void refreshData(List<DataAllBank> list)
    {
        if (adapter != null)
        {
            Internet internet = new Internet();
            if (internet.isOnline(context))
            {
                if (list.size() > 0)
                {
                    progressWheel.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                }
                else
                {
                    progressWheel.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }
            }
            else
            {
                if (list.size() > 0)
                {
                    progressWheel.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                }
                else
                {
                    progressWheel.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData(MainActivity.listRUB);
    }
}
