package com.umexplorer.common;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umexplorer.R;
import com.umexplorer.jni.Mountinfo;

/**
 * @author ni_guanhua
 */
public class NfsSvrAdapter extends BaseAdapter {

    private Context context;

    private List<Mountinfo> mountInfos;

    public NfsSvrAdapter(Context context, List<Mountinfo> mountInfos) {
        this.context = context;
        this.mountInfos = mountInfos;
    }

    public int getCount() {
        return mountInfos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rLayout = inflater.inflate(R.layout.file_row, null);
        Mountinfo info = mountInfos.get(position);
        ImageView imgServer = (ImageView) rLayout.findViewById(R.id.image_Icon);
        // if (info.getUcIsMounted() == 1) {
        //jly 20140313
        //      imgServer.setImageResource(R.drawable.folder_file);
        imgServer.setImageResource(R.drawable.um_folder_file);
        //jly 20140313
        // } else {
        // imgServer.setImageResource(R.drawable.nfs_fld_umnt);
        // }
        TextView tvCltFold = (TextView) rLayout.findViewById(R.id.text);
        StringBuilder builder = new StringBuilder();
        builder.append(info.getPcName()).append(":")
        .append(info.getSzSvrFold());
        tvCltFold.setText(builder.toString());
        return rLayout;
    }

}
