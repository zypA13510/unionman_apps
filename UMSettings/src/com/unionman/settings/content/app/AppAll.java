 package com.unionman.settings.content;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.unionman.settings.R;
import com.unionman.settings.UMSettings;
import com.unionman.settings.custom.Appinfo;
import com.unionman.settings.custom.AppinfoAsynctask;
import com.unionman.settings.layoutmanager.ConstantList;
import com.unionman.settings.layoutmanager.LayoutManager;
import com.unionman.settings.layoutmanager.RightWindowBase;
import com.unionman.settings.tools.UMLogger;
import com.unionman.settings.tools.Logger;
import com.unionman.settings.tools.UMDebug;
public class AppAll extends RightWindowBase
{
  public static final int FILTER_APPS_ALL = 0;
  public static final int FILTER_APPS_RUNNING = 1;
  public static final int FILTER_APPS_SDCARD = 3;
  public static final int FILTER_APPS_THIRD_PARTY = 2;
  private UMLogger log;
  private ListView mListView;
  private ApplicationInfoAdapter mAppInfoAdapter;
  public List<Appinfo> mAppLocalList;
  private PackageManager mPackageManager;
  private UMSettings settings;
  private static final String TAG = "com.unionman.settings.content.app--AppAll--";

  public AppAll(Context paramContext)
  {
    super(paramContext);
  }

  public void initData()
  {
    Logger.i(TAG,"initData()--");
    this.mAppLocalList = new ArrayList();
    this.mAppInfoAdapter = new ApplicationInfoAdapter(this.context);
    this.mListView.setAdapter(this.mAppInfoAdapter);
    this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        if (AppAll.this.mAppLocalList.size() == 0)
          AppAll.this.settings.setAppInfo(null);
        try
        {
        	AppAll.this.settings.setAppInfo((Appinfo)AppAll.this.mAppLocalList.get(paramAnonymousInt));
            AppAll.this.layoutManager.showLayout(ConstantList.FRAME_APP_DETAIL);            
           
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    });
  }

  public void onInvisible()
  {
  }

  public void onResume()
  {
    Logger.i(TAG,"onResume()--");
    this.mListView.requestFocus();
    AppinfoAsynctask localAppinfoAsynctask = new AppinfoAsynctask(this, this.context, this.mPackageManager, this.mAppLocalList, this.mAppInfoAdapter, this.mListView);
    Integer[] arrayOfInteger = new Integer[1];
    arrayOfInteger[0] = Integer.valueOf(0);
    localAppinfoAsynctask.execute(arrayOfInteger);
  }

  public void setId()
  {
    this.frameId = ConstantList.FRAME_APP_ALL;
    this.levelId = 1002;
    this.mPackageManager = this.context.getPackageManager();
    this.settings = ((UMSettings)this.context.getApplicationContext());
    this.log = UMLogger.getLogger(getClass());
  }

  public void setView()
  {
    Logger.i(TAG,"setView()--");
    this.layoutInflater.inflate(R.layout.app_all, this);
    this.mListView = ((ListView)findViewById(R.id.app_all_listView));
    this.mListView.setEmptyView(findViewById(R.id.progressBar1));
  }

  class ApplicationInfoAdapter extends BaseAdapter
  {

    Context content;
    private LayoutInflater mInflater;
    PackageInfo packageInfo = null;
    PackageInfo packinfo;

    public ApplicationInfoAdapter(Context arg2)
    {

      Context localContext = arg2;
      this.content = localContext;
      this.mInflater = LayoutInflater.from(localContext);
    }

    public int getCount()
    {
      Logger.i(TAG,"getCount()--");
      return AppAll.this.mAppLocalList.size();
    }

    public Object getItem(int paramInt)
    {
      Logger.i(TAG,"getItem()--");
      return AppAll.this.mAppLocalList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      Logger.i(TAG,"getItemId()--");
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Logger.i(TAG," getView()--");
      AppViewHolder localAppViewHolder = null;
      if (paramView == null)
      {
        paramView = this.mInflater.inflate(R.layout.app_item, null);
		localAppViewHolder = new AppViewHolder();;
        localAppViewHolder.tv_appName = ((TextView)paramView.findViewById(R.id.app_lable));
        localAppViewHolder.iv_appIcon = ((ImageView)paramView.findViewById(R.id.app_icon));
        localAppViewHolder.tv_appSize = ((TextView)paramView.findViewById(R.id.app_size));
        paramView.setTag(localAppViewHolder);
      }
      Appinfo localAppinfo;
	  localAppViewHolder = (AppViewHolder)paramView.getTag();
     
        localAppinfo = (Appinfo)AppAll.this.mAppLocalList.get(paramInt);
        localAppViewHolder.iv_appIcon.setImageDrawable(localAppinfo.appIcon);
        localAppViewHolder.tv_appName.setText(localAppinfo.appName);
		String str = Formatter.formatFileSize(this.content, localAppinfo.codeSize + localAppinfo.dataSize);
        if (localAppinfo.codeSize + localAppinfo.dataSize != -2L)
        {
        	localAppViewHolder.tv_appSize.setText(AppAll.this.context.getResources().getString(R.string.app_down_size, new Object[] { str }));
     	 	return paramView;
        };
     	AppAll.this.log.d("@@@@@@@@@@@@@@@@@@@@======" + str);
        localAppViewHolder.tv_appSize.setText(AppAll.this.context.getResources().getString(R.string.app_down_size, new Object[] { "0.0KB" }));
        return paramView;
    }

    class AppViewHolder
    {
      ImageView iv_appIcon;
      TextView tv_appName;
      TextView tv_appSize;

      AppViewHolder()
      {
      }
    }
  }
}

/* Location:           C:\Documents and Settings\Administrator\桌面\seting\classes_dex2jar.jar
 * Qualified Name:     net.sunniwell.app.swsettings.chinamobile.content.AppAll
 * JD-Core Version:    0.6.2
 */