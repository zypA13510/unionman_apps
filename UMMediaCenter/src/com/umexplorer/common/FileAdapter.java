package com.umexplorer.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umexplorer.R;
/**
 * data adapter
 * @author liu_tianbao Provide data for the list or thumbnail
 */
public class FileAdapter extends BaseAdapter {
    // filder
    private Bitmap folder_File;

    // music file
    private Bitmap music_File_mp3;

    // other file
    private Bitmap other_File;

    // vedio file
    private Bitmap vedio_File;

    // bd file
    private Bitmap bd_File;

    // dvd file
    private Bitmap dvd_File;

    // APK file
    private Bitmap apk_File;

    // picturn file
    private Bitmap img_File;

    // file list
    private List<File> list;

    // layout file ID
    private int layout = 0;

    /**
     * @brief : Eliminating redundant code
     */

    LayoutInflater inflater;

    CommonActivity context;

    String fileLength = null;

    private ArrayList<View> fileListActive = new ArrayList<View>();
    
    private List<Map<String, customerBitmap>> ThumbnailList = new ArrayList<Map<String, customerBitmap>>();
    /**
     * @param context
     *            page
     * @param list
     *            collection of files
     * @param fileString
     *            file path
     * @param layout
     *            data layout
     */
    // public FileAdapter(Activity context, List<File> list, int layout) {
    public FileAdapter(CommonActivity context, List<File> list, int layout) {
        /**
         * Initialization parameters
         */
        this.list = list;
        this.layout = layout;
        inflater = LayoutInflater.from(context);
        this.context = context;
        /*
         * According to the different file types together with the different
         * picture
         */
        /* CNcomment: 根据不同的文件类型配以不同的图片显示 */
        //jly 20140313
        //      other_File = BitmapFactory.decodeResource(context.getResources(),
        //              R.drawable.otherfile);
        other_File = BitmapFactory.decodeResource(context.getResources(),
                                                  R.drawable.um_other_file);
        apk_File = BitmapFactory.decodeResource(context.getResources(),
                                                R.drawable.um_apk_file);
        //      vedio_File = BitmapFactory.decodeResource(context.getResources(),
        //              R.drawable.vediofile);
        vedio_File = BitmapFactory.decodeResource(context.getResources(),
                                                  R.drawable.um_video_file);
        bd_File = BitmapFactory.decodeResource(context.getResources(),
                                               R.drawable.um_bd_file);
        dvd_File = BitmapFactory.decodeResource(context.getResources(),
                                                R.drawable.um_dvd_file);
        //      music_File_mp3 = BitmapFactory.decodeResource(context.getResources(),
        //              R.drawable.mp3file);
        music_File_mp3 = BitmapFactory.decodeResource(context.getResources(),
                                                      R.drawable.um_music_file);
        //      folder_File = BitmapFactory.decodeResource(context.getResources(),
        //              R.drawable.folder_file);
        folder_File = BitmapFactory.decodeResource(context.getResources(),
                                                   R.drawable.um_folder_file);
        //      img_File = BitmapFactory.decodeResource(context.getResources(),
        //              R.drawable.imgfile);
        img_File = BitmapFactory.decodeResource(context.getResources(),
                                                R.drawable.um_picture_file);
        //jly 20140313
    }

    /* he number of data containers */
    /* CNcomment: 获得容器中数据的数目 */

    public int getCount() {
        return list.size();
    }

    public void addFile(File file) {
        this.list.add(file);
    }

    public void addDir(int location, File file) {
        this.list.add(location, file);
    }

    /* For each option object container */
    /* CNcomment: 获得容器中每个选项对象 */

    public Object getItem(int position) {
        return list.get(position);
    }

    /* Access to each option in the container object */
    /* CNcomment: 获得容器中每个选项对象的ID */

    public long getItemId(int position) {
        return position;
    }

    public List<File> getFiles() {
        return list;
    }

    /* Assignment for each option object */
    /* CNcomment: 为每个选项对象赋值 */

    public View getView(final int position, View convertView, ViewGroup parent) {
        return getMyView(convertView, position);
    }

    private View getMyView(View convertView, int position) {
        // Control container
        final ViewHolder holder;

        /*
         * According to the view whether there is to initialize the controls in
         * each data container
         */
        /* CNcomment: 根据视图是否存在 初始化每个数据容器中的控件 */
        
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(layout, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.sizetext = (TextView) convertView
                              .findViewById(R.id.sizeText);
            holder.icon = (ImageView) convertView.findViewById(R.id.image_Icon);
            convertView.setTag(holder);
            
            fileListActive.add(convertView);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final File f;
        // f = list.get(position);
        f = list.get(position);
        String f_type = FileUtil.getMIMEType(f, context);

        // get the type of file
        if (f.isFile()) {
            if ("audio/*".equals(f_type)) {
                holder.icon.setImageBitmap(music_File_mp3);
            }
            else if ("video/*".equals(f_type) || "video/iso".equals(f_type)) {
            	int i = 0;
            	for (i=0; i < ThumbnailList.size(); i++)
            	{
            		if (ThumbnailList.get(i).get("imageBitmap").fileName.equals(f.getName()))
            		{
            			holder.icon.setImageBitmap(
            					ThumbnailList.get(i).get("imageBitmap").imageBitmap);
            			break;
            		}
            	}
            	if (i == ThumbnailList.size())
            	{
            		holder.icon.setImageBitmap(vedio_File);
            	}
            }
            else if ("apk/*".equals(f_type)) {
                holder.icon.setImageBitmap(apk_File);
            }
            else if ("image/*".equals(f_type)) {
            	int i = 0;
            	for (i=0; i < ThumbnailList.size(); i++)
            	{
            		if (ThumbnailList.get(i).get("imageBitmap").fileName.equals(f.getName()))
            		{
            			holder.icon.setImageBitmap(
            					ThumbnailList.get(i).get("imageBitmap").imageBitmap);
            			break;
            		}
            	}
            	if (i == ThumbnailList.size())
            	{
            		holder.icon.setImageBitmap(img_File);
            	}
            }
            else if ("video/dvd".equals(f_type)) {
                holder.icon.setImageBitmap(vedio_File);
            }
            else {
                holder.icon.setImageBitmap(other_File);
            }
        }
        else {
            if ("video/bd".equals(f_type)) {
                holder.icon.setImageBitmap(bd_File);
            }
            else if ("video/dvd".equals(f_type)) {
                holder.icon.setImageBitmap(dvd_File);
            }
            else {
                holder.icon.setImageBitmap(folder_File);
            }
        }

        // Judge the thumbnail display format of the text
        // CNcomment:判断缩略图时文字的显示格式
        if (layout == R.layout.gridfile_row) {
            if (holder.text.getPaint().measureText(f.getName()) >= holder.text
                .getLayoutParams().width) {
                holder.text.setEllipsize(TruncateAt.MARQUEE);
                holder.text.setMarqueeRepeatLimit(android.R.attr.marqueeRepeatLimit);
                holder.text.setHorizontallyScrolling(true);
            }
            else {
                holder.text.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
        else {
            if (f.isFile()) {
                fileLength = FileUtil.fileSizeMsg(f);
                holder.sizetext.setText(fileLength);
            }
            else {
                holder.sizetext.setText("");
            }
        }

        holder.text.setText(f.getName());
        return convertView;
    }

    public void imageChanged(customerBitmap bitmap) {
    	for (int i=0; i<fileListActive.size(); i++)
    	{
    		ViewHolder holder = (ViewHolder)fileListActive.get(i).getTag();
    		if (holder.text.getText().equals(bitmap.fileName))
    		{
    			holder.icon.setImageBitmap(bitmap.imageBitmap);
    			return ;
    		}
    	}
    }
    
    public void addThumbnail(Map<String, customerBitmap> map) {
    	ThumbnailList.add(map);
    }
    
    static public class customerBitmap {
    	public Bitmap imageBitmap;
    	public String fileName;
    }
    /**
     * the type of control container
     * @author qian_wei save control
     */
    private static class ViewHolder {
        private TextView text;
        private TextView sizetext;
        private ImageView icon;
    }

}
