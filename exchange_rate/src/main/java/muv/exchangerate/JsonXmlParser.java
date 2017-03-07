package muv.exchangerate;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import muv.exchangerate.data.DataAllBank;
import muv.exchangerate.data.DataNBU;

public class JsonXmlParser
{
    public List<DataNBU> parseNBU(String dateUrl) throws Exception
    {
        List<DataNBU> list = new ArrayList<>();

        URL url = new URL(dateUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String resultJson = buffer.toString();

        JSONArray jsonObj = new JSONArray(resultJson);

        for (int i = 0; i < jsonObj.length(); i++)
        {
            DataNBU dataNBU = new DataNBU();
            JSONObject object = jsonObj.getJSONObject(i);
            String cc = object.getString("cc");
            String rate = object.getString("rate");
            String date = object.getString("exchangedate");
            if (cc.equals("RUB") || cc.equals("EUR") || cc.equals("USD"))
            {
                dataNBU.setCc(cc);
                dataNBU.setRate(rate);
                dataNBU.setDate(date);
                list.add(dataNBU);
            }
        }
        return list;
    }

    public List<DataAllBank> parseAllBank(String dateUrl) throws Exception {
        List<DataAllBank> list = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser xpp = factory.newPullParser();
        URL url = new URL(dateUrl);
        URLConnection ucon = url.openConnection();
        InputStream is = ucon.getInputStream();
        xpp.setInput(is, null);

        DataAllBank data;
        String title = "";
        String date = "";
        Boolean stateBreak = false;

        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("source"))
                    {
                        for (int i = 0; i < xpp.getAttributeCount(); i++)
                        {
                            if (xpp.getAttributeName(i).equals("date"))
                            {
                                date = xpp.getAttributeValue(i);
                            }
                        }
                    }
                    if (xpp.getName().equals("title"))
                    {
                        for (int i = 0; i < xpp.getAttributeCount(); i++)
                        {
                            title = xpp.getAttributeValue(i);
                        }
                    }
                    if (xpp.getName().equals("c") & !stateBreak)
                    {
                        data = new DataAllBank();
                        boolean stateId = false;
                        for (int i = 0; i < xpp.getAttributeCount(); i++)
                        {
                            if (xpp.getAttributeName(i).equals("id"))
                            {
                                data.setCur(xpp.getAttributeValue(i));
                                if (xpp.getAttributeValue(i).equals("USD") || xpp.getAttributeValue(i).equals("EUR")
                                        || xpp.getAttributeValue(i).equals("RUB"))
                                {
                                    stateId = true;
                                }
                            }
                            if (xpp.getAttributeName(i).equals("br"))
                            {
                                data.setBuy(xpp.getAttributeValue(i));
                            }
                            if (xpp.getAttributeName(i).equals("ar"))
                            {
                                data.setSale(xpp.getAttributeValue(i));
                            }
                            data.setDate(date);
                            data.setTitle(title);
                        }
                        if (stateId)
                        {
                            list.add(data);
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:

                    if (xpp.getName().equals("organizations"))
                    {
                        stateBreak = true;
                    }
                    break;
                default:
                    break;
            }
            xpp.next();
        }
        return list;
    }

}
