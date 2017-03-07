package muv.exchangerate;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import muv.exchangerate.constans.ConstantsURL;
import muv.exchangerate.data.Base;
import muv.exchangerate.data.DataAllBank;
import muv.exchangerate.data.DataNBU;
import muv.exchangerate.data.SaveLoadPreferences;
import muv.exchangerate.money.MoneyNBU;

public class Service extends android.app.Service
{
    private NotificationManager nm;
    private JsonXmlParser parser = new JsonXmlParser();
    private List<DataNBU> listNBU = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        someTaskOfTime();
        return START_STICKY;
    }

    void someTaskOfTime()
    {
        Timer timer = new Timer();
        TimerTask task = new LoadingRateTimerTask(this);
        timer.schedule(task, 0, 600000);
    }

    public class LoadingRateTimerTask extends TimerTask {

        public LoadingRateTimerTask(Context context) {
            this.context = context;
        }

        Context context;

        @Override
        public void run()
        {
            Internet internet = new Internet();
            if (internet.isOnline(context))
            {
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);

                String dateDMY = day + ""+  month + "" + year;

                SaveLoadPreferences saveLoadPreferences = new SaveLoadPreferences();

                if (saveLoadPreferences.loadStringPreferences("Date", "Date", context).equals("")
                        || !saveLoadPreferences.loadStringPreferences("Date", "Date", context).equals(dateDMY)) {

                    if (hour >= 9) {
                        LoadingRate loadingRate = new LoadingRate(dateDMY, context);
                        loadingRate.execute();
                    }
                }
            }
        }
    }

    class LoadingRate extends AsyncTask<Void, Void, Void> {

        private boolean stateLoading;
        private String date;
        private Context context;
        List<DataAllBank> listAllBank;
        SaveLoadPreferences saveLoadPreferences = new SaveLoadPreferences();

        public LoadingRate(String date, Context context) {
            this.date = date;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                listNBU = parser.parseNBU(ConstantsURL.NBU_URL);
                listAllBank = parser.parseAllBank(ConstantsURL.ALL_BANK_URL);
                stateLoading = true;
            }
            catch (Exception e) {
                stateLoading = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            Base base = new Base(listAllBank, listNBU);
            base.createBase();
            saveLoadPreferences.clearPreferences("Date", context);
            saveLoadPreferences.saveStringPreferences("Date", "Date", date, context);
            sendNotification(stateLoading);
        }
    }

    private void sendNotification(boolean stateLoading)
    {
        if (stateLoading)
        {
            MoneyNBU money = new MoneyNBU(listNBU);
            DataNBU USDdata = money.getMoney("USD");
            DataNBU EURdata = money.getMoney("EUR");

            String USD = USDdata.getRate();
            String EUR = EURdata.getRate();

            String mesagge = getResources().getString(R.string.nbu_title) + ": " +
                    getResources().getString(R.string.usd) + " - " + USD +
                    ", " + getResources().getString(R.string.eur) + " - " + EUR;

            long when = System.currentTimeMillis();
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(mesagge)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(when)
                    .build();

            Intent notificationIntent = new Intent(this, MainActivity.class);
            notification.contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            notification.defaults |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            nm.notify(1, notification);
        }
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static class Alarm extends BroadcastReceiver {

        public static final String ALARM_EVENT = "net.multipi.ALARM";
        public static final int ALARM_INTERVAL_SEC = 5;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isRunning(context)) {
                context.startService(new Intent(context, Service.class));
            }
        }

        public static void setAlarm(Context context) {
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * ALARM_INTERVAL_SEC, pi);
        }

        public static void cancelAlarm(Context context) {
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
