package com.um.filemanager.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.storage.IMountService;
import android.os.ServiceManager;
import android.os.IBinder;

import com.um.filemanager.R;
import com.hisilicon.android.netshare.Jni;
import com.hisilicon.android.netshare.NativeSamba;
import com.hisilicon.android.netshare.SambaTreeNative;
import com.um.filemanager.common.CommonActivity;
import com.um.filemanager.common.ControlListAdapter;
import com.um.filemanager.common.FileAdapter;
import com.um.filemanager.common.FileUtil;
import com.um.filemanager.common.MyDialog;
import com.um.filemanager.common.MyProDialog;
import com.um.filemanager.common.NewCreateDialog;
import com.um.filemanager.common.SocketClient;
import com.um.filemanager.ftp.DBHelper;

/**
 * @author liu_tianbao
 */
public class SambaActivity extends CommonActivity implements Runnable {

    static final String TAG = "SambaActivity";

    private static final int MENU_EDIT = Menu.FIRST + 14;

    // add shortcut
    // CNcomment:添加快捷方式
    private static final int MENU_SHORT = Menu.FIRST + 16;

    private String parentPath = "";

    private List<File> fileArray = null;

    // server list, Set the data source of server list
    // CNcomment:server 列表,设置服务器列表数据源
    List<Map<String, Object>> list = null;

    // directorys
    private String directorys = "/sdcard";

    private int result;

    // Switching the Display menu content flag, the default is the server list identifies the focus
    // CNcomment:切换显示菜单内容标识位,默认为服务器列表获得焦点标识
    private int temp = 0;

    // server path
    // CNcomment:服务器路径
    private String folder_position = "";

    // the name of the server which clicked
    // CNcomment:点击的item中的服务器名字
    private String serverName = "";

    private String Userserver = "";

    private String Username = "";

    private String Userpass = "";

    private String display = "";

    // private List<File> li = null;

    private EditText editServer;

    private EditText editName;

    private EditText editpass;

    private EditText editdisplay;

    private EditText position;

    // Sub operation menu
    // CNcomment:子操作菜单
    SubMenu suboper;

    // current cursor position
    // CNcomment:当前光标位置
    private int myPosition = 0;

    MyDialog dialog;

    Button myOkBut;

    Button myCancelBut;

    private DBHelper dbHelper;

    private SQLiteDatabase sqlite;

    Cursor cursor;

    // name selections of selected files
    // CNcomment:被选择文件名集合
    List<String> selected;

    // edit, create flag
    // CNcomment:编辑、新建标识
    int controlFlag = 0;

    // edit data id
    // CNcomment:编辑数据ID
    int id = 0;

    // operation flag
    // CNcomment:操作标识
    int flagItem = 0;

    AlertDialog alertDialog;

    // Deleted data container
    // CNcomment:删除数据容器
    ListView listViews;

    String prevName = "";

    String prevFolder = "";

    String prevUser = "";

    String prevPass = "";

    // Click position collection
    // CNcomment:点击位置集合
    List<Integer> intList;

    Jni jni;

    String localPath = "";

    static final String TABLE_NAME = "samba";

    static final String ID = "_id";

    static final String SERVER_IP = "server_ip";

    static final String NICK_NAME = "nick_name";

    static final String WORK_PATH = "work_path";

    static final String MOUNT_POINT = "mount_point";

    static final String ACCOUNT = "account";

    static final String PASSWORD = "password";

    static final String IMAGE = "image";

    // Shortcut, all network identification
    // CNcomment:快捷方式、全部网络标识
    static final String SHORT = "short";

    // Shared directory collection
    // CNcomment:共享目录集合
    List<Map<String, Object>> workFolderList;

    // Server introduces information key value
    // CNcomment:服务器介绍信息key值
    static final String SERVER_INTRO = "infos";

    private String prePath = "";

    private CheckBox netCheck;

    AlertDialog nServerLogonDlg;

    private static final String BLANK = "";

    static final String NEED_INPUT_PASSWORD = "NT_STATUS_ACCESS_DENIED";

    // display Number of files and the current position
    // CNcomment:文件数目和当前位置显示
    private TextView numInfo;

    private String loginName = "";

    private String loginPass = "";
    // =============== SambaTree ====================================
    ProgressDialog pgsDlg;

    long waitLong;

    String strWorkgrpups;

    SambaTreeNative sambaTree;

    long totalLong;

    Timer timer;

    static final String DIR_ICON = "dirIcon";

    // For recording the server clicked in the server list
    // CNcomment:用于记录在服务器列表中点击的网络服务器
    Map<String, Object> clickedNetServer;

    // Shared directory list from the clicked server
    // CNcomment:放置点击服务器后获取到的共享目录列表
    Map<String, List<Map<String, Object>>> server2groupDirs = new HashMap<String, List<Map<String, Object>>>(
        1);

    private MenuItem pasteMenuItem;

    private MenuItem cutMenuItem;

    private MenuItem copyMenuItem;

    private MenuItem renameMenuItem;

    private MenuItem deleteMenuItem;

    private final static int USER_OR_PASS_ERROR = -6;

    private final static int NET_ERROR = -5;

    private String prevServer;

    // Server address and shared directories
    // 服务器地址和共享目录相连
    private StringBuilder builder = null;

    // collection of shortcut or shared directory
    // CNcomment:快捷方式或者共享目录集合
    private Map<String, Object> clickedServerDirItem;

    // new mount
    // CNcomment:新增挂载
    private static final int MOUNT_RESULT_1 = 11;

    // edit mount
    // CNcomment:编辑挂载
    private static final int MOUNT_RESULT_2 = 12;

    // short or share directory mount
    // CNcomment:快捷方式或者共享目录挂载
    private static final int MOUNT_RESULT_3 = 13;

    // Anonymous mount fail or shortcut mount fail
    // CNcomment:匿名挂载失败或者快捷方式挂载失败
    private static final int MOUNT_RESULT_4 = 14;

    private AlertDialog sureDialog;

    // shortcut or shared directory flag
    // CNcomment:快捷方式或共享目录标识
    private int allOrShort = 0;

    private String isoParentPath = new String();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lan);
        init();
    }

    private void init() {
        showBut = (ImageButton) findViewById(R.id.showBut);
        // sortBut = (ImageButton) findViewById(R.id.sortBut);
        filterBut = (ImageButton) findViewById(R.id.filterBut);
        jni = new Jni();
        listFile = new ArrayList<File>();
        intList = new ArrayList<Integer>();
        fileL = new ArrayList<File>();
        gridlayout = R.layout.gridfile_row;
        listlayout = R.layout.file_row;
        listView = (ListView) findViewById(R.id.listView);
        pathTxt = (TextView) findViewById(R.id.textPath);
        gridView = (GridView) findViewById(R.id.gridView);
        // init server list
        // CNcomment:初始化服务器数据列表
        list = new ArrayList<Map<String, Object>>(1);
        dbHelper = new DBHelper(this, DBHelper.DATABASE_NAME, null,
                                DBHelper.DATABASE_VERSION);
        sqlite = dbHelper.getWritableDatabase();
        selected = new ArrayList<String>();
        numInfo = (TextView) findViewById(R.id.title);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(IMAGE, R.drawable.mainfile);
        map.put(NICK_NAME, getString(R.string.all_network));
        map.put(SHORT, 0);
        list.add(map);
        list.addAll(getServer());
        servers();
        isNetworkFile = true;
    }

    /**
     * Get information and set to the UI server list information
     * CNcomment:获取服务器信息并设置到UI列表信息中
     */
    private void servers() {
        SimpleAdapter serveradapter = new SimpleAdapter(this, list,
                                                        R.layout.file_row, new String[] { IMAGE, NICK_NAME },
                                                        new int[] { R.id.image_Icon, R.id.text });
        listView.setAdapter(serveradapter);
        listView.setOnItemClickListener(ItemClickListener);
        listView.setOnItemSelectedListener(itemSelect);
        listView.setSelection(clickPos);
    }

    /**
     * Get samba in the local mapping files in the directory list
     * CNcomment:获取samba在本地映射目录中的文件列表
     * @param path
     */
    private void getDirectory(String path) {
        temp = 1;
        directorys = path;
        parentPath = path;
        currentFileString = path;
        getFiles(path);
    }

    /**
     * Get encapsulated into a list of key-value pairs
     * CNcomment:获取封装成key-value对的列表
     */
    private List<Map<String, Object>> getServer() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        cursor = sqlite.query(TABLE_NAME, new String[] { ID, NICK_NAME,
                                                         WORK_PATH, SERVER_IP, ACCOUNT, PASSWORD
                                                       }, null, null, null,
                              null, null);

        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            //          map.put(IMAGE, R.drawable.folder_file);
            //jly
            map.put(IMAGE, R.drawable.hisil_folder_file);
            String nickName = "\\\\"
                              + cursor.getString(cursor.getColumnIndex(NICK_NAME)) + "\\"
                              + cursor.getString(cursor.getColumnIndex(WORK_PATH));
            map.put(NICK_NAME, nickName);
            map.put(SHORT, 1);
            map.put(MOUNT_POINT, " ");
            map.put(SERVER_IP,
                    cursor.getString(cursor.getColumnIndex(SERVER_IP)));
            map.put(ACCOUNT, cursor.getString(cursor.getColumnIndex(ACCOUNT)));
            map.put(PASSWORD, cursor.getString(cursor.getColumnIndex(PASSWORD)));
            map.put(WORK_PATH,
                    cursor.getString(cursor.getColumnIndex(WORK_PATH)));
            list.add(map);
        }

        cursor.close();
        return list;
    }

    /**
     * create options menus
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // hide tab menu
        menu.add(Menu.NONE, MENU_TAB, 0, R.string.hide_tab);
        // add sub menu
        suboper = menu.addSubMenu(Menu.NONE, Menu.NONE, 0, R.string.operation);
        menu.add(Menu.NONE, MENU_ADD, 0, R.string.str_server);
        // default "Search Servers" when temp=0 on start
        menu.add(Menu.NONE, MENU_SEARCH, 0, R.string.search);// getResources().getString(R.string.search));
        menu.add(Menu.NONE, MENU_EDIT, 0,
                 getResources().getString(R.string.edit));
        menu.add(Menu.NONE, MENU_SHORT, 0, R.string.add_shortcut);
        return true;
    };

    public boolean onPrepareOptionsMenu(Menu menu) {
        String[] splitedPath = pathTxt.getText().toString().split("/");

        if (splitedPath.length == 1) {
            temp = 0;
            // menu.getItem(7).setEnabled(false);
            // menu.getItem(6).setEnabled(false);
            suboper.clear();

            if (!OPERATER_ENABLE) {
                menu.getItem(1).setVisible(true);
            }

            menu.getItem(1).setEnabled(true);
            suboper.add(Menu.NONE, MENU_DELETE, 0,
                        getResources().getString(R.string.delete));

            if (pathTxt.getText().toString().equals("")) {
                if (intList.size() == 0) {
                    menu.getItem(2).setEnabled(true);
                }
                else {
                    menu.getItem(2).setEnabled(false);
                }

                if (list.size() <= 1 || intList.size() == 1) {
                    menu.getItem(1).setEnabled(false);
                }
                else {
                    menu.getItem(1).setEnabled(true);
                }

                menu.getItem(3).setEnabled(false);

                if (listView.getSelectedItemPosition() > 0
                    && intList.size() == 0) {
                    menu.getItem(4).setEnabled(true);
                }
                else {
                    menu.getItem(4).setEnabled(false);
                }

                menu.getItem(5).setEnabled(false);
            }
            else {
                menu.getItem(1).setEnabled(false);
                menu.getItem(2).setEnabled(false);
                menu.getItem(3).setEnabled(false);
                menu.getItem(4).setEnabled(false);

                if (listView.getSelectedItemPosition() >= 0) {
                    if (hasTheShortCut()) {
                        menu.getItem(5).setEnabled(false);
                    }
                    else {
                        menu.getItem(5).setEnabled(true);
                    }
                }
                else {
                    menu.getItem(5).setEnabled(false);
                }
            }
        }
        else {
            temp = 1;

            // menu.getItem(7).setEnabled(true);
            // menu.getItem(6).setEnabled(true);
            if (hasTheShortCut()) {
                menu.getItem(5).setEnabled(false);
            }
            else {
                menu.getItem(5).setEnabled(true);
            }

            suboper.clear();

            if (!OPERATER_ENABLE) {
                menu.getItem(1).setVisible(false);
            }

            copyMenuItem = suboper.add(Menu.NONE, MENU_COPY, 0, getResources()
                                       .getString(R.string.copy));
            cutMenuItem = suboper.add(Menu.NONE, MENU_CUT, 0, getResources()
                                      .getString(R.string.cut));
            pasteMenuItem = suboper.add(Menu.NONE, MENU_PASTE, 0,
                                        getResources().getString(R.string.paste));
            deleteMenuItem = suboper.add(Menu.NONE, MENU_DELETE, 0,
                                         getResources().getString(R.string.delete));
            renameMenuItem = suboper.add(Menu.NONE, MENU_RENAME, 0,
                                         getResources().getString(R.string.str_rename));
            SharedPreferences share = getSharedPreferences("OPERATE",
                                                           SHARE_MODE);
            int num = share.getInt("NUM", 0);

            if (listFile.size() == 0) {
                if (num == 0) {
                    menu.getItem(1).setEnabled(false);
                }
                else {
                    menu.getItem(1).setEnabled(true);
                    pasteMenuItem.setEnabled(true);
                    copyMenuItem.setEnabled(false);
                    cutMenuItem.setEnabled(false);
                    deleteMenuItem.setEnabled(false);
                    renameMenuItem.setEnabled(false);
                }

                menu.getItem(3).setEnabled(false);
            }
            else {
                if (num == 0) {
                    menu.getItem(1).setEnabled(true);
                    pasteMenuItem.setEnabled(false);
                    copyMenuItem.setEnabled(true);
                    cutMenuItem.setEnabled(true);
                    deleteMenuItem.setEnabled(true);
                    renameMenuItem.setEnabled(true);
                }
                else {
                    menu.getItem(1).setEnabled(true);
                    pasteMenuItem.setEnabled(true);
                    copyMenuItem.setEnabled(true);
                    cutMenuItem.setEnabled(true);
                    deleteMenuItem.setEnabled(true);
                    renameMenuItem.setEnabled(true);
                }

                menu.getItem(3).setEnabled(true);
            }

            menu.getItem(2).setEnabled(true);
            Log.d("SambaActivity[onPrepareOptionsMenu]", "533::file_temp="
                  + temp);
            menu.getItem(4).setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_ADD:
                controlFlag = 0;
                showDialogs(MENU_ADD);
                // newDirOrFile(false);
                break;

                // search menu
            case MENU_SEARCH:
                if (temp == 1) {
                    Log.d("SambaActivity[onOptionsItemSelected]",
                          "619::call_searchFileDialog()");
                    searchFileDialog();
                }

                break;

                // cut menu
            case MENU_CUT:
                managerF(myPosition, MENU_CUT);
                break;

                // paste menu
            case MENU_PASTE:
                managerF(myPosition, MENU_PASTE);
                break;

                // delete menu
            case MENU_DELETE:
                managerF(myPosition, MENU_DELETE);
                break;

                // rename menu
            case MENU_RENAME:
                managerF(myPosition, MENU_RENAME);
                break;

                // copy menu
            case MENU_COPY:
                managerF(myPosition, MENU_COPY);
                break;

            case MENU_EDIT:
                controlFlag = 1;
                showDialogs(MENU_EDIT);
                break;

            case MENU_SHORT:
                addShortCut();
                break;

            case MENU_HELP:
                FileMenu.setHelpFlag(2);
                FileMenu.filterType(SambaActivity.this, MENU_HELP, null);
                break;
        }

        return true;
    }

    /**
     * According click menu displays a dialog box, perform different action
     * CNcomment:根据点击的菜单,显示对话框,执行不同的操作
     * @param item
     */
    private void showDialogs(final int item) {
        // LayoutInflater factory = LayoutInflater.from(SambaActivity.this);
        if (item == MENU_ADD || item == MENU_EDIT) {
            if (temp == 0) {
                dialog = new MyDialog(this, R.layout.new_server);
                dialog.show();
                // View myView01 = factory.inflate(R.layout.new_server, null);
                editServer = (EditText) dialog.findViewById(R.id.editServer);
                editName = (EditText) dialog.findViewById(R.id.editName);
                editpass = (EditText) dialog.findViewById(R.id.editpass);
                editdisplay = (EditText) dialog.findViewById(R.id.editdisplay);
                position = (EditText) dialog.findViewById(R.id.position);
                netCheck = (CheckBox) dialog.findViewById(R.id.add_shortcut);
                // position.setText(folder_position);
                netCheck.setVisibility(View.GONE);

                if (controlFlag == 1) {
                    serverName = pathTxt.getText().toString();
                    prevFolder = list.get(listView.getSelectedItemPosition())
                                 .get(WORK_PATH).toString();
                    position.setText(prevFolder);
                    prevServer = list.get(listView.getSelectedItemPosition())
                                 .get(SERVER_IP).toString();
                    editServer.setText(prevServer);
                    String nick = list.get(listView.getSelectedItemPosition())
                                  .get(NICK_NAME).toString();
                    editdisplay.setText(nick.substring(2, nick.substring(2)
                                                       .indexOf("\\") + 2));
                    cursor = sqlite.query(TABLE_NAME, new String[] { ID,
                                                                     SERVER_IP, ACCOUNT, PASSWORD
                                                                   }, NICK_NAME
                                          + "=? and " + WORK_PATH + "=?", new String[] {
                                              editdisplay.getText().toString(), prevFolder
                                          },
                                          null, null, null);

                    if (cursor.moveToFirst()) {
                        id = cursor.getInt(cursor.getColumnIndex(ID));
                    }

                    cursor.close();
                    prevUser = list.get(listView.getSelectedItemPosition())
                               .get(ACCOUNT).toString();

                    if (prevUser.equals("g")) {
                        editName.setText("");
                    }
                    else {
                        editName.setText(prevUser);
                    }

                    prevPass = list.get(listView.getSelectedItemPosition())
                               .get(PASSWORD).toString();
                    editpass.setText(prevPass);
                }

                myOkBut = (Button) dialog.findViewById(R.id.myOkBut);
                myCancelBut = (Button) dialog.findViewById(R.id.myCancelBut);
                myOkBut.setOnClickListener(butClick);
                myCancelBut.setOnClickListener(butClick);
            }
            else {
                // The focus is not on the server list, calling new or edit files
                // CNcomment:在服务器列表没有获得焦点时,调用新建或者编辑文件
                FileUtil util = new FileUtil(this);
                util.createNewDir(currentFileString);
            }
        }
    }

    OnClickListener butClick = new OnClickListener() {
        public void onClick(View v) {
            if (v.equals(myOkBut)) {
                myOkBut.setEnabled(false);
                Userserver = editServer.getText().toString();
                Username = editName.getText().toString();

                if (Username.equals("")) {
                    Username = "g";
                }

                Userpass = editpass.getText().toString();
                display = jni.getPcName(Userserver);

                if (display.equals("ERROR")) {
                    display = Userserver;
                }

                editdisplay.setText(display);
                folder_position = position.getText().toString().toUpperCase();

                if (folder_position.startsWith("/")) {
                    folder_position = folder_position.substring(1);
                }

                if (folder_position.endsWith("/")) {
                    folder_position = folder_position.substring(0,
                                                                folder_position.length() - 1);
                }

                if (Userserver.trim().equals("")) {
                    myOkBut.setEnabled(true);
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.input_server));
                }
                else if (folder_position.trim().equals("")) {
                    myOkBut.setEnabled(true);
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.work_path_null));
                }
                else {
                    doMount();
                }
            }
            else {
                dialog.cancel();
            }
        }
    };

    /**
     * Perform mount operation
     * CNcomment:执行挂载操作
     */
    private void doMount() {
        // for extend the mount time
        // StringBuilder builder = new StringBuilder(Userserver);
        builder = new StringBuilder(Userserver);
        builder.append("/").append(folder_position);

        if (controlFlag == 1) {
            boolean bServer = prevServer.equals(Userserver);
            boolean bDir = prevFolder.equals(folder_position);
            boolean bUser = prevUser.equals(Username);
            boolean bPass = prevPass.equals(Userpass);

            if (bDir && bUser && bPass && bServer) {
                dialog.cancel();
            }
            else {
                cursor = sqlite
                         .query(TABLE_NAME, new String[] { ID }, SERVER_IP
                                + "=? and " + WORK_PATH + "=?", new String[] {
                                    Userserver, folder_position
                                }, null, null, null);

                if (cursor.moveToFirst()) {
                    myOkBut.setEnabled(true);
                    FileUtil.showToast(this, getString(R.string.shortcut_exist));
                }
                else {
                    progress = new MyProDialog(this);
                    progress.setOnKeyListener(new OnKeyListener() {
                        public boolean onKey(DialogInterface arg0, int arg1,
                        KeyEvent arg2) {
                            return true;
                        }
                    });
                    progress.show();
                    MountThread thread = new MountThread(MOUNT_RESULT_2);
                    thread.start();
                }

                cursor.close();
            }
        }
        else {
            cursor = sqlite.query(TABLE_NAME, new String[] { ID }, NICK_NAME
                                  + "=? and " + WORK_PATH + "=?", new String[] { display,
                                                                                 folder_position
                                                                               }, null, null, null);

            if (cursor.moveToFirst()) {
                myOkBut.setEnabled(true);
                FileUtil.showToast(this, getString(R.string.shortcut_exist));
            }
            else {
                localPath = jni.getMountPoint(builder.toString());

                if (!localPath.equals("ERROR")) {
                    showLading();
                    dialog.cancel();
                }
                else {
                    progress = new MyProDialog(this);
                    progress.setOnKeyListener(new OnKeyListener() {
                        public boolean onKey(DialogInterface arg0, int arg1,
                        KeyEvent arg2) {
                            return true;
                        }
                    });
                    progress.show();
                    MountThread thread = new MountThread(MOUNT_RESULT_1);
                    thread.start();
                }
            }

            cursor.close();
        }
    }

    private void showLading() {
        progress = new MyProDialog(SambaActivity.this);
        progress.setTitle(getResources().getString(R.string.adding_server));
        progress.setMessage(getResources().getString(R.string.please_waitting));
        progress.show();
        Thread threas = new Thread(SambaActivity.this);
        threas.start();
    }

    public void run() {
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH_RESULT:
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    synchronized (lock) {
                        if (arrayFile.size() == 0 && arrayDir.size() == 0) {
                            FileUtil.showToast(SambaActivity.this,
                                               getString(R.string.no_search_file));
                            return;
                        }
                        else {
                            updateList(true);
                        }
                    }

                    break;

                case UPDATE_LIST:
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    if (listView.getVisibility() == View.VISIBLE) {
                        adapter = new FileAdapter(SambaActivity.this, listFile,
                                                  listlayout);
                        listView.setAdapter(adapter);
                        listView.setOnItemSelectedListener(itemSelect);
                        listView.setOnItemClickListener(ItemClickListener);
                    }
                    else if (gridView.getVisibility() == View.VISIBLE) {
                        adapter = new FileAdapter(SambaActivity.this, listFile,
                                                  gridlayout);
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(ItemClickListener);
                        gridView.setOnItemSelectedListener(itemSelect);
                    }

                    fill(new File(currentFileString));
                    break;

                case ISO_MOUNT_SUCCESS:
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    intList.add(myPosition);
                    getFiles(mISOLoopPath);
                    break;

                case ISO_MOUNT_FAILD:
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.new_mout_fail));
                    break;

                case END_SEARCH:
                case NET_NORMAL:
                    Log.d(TAG, "2398::dismiss_dialog");

                    if (SambaActivity.this != null
                    && !SambaActivity.this.isFinishing()) {
                        pgsDlg.dismiss();
                    }

                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                    }

                    if (!strWorkgrpups.toLowerCase().equals("error")) {
                        Log.d(TAG, "2424::strWorkgroups=" + strWorkgrpups);

                        if (willClickNetDir) {
                            intList.add(myPosition);
                        }

                        updateServerListAfterParse(strWorkgrpups);
                    }
                    else {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.net_time_out));
                    }

                    break;

                case NET_ERROR:
                    if (SambaActivity.this != null
                    && !SambaActivity.this.isFinishing()) {
                        pgsDlg.dismiss();
                    }

                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                    }

                    strWorkgrpups = "";
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.no_server));
                    break;

                    // for treatment the result of mount
                case MOUNT_RESULT_1:
                    myOkBut.setEnabled(true);

                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    if (result == 0) {
                        localPath = jni.getMountPoint(builder.toString());
                        showLading();
                        dialog.cancel();
                    }
                    else if (result == USER_OR_PASS_ERROR) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.user_or_pass));
                    }
                    else if (result == NET_ERROR) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.network_error));
                    }
                    else {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.new_server_error));
                    }

                    break;

                case MOUNT_RESULT_2:
                    myOkBut.setEnabled(true);

                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    if (result == 0) {
                        builder = new StringBuilder(Userserver);
                        builder.append("/").append(folder_position);
                        localPath = jni.getMountPoint(builder.toString());
                        showLading();
                        dialog.cancel();
                    }
                    else if (result == USER_OR_PASS_ERROR) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.user_or_pass));
                    }
                    else if (result == NET_ERROR) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.network_error));
                    }
                    else {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.new_server_error));
                    }

                    break;

                case MOUNT_RESULT_3:
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    if (result == 0) {
                        if (allOrShort == -1) {
                            showData(builder.toString(), sureDialog, myPosition);
                        }
                        else {
                            cursor = sqlite.query(TABLE_NAME, new String[] { ID,
                                                                             ACCOUNT, PASSWORD, NICK_NAME
                                                                           }, SERVER_IP
                                                  + " =? and " + WORK_PATH + " = ?",
                                                  new String[] { Userserver, folder_position },
                                                  null, null, null);

                            if (cursor.moveToFirst()) {
                                showData(builder.toString(), sureDialog, myPosition);
                            }
                            else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(
                                    SambaActivity.this);
                                alert.setMessage(getString(R.string.is_add_short));
                                alert.setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                        DialogInterface dialog,
                                    int which) {
                                        addShortCut();
                                        showData(builder.toString(),
                                                 sureDialog, myPosition);
                                    }
                                });
                                alert.setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                        DialogInterface dialog,
                                    int which) {
                                        showData(builder.toString(),
                                                 sureDialog, myPosition);
                                    }
                                });
                                sureDialog = alert.create();
                                sureDialog.show();
                            }

                            cursor.close();
                        }
                    }
                    else {
                        if (result == NET_ERROR) {
                            FileUtil.showToast(SambaActivity.this,
                                               getString(R.string.network_error));
                        }
                        else {
                            if (allOrShort == -1) {
                                mountShortCut(myPosition);
                            }
                            else {
                                showSetNewMountDlg(list.get(intList.get(0)),
                                                   workFolderList.get(myPosition), myPosition);
                            }
                        }
                    }

                    break;

                case MOUNT_RESULT_4:
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    if (result == 0) {
                        // workFlag = true;
                        // 在成功挂载后对点击的服务器置空，
                        SambaActivity.this.clickedNetServer = null;
                        builder = new StringBuilder(Userserver);
                        builder.append("/").append(folder_position);
                        String returnStr = jni.getMountPoint(builder.toString());

                        if (returnStr.equals("ERROR")) {
                            FileUtil.showToast(SambaActivity.this,
                                               getString(R.string.user_or_pass));
                        }
                        else {
                            localPath = returnStr;

                            if (netCheck.isChecked()) {
                                cursor = sqlite.query(TABLE_NAME,
                                                      new String[] { ID }, SERVER_IP + " =? and "
                                                      + WORK_PATH + " = ?", new String[] {
                                                          Userserver, folder_position
                                                      },
                                                      null, null, null);

                                if (cursor.moveToFirst()) {
                                    int id = cursor.getInt(cursor
                                                           .getColumnIndex(ID));
                                    ContentValues values = new ContentValues();
                                    // values.put(MOUNT_POINT, localPath);
                                    values.put(ACCOUNT, Username);
                                    values.put(PASSWORD, Userpass);
                                    sqlite.update(TABLE_NAME, values, ID + "=?",
                                                  new String[] { String.valueOf(id) });
                                }
                                else {
                                    setValues(Userserver, display,
                                              folder_position.replace("/", "\\"),
                                              localPath, Username, Userpass, 1);
                                }

                                cursor.close();
                            }

                            // Modify mount directory location
                            // CNcomment:修改挂载目录位置
                            clickedServerDirItem.put(MOUNT_POINT, localPath);
                            FileUtil.showToast(SambaActivity.this,
                                               getString(R.string.new_mout_successfully));
                            // Get local mapping mount directory contents
                            // CNcomment:获取本地映射挂载目录中的内容
                            getDirectory(localPath);

                            if (getFileFlag(currentFileString, prePath) == 1) {
                                intList.add(myPosition);
                            }
                        }
                    }
                    else if (result == USER_OR_PASS_ERROR) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.user_or_pass));
                    }
                    else if (result == NET_ERROR) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.network_error));
                    }
                    else {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.new_server_error));
                    }

                    dialog.dismiss();
                    break;

                default:
                    progress.dismiss();
                    ContentValues values = new ContentValues();
                    values.put(SERVER_IP, Userserver);
                    values.put(ACCOUNT, Username);
                    values.put(PASSWORD, Userpass);
                    values.put(WORK_PATH, folder_position.replace("/", "\\"));
                    values.put(NICK_NAME, display);
                    // values.put(DIR_HAS_MOUNTED, 1);
                    Map<String, Object> map = null;

                    if (controlFlag == 0) {
                        sqlite.insert(TABLE_NAME, ID, values);
                        boolean flag = false;

                        for (Map<String, Object> maps : list) {
                            if (!maps.containsValue(display)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            map = new HashMap<String, Object>();
                            //                      map.put(IMAGE, R.drawable.folder_file);
                            //jly
                            map.put(IMAGE, R.drawable.hisil_folder_file);
                            String nickName = "\\\\" + display + "\\"
                                              + folder_position.replace("/", "\\");
                            map.put(NICK_NAME, nickName);
                            map.put(SHORT, 1);
                            map.put(MOUNT_POINT, localPath);
                            map.put(SERVER_IP, Userserver);
                            map.put(ACCOUNT, Username);
                            map.put(PASSWORD, Userpass);
                            map.put(WORK_PATH, folder_position);
                            list.add(map);
                        }
                    }
                    else {
                        sqlite.update(TABLE_NAME, values, ID + "=?",
                                      new String[] { String.valueOf(id) });
                        map = new HashMap<String, Object>();
                        //jly
                        //                  map.put(IMAGE, R.drawable.folder_file);
                        map.put(IMAGE, R.drawable.hisil_folder_file);
                        String nickName = "\\\\" + display + "\\"
                                          + folder_position.replace("/", "\\");
                        map.put(NICK_NAME, nickName);
                        map.put(SHORT, 1);
                        map.put(MOUNT_POINT, localPath);
                        map.put(SERVER_IP, Userserver);
                        map.put(ACCOUNT, Username);
                        map.put(PASSWORD, Userpass);
                        map.put(WORK_PATH, folder_position);

                        if (pathTxt.getText().toString().equals("")) {
                            list.set(listView.getSelectedItemPosition(), map);
                        }
                    }

                    pathTxt.setText("");
                    intList.clear();
                    servers();
                    listView.requestFocus();
                    highlightLastItem(listView, list.size());
                    break;
            }
        }
    };

    // Click identifies whether a network server or a network folder or
    // CNcomment:标识是否点击的是网络服务器或者是或者是网络文件夹
    boolean willClickNetDir = false;

    /**
     * GridView_ItemClickListener
     */
    private OnItemClickListener ItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position,
        long id) {
            if (IsNetworkDisconnect()) {
                return;
            }

            if (listFile.size() > 0) {
                if (position >= listFile.size()) {
                    position = listFile.size() - 1;
                }
            }

            myPosition = position;
            Log.d(TAG, "1107::server_list_item_clicked");
            String[] splitedPath = pathTxt.getText().toString().split("/");
            TextView text = (TextView) v.findViewById(R.id.text);

            if (parent.equals(listView) && (splitedPath.length == 1)) {
                fileL.clear();

                if (pathTxt.getText().toString().equals("")
                && (intList.size() == 0)) {
                    if (list.get(position).get(SHORT).toString().equals("1")) {
                        Userserver = list.get(position).get(SERVER_IP)
                                     .toString();
                        folder_position = list.get(position).get(WORK_PATH)
                                          .toString().replace("\\", "/");
                        Username = list.get(position).get(ACCOUNT).toString();
                        Userpass = list.get(position).get(PASSWORD).toString();
                        String nick = list.get(position).get(NICK_NAME)
                                      .toString();
                        serverName = nick.substring(2, nick.length()
                                                    - folder_position.length() - 1);
                        mountPath(position, -1);
                    }
                    else {
                        clickPos = 0;
                        willClickNetDir = true;
                        searchServers();
                        // willClickNetDir = false;
                        // pathTxt.setText(R.string.all_network);
                    }
                }
                else if (intList.size() == 1) {
                    serverName = text.getText().toString();
                    // intList.add(position);
                    willClickNetDir = true;
                    loginName = "";
                    showServerFolderList(text.getText().toString(), "", "");
                }
                else if (intList.size() == 2) {
                    if (workFolderList != null) {
                        for (int i = 0; i < workFolderList.size(); i++) {
                            String str = workFolderList.get(i).get(WORK_PATH)
                                         .toString().replace("\\", "/");
                        }

                        Userserver = workFolderList.get(position)
                                     .get(SERVER_IP).toString();
                        folder_position = workFolderList.get(position)
                                          .get(WORK_PATH).toString().replace("\\", "/")
                                          .toUpperCase();
                        cursor = sqlite.query(TABLE_NAME, new String[] { ID,
                                                                         ACCOUNT, PASSWORD, NICK_NAME
                                                                       }, SERVER_IP
                                              + " =? and " + WORK_PATH + " = ?",
                                              new String[] { Userserver, folder_position },
                                              null, null, null);

                        if (cursor.moveToFirst()) {
                            Username = cursor.getString(cursor
                                                        .getColumnIndex(ACCOUNT));
                            Userpass = cursor.getString(cursor
                                                        .getColumnIndex(PASSWORD));
                        }
                        else {
                            if (loginName != "") {
                                Username = loginName;
                                Userpass = loginPass;
                            }
                            else {
                                Username = "g";
                                Userpass = "";
                            }
                        }

                        cursor.close();
                        mountPath(position, 0);
                    }
                }
            }
            else {
                // for chmod the file
                chmodFile(listFile.get(position).getPath());

                if (listFile.get(position).canRead()) {
                    if (listFile.get(position).isDirectory()) {
                        intList.add(myPosition);
                        clickPos = 0;
                    }
                    else {
                        clickPos = position;
                    }

                    preCurrentPath = currentFileString;
                    keyBack = false;
                    getFiles(listFile.get(position).getPath());
                }
                else {
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.file_cannot_read));
                }
            }
        }
    };

    private void mountPath(final int position, int flag) {
        // for extend the mount time
        allOrShort = flag;
        builder = new StringBuilder(Userserver);
        // final StringBuilder builder = new StringBuilder(Userserver);
        builder.append("/").append(folder_position);
        String returnStr = jni.getMountPoint(builder.toString());

        if (returnStr.equals("ERROR")) {
            progress = new MyProDialog(this);
            progress.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface arg0, int arg1,
                KeyEvent arg2) {
                    return true;
                }
            });
            progress.show();
            MountThread thread = new MountThread(MOUNT_RESULT_3);
            thread.start();
        }
        else {
            localPath = returnStr;
            mountSdPath = localPath;
            clickPos = 0;
            File file = new File(localPath);

            if (file.isDirectory() && file.canRead()) {
                Log.e("ADD1165", "POSITION");
                intList.add(position);
            }

            myPosition = 0;
            getDirectory(returnStr);
        }
    }

    private void showData(String str, AlertDialog dialog, int position) {
        String returnStr = jni.getMountPoint(str.toString());
        localPath = returnStr;
        mountSdPath = localPath;
        clickPos = 0;
        File file = new File(localPath);

        if (file.isDirectory() && file.canRead()) {
            intList.add(position);
        }

        getDirectory(returnStr);
    }

    private void mountShortCut(int position) {
        List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        cursor = sqlite.query(TABLE_NAME, new String[] { ID, SERVER_IP,
                                                         WORK_PATH, NICK_NAME
                                                       }, SERVER_IP + " =? and " + WORK_PATH
                              + " = ?", new String[] { Userserver, folder_position }, null,
                              null, null);

        // The specific multiple shared directories added to the database
        // CNcomment:将具体的多个共享目录加入到数据库中
        if (cursor.moveToFirst()) {
            map = new HashMap<String, Object>();
            //          map.put(IMAGE, R.drawable.folder_file);
            //jly
            map.put(IMAGE, R.drawable.hisil_folder_file);
            String serverIp = cursor
                              .getString(cursor.getColumnIndex(SERVER_IP));
            map.put(SERVER_IP, serverIp);
            String workPath = cursor
                              .getString(cursor.getColumnIndex(WORK_PATH));
            map.put(WORK_PATH, workPath);
            map.put(NICK_NAME,
                    cursor.getString(cursor.getColumnIndex(NICK_NAME)));
            groupList.add(map);
        }

        cursor.close();

        if (groupList.size() == 0) {
            FileUtil.showToast(this, getString(R.string.quary_error));
        }
        else {
            showSetNewMountDlg(list.get(position), groupList.get(0), position);
        }
    }

    private void showServerFolderList(String nickName, String account,
                                      String pass) {
        List<Map<String, Object>> groupDetails;
        Username = account;
        Userpass = pass;

        if (loginName != "") {
            Username = loginName;
            Userpass = loginPass;
        }
        else {
            Username = "";
            Userpass = "";
        }

        String detailGroup = sambaTree.getDetailsBy(nickName, Username,
                                                    Userpass);

        if (null != detailGroup
            && detailGroup.equals("NT_STATUS_ACCESS_DENIED")) {
            nServerLogonDlg = new NewCreateDialog(SambaActivity.this);
            View view = inflateLayout(SambaActivity.this,
                                      R.layout.server_log_on);
            nServerLogonDlg.setView(view);
            // nServerLogonDlg.setTitle(title)
            nServerLogonDlg.setButton(DialogInterface.BUTTON_POSITIVE,
                                      getString(R.string.ok), new DialogClickListener(nickName));
            nServerLogonDlg.setButton(DialogInterface.BUTTON_NEGATIVE,
                                      getString(R.string.cancel), new DialogClickListener());
            nServerLogonDlg.show();
            nServerLogonDlg.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextAppearance(SambaActivity.this,
                               android.R.style.TextAppearance_Large_Inverse);
            nServerLogonDlg.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextAppearance(SambaActivity.this,
                               android.R.style.TextAppearance_Large_Inverse);
        }
        else if (null != detailGroup
                 && detailGroup.equals("NT_STATUS_LOGON_FAILURE")) {
            loginName = "";
            FileUtil.showToast(SambaActivity.this,
                               getString(R.string.login_server_error));
            return;
        }
        else if (null != detailGroup && detailGroup.equals("ERROR")) {
            FileUtil.showToast(SambaActivity.this,
                               getString(R.string.error_folder));
            return;
        }
        // Show shared directory
        // CNcomment:显示共享目录
        else {
            if (null != detailGroup && !"".equals(detailGroup)
                && !detailGroup.toLowerCase().equals("error")) {
                // Parse multiple shared directory information
                // CNcomment:解析多个共享目录信息
                groupDetails = parse2DetailDirectories(detailGroup);
                workFolderList = new ArrayList<Map<String, Object>>();
                Map<String, Object> map = null;

                // The specific multiple shared directories added to the database
                // CNcomment:将具体的多个共享目录加入到数据库中
                for (int i = 0; i < groupDetails.size(); i++) {
                    map = new HashMap<String, Object>();
                    //                  map.put(IMAGE, R.drawable.folder_file);
                    map.put(IMAGE, R.drawable.hisil_folder_file);
                    //jly
                    String serverIp = groupDetails.get(i).get(SERVER_IP)
                                      .toString();
                    Log.d(TAG, "2826::server_ip=" + serverIp);
                    map.put(SERVER_IP, serverIp);
                    String workPath = groupDetails.get(i).get(WORK_PATH)
                                      .toString();
                    Log.d(TAG, "2830::work_path=" + workPath);
                    map.put(WORK_PATH, workPath);
                    map.put(NICK_NAME, nickName);
                    workFolderList.add(map);
                }

                if (willClickNetDir) {
                    intList.add(myPosition);
                }

                SimpleAdapter adapter = new SimpleAdapter(SambaActivity.this,
                                                          workFolderList, R.layout.file_row, new String[] {
                                                              IMAGE, WORK_PATH
                                                          }, new int[] {
                                                              R.id.image_Icon, R.id.text
                                                          });
                pathTxt.setText(nickName);
                Log.d(TAG, "1372::[showServerFolderList]workFolderList.size="
                      + workFolderList.size());
                Log.d(TAG, "1372::[showServerFolderList]listview=" + listView);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(ItemClickListener);
                listView.setOnItemSelectedListener(itemSelect);
            }
        }
    }

    private OnItemClickListener deleListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            ControlListAdapter adapter = (ControlListAdapter) l.getAdapter();
            CheckedTextView check = (CheckedTextView) v
                                    .findViewById(R.id.check);
            String path = adapter.getList().get(position).getPath();

            if (check.isChecked()) {
                selected.add(path);
            }
            else {
                selected.remove(path);
            }
        }
    };

    /**
     * 获取本地映射目录中所有文件信息
     * @param path
     */

    public void getFiles(String path) {
        // item = item01;
        openFile = new File(path);

        if (openFile.isDirectory()) {
            if (mIsSupportBD) {
                if (FileUtil.getMIMEType(openFile, this).equals("video/bd")) {
                    // launchHiBDPlayer(path);
                    preCurrentPath = "";
                    // currentFileString = path;
                    intList.remove(intList.size() - 1);
                    Intent intent = new Intent();
                    intent.setClassName("com.hisilicon.android.videoplayer",
                                        "com.hisilicon.android.videoplayer.activity.MediaFileListService");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.parse(path), "video/bd");
                    intent.putExtra("sortCount", sortCount);
                    intent.putExtra("isNetworkFile", isNetworkFile);
                    startService(intent);
                    return;
                }
                else if (FileUtil.getMIMEType(openFile, this).equals("video/dvd")) {
                    preCurrentPath = "";
                    // currentFileString = path;
                    intList.remove(intList.size() - 1);
                    Intent intent = new Intent();
                    intent.setClassName(
                        "com.hisilicon.android.videoplayer",
                        "com.hisilicon.android.videoplayer.activity.MediaFileListService");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.parse(path), "video/dvd");
                    intent.putExtra("sortCount", sortCount);
                    intent.putExtra("isNetworkFile", isNetworkFile);
                    startService(intent);
                    return;
                }
            }

            currentFileString = path;
            updateList(true);
        }
        else {
            if (openFile.isFile()) {
                super.openFile(this, openFile);
            }
            else {
                FileUtil.showToast(this, getString(R.string.network_error));
            }
        }
    };

    /**
     * @param files
     * @param fileroot
     */
    public void fill(File fileroot) {
        try {
            // li = new ArrayList<File>();//
            // li = adapter.getFiles();
            if (clickPos >= listFile.size()) {
                clickPos = listFile.size() - 1;
            }

            numInfo.setText((clickPos + 1) + "/" + listFile.size());

            if (!fileroot.getPath().equals(directorys)) {
                parentPath = fileroot.getParent();
                currentFileString = fileroot.getPath();
            }
            else {
                currentFileString = directorys;
            }

            if (listFile.size() == 0) {
                numInfo.setText(0 + "/" + 0);
            }

            /*
             * if ((listFile.size() == 0) && (showBut.findFocus() == null) &&
             * (filterBut.findFocus() == null)) { sortBut.requestFocus(); }
             */
            builder = new StringBuilder(Userserver);
            builder.append("/").append(folder_position);
            String tempStr = jni.getMountPoint(builder.toString());
            String display_path = fileroot.getPath().substring(
                                      tempStr.length(), fileroot.getPath().length());
            Log.i(TAG, "display_path" + display_path);
            pathTxt.setText(builder.toString() + display_path);
            // pathTxt.setText(serverName + fileroot.getPath());

            if (clickPos >= 0) {
                if (listView.getVisibility() == View.VISIBLE) {
                    listView.requestFocus();
                    listView.setSelection(clickPos);
                }
                else if (gridView.getVisibility() == View.VISIBLE) {
                    gridView.requestFocus();
                    gridView.setSelection(clickPos);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    AlertDialog attrServerDeletingDialog;

    /**
     * Thinning operations for menu operation: the file operation "Paste",
     * "Rename" and "Delete" on the server operating
     * 对菜单操作进行细化操作： 对文件操作 “粘贴”，“重命名”及“删除” 对服务器操作
     */
    private void managerF(final int position, final int item) {
        flagItem = item;

        if (temp == 1) { // operate file
            if (position >= listFile.size()) {
                myPosition = listFile.size() - 1;
            }

            if (item == MENU_PASTE) {
                getMenu(myPosition, item, listViews);
            }
            else if (item == MENU_RENAME) {
                View view = inflateLayout(SambaActivity.this,
                                          R.layout.samba_server_list_dlg_layout);
                alertDialog = new NewCreateDialog(SambaActivity.this);
                alertDialog.setView(view);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                      getString(R.string.ok), imageButClick);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                                      getString(R.string.cancel), imageButClick);
                alertDialog.show();
                alertDialog = FileUtil.setDialogParams(alertDialog,
                                                       SambaActivity.this);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextAppearance(SambaActivity.this,
                                   android.R.style.TextAppearance_Large_Inverse);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .requestFocus();
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextAppearance(SambaActivity.this,
                                   android.R.style.TextAppearance_Large_Inverse);
                listViews = (ListView) alertDialog
                            .findViewById(R.id.lvSambaServer);
                selected.clear();
                listViews.setItemsCanFocus(false);
                listViews.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listViews.setAdapter(new ControlListAdapter(SambaActivity.this,
                                                            listFile));
                listViews.setItemChecked(myPosition, true);
                listViews.setSelection(myPosition);
                selected.add(listFile.get(myPosition).getPath());
                listViews.setOnItemClickListener(deleListener);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .requestFocus();
            }
            else {
                View view = inflateLayout(SambaActivity.this,
                                          R.layout.samba_server_list_dlg_layout);
                alertDialog = new NewCreateDialog(SambaActivity.this);
                alertDialog.setView(view);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                      getString(R.string.ok), imageButClick);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                                      getString(R.string.cancel), imageButClick);
                alertDialog.show();
                alertDialog = FileUtil.setDialogParams(alertDialog,
                                                       SambaActivity.this);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextAppearance(SambaActivity.this,
                                   android.R.style.TextAppearance_Large_Inverse);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .requestFocus();
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextAppearance(SambaActivity.this,
                                   android.R.style.TextAppearance_Large_Inverse);
                listViews = (ListView) alertDialog
                            .findViewById(R.id.lvSambaServer);
                selected.clear();
                listViews.setItemsCanFocus(false);
                listViews.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listViews.setAdapter(new ControlListAdapter(SambaActivity.this,
                                                            listFile));
                listViews.setItemChecked(myPosition, true);
                listViews.setSelection(myPosition);
                selected.add(listFile.get(myPosition).getPath());
                listViews.setOnItemClickListener(deleListener);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .requestFocus();
            }
        }
        else {  // server
            int selectedPos = listView.getSelectedItemPosition() - 1;
            Log.d(TAG, "1571::managerF()_selectedPosition=" + selectedPos);

            if (selectedPos == -1) {
                selectedPos = 0;
            }

            View view = inflateLayout(SambaActivity.this,
                                      R.layout.samba_server_list_dlg_layout);
            attrServerDeletingDialog = new NewCreateDialog(SambaActivity.this);
            attrServerDeletingDialog.setView(view);
            attrServerDeletingDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                               getString(R.string.ok), new DialogClickListener(
                                                   SambaActivity.this, list));
            attrServerDeletingDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                                               getString(R.string.cancel), new DialogClickListener(
                                                   SambaActivity.this, list));
            attrServerDeletingDialog.show();
            attrServerDeletingDialog = FileUtil.setDialogParams(
                                           attrServerDeletingDialog, SambaActivity.this);
            //
            ListView lvServer = (ListView) attrServerDeletingDialog
                                .findViewById(R.id.lvSambaServer);
            lvServer.setAdapter(new SambaServerAdapter(SambaActivity.this,
                                                       convertMapToHashMap(list), -1));
            lvServer.setItemsCanFocus(false);
            lvServer.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lvServer.setClickable(true);
            lvServer.setItemChecked(selectedPos, true);
            attrServerDeletingDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .requestFocus();
            attrServerDeletingDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextAppearance(SambaActivity.this,
                               android.R.style.TextAppearance_Large_Inverse);
            attrServerDeletingDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .requestFocus();
            attrServerDeletingDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextAppearance(SambaActivity.this,
                               android.R.style.TextAppearance_Large_Inverse);
        }
    }

    /**
     * @param position
     * @param item
     * @param list
     */
    private void getMenu(final int position, final int item, final ListView list) {
        int selectionRowID = (int) position;
        File file = null;
        File myFile = new File(currentFileString);
        FileMenu menu = new FileMenu();
        SharedPreferences sp = getSharedPreferences("OPERATE", SHARE_MODE);

        if (item == MENU_RENAME) {
            fileArray = new ArrayList<File>();

            //file = new File(currentFileString + "/"
            //+ listFile.get(selectionRowID).getName());
            for (int i = 0; i < selected.size(); i++) {
                file = new File(selected.get(i));
                fileArray.add(file);
            }

            fileArray.add(file);
            menu.getTaskMenuDialog(SambaActivity.this, myFile, fileArray, sp,
                                   item, 0);
        }
        else if (item == MENU_PASTE) {
            fileArray = new ArrayList<File>();
            menu.getTaskMenuDialog(SambaActivity.this, myFile, fileArray, sp,
                                   item, 0);
        }
        else {
            fileArray = new ArrayList<File>();

            for (int i = 0; i < selected.size(); i++) {
                file = new File(selected.get(i));
                fileArray.add(file);
            }

            menu.getTaskMenuDialog(SambaActivity.this, myFile, fileArray, sp,
                                   item, 0);
        }
    }

    List<File> fileL = null;

    private DialogInterface.OnClickListener imageButClick = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                if (selected.size() > 0) {
                    getMenu(myPosition, flagItem, listViews);
                    alertDialog.cancel();
                }
                else {
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.select_file));
                }
            }
            else {
                alertDialog.cancel();
            }
        }
    };

    OnItemSelectedListener itemSelect = new OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view,
        int position, long id) {
            if (!pathTxt.getText().toString().equals("")
            && !pathTxt.getText().toString().equals(serverName)) {
                myPosition = position;
                numInfo.setText((position + 1) + "/" + listFile.size());
            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    File[] sortFile;
    /**
     * 根据不同排序,更新文件列表
     * @param sort
     */
    FileUtil util;

    public void updateList(boolean flag) {
        if (flag) {
            Log.i(TAG, "updateList");
            listFile.clear();
            showBut.setOnClickListener(clickListener);
            // sortBut.setOnClickListener(clickListener);
            filterBut.setOnClickListener(clickListener);

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            progress = new MyProDialog(SambaActivity.this);
            progress.show();

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

            waitThreadToIdle(thread);
            thread = new MyThread();
            thread.setStopRun(false);
            progress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    Log.v("\33[32m Main1", "onCancel" + "\33[0m");
                    thread.setStopRun(true);

                    if (keyBack) {
                        intList.add(clickPos);
                    }
                    else {
                        clickPos = myPosition;
                        currentFileString = preCurrentPath;
                        Log.v("\33[32m Main1", "onCancel" + currentFileString
                              + "\33[0m");
                        intList.remove(intList.size() - 1);
                    }

                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.cause_anr));
                }
            });
            thread.start();
        }
        else {
            adapter.notifyDataSetChanged();
            fill(new File(currentFileString));
        }

        // util = new FileUtil(this, filterCount, fileL, currentFileString);
        // util.fillData(sortCount, adapter);
    }

    int clickCount = 0;
    int clickPos = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = pathTxt.getText().toString().equals(serverName);

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                super.onKeyDown(KeyEvent.KEYCODE_ENTER, event);
                return true;

            case KeyEvent.KEYCODE_BACK:// KEYCODE_BACK
                keyBack = true;
                willClickNetDir = false;

                if (intList.size() == 0) {
                    clickCount++;

                    if (clickCount == 1) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.quit_app));
                    }
                    else if (clickCount == 2) {
                        SharedPreferences share = getSharedPreferences("OPERATE",
                                                                       SHARE_MODE);
                        share.edit().clear().commit();

                        if (FileUtil.getToast() != null) {
                            FileUtil.getToast().cancel();
                        }

                        onBackPressed();
                    }
                }
                else {
                    if (!pathTxt.getText().toString().equals("")) {
                        clickCount = 0;

                        if (currentFileString.equals("") && flag) {
                            pathTxt.setText("");
                            // servers();
                            willClickNetDir = false;
                            searchServers();
                        }
                        else if (currentFileString.equals(localPath)) {
                            numInfo.setText("");
                            // sortBut.setOnClickListener(null);
                            // sortBut.setImageResource(sortArray[0]);
                            sortCount = 0;
                            filterBut.setOnClickListener(null);
                            filterBut.setImageResource(filterArray[0]);
                            filterCount = 0;
                            gridView.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);
                            currentFileString = "";
                            listFile.clear();

                            if (list.get(intList.get(0)).get(SHORT).toString()
                                .equals("1")) {
                                pathTxt.setText("");
                                clickPos = intList.get(intList.size() - 1);
                                servers();
                            }
                            else {
                                // Reach shared directory layer
                                // 到达共享目录层
                                willClickNetDir = false;

                                if (loginName != "") {
                                    showServerFolderList(
                                        workFolderList
                                        .get(intList.get(intList.size() - 1))
                                        .get(NICK_NAME).toString(),
                                        loginName, loginPass);
                                }
                                else {
                                    showServerFolderList(
                                        workFolderList
                                        .get(intList.get(intList.size() - 1))
                                        .get(NICK_NAME).toString(),
                                        Username, Userpass);
                                }

                                pathTxt.setText(serverName);
                            }
                        }
                        else {
                            fileL.clear();

                            if (currentFileString.equals(ISO_PATH)) {
                                getFiles(prevPath);
                            }
                            else {
                                getFiles(parentPath);
                            }
                        }

                        if (intList.size() > 0) {
                            int pos = intList.size() - 1;

                            if (listView.getVisibility() == View.VISIBLE) {
                                clickPos = intList.get(pos);
                                listView.setSelection(clickPos);
                                intList.remove(pos);
                            }
                            else if (gridView.getVisibility() == View.VISIBLE) {
                                clickPos = intList.get(pos);
                                intList.remove(pos);
                            }
                        }
                    }
                    else {
                        servers();

                        if (intList.size() > 0) {
                            int pos = intList.size() - 1;

                            if (listView.getVisibility() == View.VISIBLE) {
                                clickPos = intList.get(pos);
                                listView.setSelection(clickPos);
                                intList.remove(pos);
                            }
                        }
                    }
                }

                return true;

            case KeyEvent.KEYCODE_SEARCH: // search
                if (!pathTxt.getText().toString().equals("")
                    && !pathTxt.getText().toString().equals(serverName)) {
                    searchFileDialog();
                }

                return true;

            case KeyEvent.KEYCODE_INFO: // info
                if (!pathTxt.getText().toString().equals("")
                    && !pathTxt.getText().toString().equals(serverName)) {
                    FileUtil util = new FileUtil(this);
                    util.showFileInfo(listFile.get(myPosition));
                }

                return true;

            case KeyEvent.KEYCODE_PAGE_UP:
                super.onKeyDown(keyCode, event);
                break;

            case KeyEvent.KEYCODE_PAGE_DOWN:
                super.onKeyDown(keyCode, event);
                break;
        }

        return false;
    }

    public String getCurrentFileString() {
        return currentFileString;
    }

    private void doCmdSystem(String strCmd) {
        SocketClient sktClint = new SocketClient(this, false);

        try {
            sktClint.writeMess("system " + strCmd);
            // sktClint.readNetResponseSync();
        }
        catch (Exception e) {
            Log.e(TAG, " doCmdSystem do " + strCmd + " ,,!error=" + e);
        }

        sktClint = null;
    }

    /*
     * search samba server
     * 搜索samba服务器
     */
    void searchServers() {
        // BEGIN : ADD FOR NOT FIND THE SAMBA SERVER
        doCmdSystem("rm /data/app/samba/var/locks/gencache.tdb");
        doCmdSystem("rm /data/app/samba/var/locks/gencache_notrans.tdb");
        // END : ADD FOR NOT FIND THE SAMBA SERVER
        sambaTree = new SambaTreeNative();
        timer = new Timer();
        waitLong = 0;
        Log.d(TAG, "2385::init_waitLong=" + waitLong);
        totalLong = 120 * 1000;
        pgsDlg = new ProgressDialog(SambaActivity.this);
        pgsDlg.setTitle(R.string.wait_str);
        pgsDlg.setMessage(getString(R.string.search_str));
        pgsDlg.show();
        Log.d(TAG, "2392::progress_dialog_shown");
        timer.schedule(new TimerTask() {
            public void run() {
                if (pgsDlg != null && pgsDlg.isShowing()) {
                    waitLong += 3000;// 没5秒中进行一次增加
                    Log.d(TAG, "2347::waitLong=" + waitLong);

                    // Message msg = new Message();
                    if (waitLong >= totalLong) {
                        Log.d(TAG, "2351::dismiss_progress_dialog");
                        strWorkgrpups = "error";
                        handler.sendEmptyMessage(END_SEARCH);
                        return;
                    }

                    Log.d(TAG, "2355::not_dismiss");

                    if (!"".equals(strWorkgrpups) && null != strWorkgrpups) {
                        if (strWorkgrpups.toLowerCase().equals("error")) {
                            handler.sendEmptyMessage(NET_ERROR);
                        }
                        else {
                            handler.sendEmptyMessage(NET_NORMAL);
                        }

                        return;
                    }
                }
                else {
                    return;
                }
            }
        }, 1000, 3000);
        // Open a thread for server search
        // CNcomment:开启一个线程，进行服务器搜索
        new Thread(new Runnable() {
            public void run() {
                Log.d(TAG, "2508::call_getWorkgroups()");

                if (strWorkgrpups == null || "".equals(strWorkgrpups)
                || strWorkgrpups.toLowerCase().equals("error")) {
                    Log.d(TAG, "2511::call_getWorkgroups()");
                    strWorkgrpups = sambaTree.getWorkgroups();
                }
            }
        }).start();
    }

    /**
     * On a formatted string to parse, analyze and then the pop-up
     * dialog box displays a list of strings parsed
     * CNcomment:对格式化的字符串进行解析，解析后再弹出对话框中 显示解析后的字符串列表
     * @param workgroups
     * @return
     */
    private void updateServerListAfterParse(String workgroup) {
        List<HashMap<String, Object>> groups = new ArrayList<HashMap<String, Object>>(
            1);

        if (!workgroup.equals("")) {
            String[] workgroups = workgroup.split("\\|");
            Log.d(TAG, "2481::workgroups_length=" + workgroups.length);
            HashMap<String, Object> detailMap;

            // Dividing the information
            // CNcomment:对信息进行分割
            for (int i = 0; i < workgroups.length; i++) {
                detailMap = new HashMap<String, Object>(1);
                // Analytical information for each pc
                // CNcomment:对每个pc信息进行解析
                String[] details = workgroups[i].split(":");
                String trimStr = details[0].trim();
                String pcName = trimStr.substring(
                                    trimStr.lastIndexOf("\\") + 1, trimStr.length());
                detailMap.put(NICK_NAME, pcName);

                if (details.length == 2) {
                    detailMap.put(SERVER_INTRO, details[1].trim());
                }
                else if (details.length == 1) {
                    detailMap.put(SERVER_INTRO, "No Details");
                }

                detailMap.put(IMAGE, R.drawable.mainfile);
                groups.add(detailMap);
            }
        }

        // Set the display server list
        // 设置显示服务器列表
        listSearchedServers(groups);
        Log.d(TAG, "2511::server_list_size=" + list.size());
    }

    AlertDialog serverDialog;

    /**
     * get samba server
     * @param groups
     */

    SambaServerAdapter smbAdapter;

    private void listSearchedServers(List<HashMap<String, Object>> groups) {
        Log.d(TAG, "2524::groups_size=" + groups.size());
        smbAdapter = new SambaServerAdapter(this, groups, 0);
        listView.setAdapter(smbAdapter);
        listView.setSelection(clickPos);
        listView.setOnItemClickListener(ItemClickListener);
        listView.setOnItemSelectedListener(itemSelect);
    }

    /**
     * samba-server
     * samba-server服务端适配器
     */
    static class SambaServerAdapter extends BaseAdapter {

        Context context;

        List<HashMap<String, Object>> groups;

        int flag;

        public SambaServerAdapter(Context context,
                                  List<HashMap<String, Object>> groups, int flag) {
            this.context = context;
            this.groups = groups;
            this.flag = flag;
        }

        public int getCount() {
            return groups.size();
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

            if (flag == 0) {
                RelativeLayout layout = (RelativeLayout) inflater.inflate(
                                            R.layout.file_row, null);
                ImageView img = (ImageView) layout
                                .findViewById(R.id.image_Icon);
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(groups.get(position).get(NICK_NAME).toString());
                img.setImageResource(R.drawable.mainfile);
                return layout;
            }
            else {
                CheckedTextView chktv = (CheckedTextView) inflater.inflate(
                                            R.layout.samba_server_checked_text_view, null);
                String textSamba = (String) groups.get(position).get(NICK_NAME);

                if (chktv.getWidth() < chktv.getPaint().measureText(textSamba)) {
                    chktv.setEllipsize(TruncateAt.MARQUEE);
                    chktv.setMarqueeRepeatLimit(-1);
                    chktv.setHorizontallyScrolling(true);
                }

                chktv.setText(textSamba);
                return chktv;
            }
        }
    }

    class DialogClickListener implements DialogInterface.OnClickListener {
        // Context context;

        List<Map<String, Object>> mainList;

        String svrName;

        public DialogClickListener() {
            Log.d(TAG, "2645::DialogClickListener_constructor_list=");
        }

        public DialogClickListener(String serverName) {
            this.svrName = serverName;
        }

        public DialogClickListener(Context context,
                                   List<Map<String, Object>> list) {
            this(context, null, list);
            Log.d(TAG, "2650::DialogClickListener_constructor_list=" + list);
        }

        public DialogClickListener(Context context,
                                   List<HashMap<String, Object>> groups,
                                   List<Map<String, Object>> mainList) {
            // this.context = context;
            // this.groups = groups;
            this.mainList = mainList;
            Log.d(TAG, "2659::DialogClickListener_constructor_list=" + mainList);
        }

        public void onClick(final DialogInterface dialog, int which) {
            if (nServerLogonDlg == dialog) {
                if (DialogInterface.BUTTON_POSITIVE == which) {
                    EditText edtServerAccount = (EditText)((AlertDialog) dialog)
                                                .findViewById(R.id.edtServerAccount);
                    EditText edtServerPassword = (EditText)((AlertDialog) dialog)
                                                 .findViewById(R.id.edtServerPassword);
                    loginName = edtServerAccount.getText().toString().trim();
                    loginPass = edtServerPassword.getText().toString().trim();

                    if (null == loginName || BLANK.equals(loginName)) {
                        // || null == password || BLANK.equals(password)) {
                        FileUtil.showToast(SambaActivity.this,
                                           getString(R.string.input_name_pass));
                        nServerLogonDlg.show();
                        return;
                    }

                    showServerFolderList(svrName, loginName, loginPass);
                    return;
                }

                if (DialogInterface.BUTTON_NEGATIVE == which) {
                    dialog.dismiss();
                    return;
                }
            }

            if (attrServerDeletingDialog == dialog) {
                Log.d(TAG, "2724::mainList=" + mainList);

                if (DialogInterface.BUTTON_POSITIVE == which) {
                    AlertDialog confirmDialog = new AlertDialog.Builder(
                        SambaActivity.this)
                    .setIcon(R.drawable.alert)
                    .setTitle(R.string.del_server_dlg_title)
                    .setMessage(R.string.comfirm_delete_hint)
                    .setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                            DialogInterface confirmDialog,
                        int which) {
                            doDeleteSeletedServers(dialog,
                                                   which, mainList);
                        }
                    })
                    .setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                            DialogInterface confirmDialog,
                        int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    confirmDialog.show();
                    return;
                }

                if (DialogInterface.BUTTON_NEGATIVE == which) {
                    dialog.dismiss();
                    return;
                }
            }
        }
    }

    /**
     * @param ip
     * @param nichName
     * @param workPath
     * @param mountPoint
     * @param account
     * @param password
     * @param isMounted
     * @return ContentValues
     */
    private void setValues(String ip, String nichName, String workPath,
                           String mountPoint, String account, String password, int isMounted) {
        ContentValues values = new ContentValues();
        values.put(NICK_NAME, nichName);
        values.put(SERVER_IP, ip);
        values.put(WORK_PATH, workPath);
        values.put(ACCOUNT, account);
        values.put(PASSWORD, password);
        // values.put(DIR_HAS_MOUNTED, isMounted);
        sqlite.insert(TABLE_NAME, ID, values);
        Map<String, Object> map = new HashMap<String, Object>();
        //      map.put(IMAGE, R.drawable.folder_file);
        //jly
        map.put(IMAGE, R.drawable.hisil_folder_file);
        String nickName = "\\\\" + nichName + "\\"
                          + workPath.replace("/", "\\");
        map.put(NICK_NAME, nickName);
        map.put(SHORT, 1);
        map.put(MOUNT_POINT, mountPoint);
        map.put(SERVER_IP, ip);
        map.put(ACCOUNT, account);
        map.put(PASSWORD, password);
        map.put(WORK_PATH, workPath);
        list.add(map);
    }

    /**
     * @param dialog
     * @param which
     */
    private void doDeleteSeletedServers(DialogInterface dialog, int which,
                                        List<Map<String, Object>> mainList) {
        ListView lvServerList = (ListView)((AlertDialog) dialog)
                                .findViewById(R.id.lvSambaServer);
        lvServerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvServerList.setItemsCanFocus(true);
        lvServerList.setClickable(true);
        List<String> deletedServerNames = new ArrayList<String>(1);
        int count = lvServerList.getCount();
        Log.d(TAG, "2978::doDeleteSeletedServers()_count=" + count);
        int flag = 0;
        boolean foo = true;
        String failedName = null;
        // SambaManager samba = null;// = (SambaManager)
        // getSystemService("Samba");

        for (int i = 0; i < count; i++) {
            Log.d(TAG, "2985::lvServerList_getCheckedItemPositions="
                  + lvServerList.getCheckedItemPositions());

            if (lvServerList.getCheckedItemPositions().get(i)) {
                String svrName = mainList.get(i + 1).get(NICK_NAME).toString();
                Log.d(TAG, "2992::SERVER_NAME=" + svrName);
                int index = svrName.indexOf("\\", 2);
                String nickname = svrName.substring(2, index);
                String workpath = svrName
                                  .substring(index + 1, svrName.length());
                cursor = sqlite.query(TABLE_NAME,
                                      new String[] { ID, SERVER_IP }, NICK_NAME + "=? and "
                                      + WORK_PATH + "=?", new String[] { nickname,
                                                                         workpath
                                                                       }, null, null, null);
                // According fetched from the database to get multiple
                // directory mount point umount mount from the server
                // CNcomment:根据从数据库获取到得多个目录挂载点，从服务器umount挂载
                String serverip = null;
                int id = 0;

                while (cursor.moveToNext()) {
                    serverip = cursor.getString(cursor
                                                .getColumnIndex(SERVER_IP));
                    id = cursor.getInt(cursor.getColumnIndex(ID));
                    StringBuilder builder = new StringBuilder(serverip);
                    builder.append("/").append(workpath);
                    // Get local mount path
                    // CNcomment:获得本地挂载路径
                    String returnStr = jni.getMountPoint(builder.toString());

                    if (!returnStr.equals("ERROR")) {
                        flag = jni.myUmount(returnStr);
                    }

                    Log.d(TAG, "3008::doDeleteSelectedServers()_flag=" + flag);

                    if (flag != 0) {
                        failedName = mainList.get(i + 1).get(NICK_NAME)
                                     .toString();
                        foo = false;
                        break;
                    }

                    Log.d(TAG,
                          "3014::selected_name="
                          + mainList.get(i).get(NICK_NAME).toString());
                }

                if (flag == 0) {
                    deletedServerNames.add(svrName);// umount成功后，在删除名列表中添加删除的名字
                    int deleteNums = sqlite.delete(TABLE_NAME, ID + "=?",
                                                   new String[] { String.valueOf(id) });
                    Log.d(TAG, "3022::deleted_name_numbers=" + deleteNums);
                }

                cursor.close();

                if (flag != 0) {
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.pos_delete_failed, failedName));
                }
            }
        }// end for

        for (int j = 0; j < deletedServerNames.size(); j++) {
            Log.d(TAG, "3033::delete_name=" + deletedServerNames.get(j));

            for (int k = 1; k < mainList.size(); k++) {
                Log.d(TAG,
                      "3035::deleted_name_in_list="
                      + mainList.get(k).get(NICK_NAME).toString());

                if (deletedServerNames.get(j).equals(
                        mainList.get(k).get(NICK_NAME).toString())) {
                    mainList.remove(k);
                    break;
                }
            }
        }

        if (deletedServerNames.size() == 0) {
            FileUtil.showToast(SambaActivity.this, getString(R.string.delete_none));
        }
        else if (foo) {
            FileUtil.showToast(SambaActivity.this, getString(R.string.delete_v));
        }

        // gridView.setVisibility(View.INVISIBLE);
        servers();
    }

    /**
     * Resolution was acquired under the group name of the shared directory information specific
     * CNcomment:解析根据group名获取到得具体的共享目录信息
     *
     * @param detailGroup
     * @return
     */
    private List<Map<String, Object>> parse2DetailDirectories(String detailGroup) {
        List<Map<String, Object>> detailInfos = new ArrayList<Map<String, Object>>(
            1);
        String[] details = detailGroup.split("\\|");
        HashMap<String, Object> map;
        String[] ipDetail = details[0].split(":");

        for (int i = 1; i < details.length; i++) {
            map = new HashMap<String, Object>(1);
            map.put(SERVER_IP, ipDetail[1]);
            String[] dirDetails = details[i].split(":");
            String dir = dirDetails[0].trim();
            String dirName = dir.substring(dir.lastIndexOf("\\") + 1,
                                           dir.length());
            map.put(WORK_PATH, dirName);
            map.put(MOUNT_POINT, "");
            Log.d(TAG, "2893::map=" + map);
            detailInfos.add(map);
        }

        Log.d(TAG, "2896::detailInfos=" + detailInfos);
        return detailInfos;
    }

    /**
     * The New mount point directory dialog box, click the server directory,
     * you need to create new mount points mount
     * CNcomment:显示新建挂载点目录对话框，在点击服务器目录后， 需要新建新建挂载点进行挂载
     *
     * @param clickedServerItem
     */
    private void showSetNewMountDlg(Map<String, Object> clickedNetServer,
                                    Map<String, Object> clickedServerItem, int clickPosition) {
        this.clickedNetServer = clickedNetServer;
        clickedServerDirItem = clickedServerItem;
        Log.d(TAG, "2893::showSetNewMountDlg_clickedNetServer="
              + clickedNetServer);
        Log.d(TAG, "2894::showSetNewMountDlg_clickedServerItem="
              + clickedServerItem);
        dialog = new MyDialog(this, R.layout.new_server);
        dialog.show();
        editServer = (EditText) dialog.findViewById(R.id.editServer);
        editServer.setText(clickedServerItem.get(SERVER_IP).toString());
        editServer.setFocusable(false);
        editServer.setClickable(false);
        // editServer.setFocusable(false);
        Log.d(TAG, "2924::serverUrl="
              + clickedServerItem.get(WORK_PATH).toString());
        editName = (EditText) dialog.findViewById(R.id.editName);
        // editName.setText(clickedNetServer.get(ACCOUNT).toString());
        editpass = (EditText) dialog.findViewById(R.id.editpass);
        // editpass.setText(clickedNetServer.get(PASSWORD).toString());
        editdisplay = (EditText) dialog.findViewById(R.id.editdisplay);
        netCheck = (CheckBox) dialog.findViewById(R.id.add_shortcut);
        editdisplay.setText(clickedServerItem.get(NICK_NAME).toString());
        editdisplay.setFocusable(false);
        position = (EditText) dialog.findViewById(R.id.position);
        position.setText(clickedServerItem.get(WORK_PATH).toString());
        position.setFocusable(false);
        position.setClickable(false);
        // position.setFocusable(false);

        // for while the net is error ,show the login dialog
        if (hasTheShortCut()) {
            netCheck.setChecked(true);
            netCheck.setVisibility(View.GONE);
        }
        else {
            netCheck.setVisibility(View.VISIBLE);
        }

        myOkBut = (Button) dialog.findViewById(R.id.myOkBut);
        myCancelBut = (Button) dialog.findViewById(R.id.myCancelBut);
        myOkBut.setOnClickListener(new ButtonSetOnNewShareDirListener(
                                       SambaActivity.this, dialog, clickedServerItem,
                                       clickedNetServer, clickPosition));
        myCancelBut.setOnClickListener(new ButtonSetOnNewShareDirListener(
                                           SambaActivity.this, dialog, clickedServerItem,
                                           clickedNetServer, clickPosition));
    }

    class ButtonSetOnNewShareDirListener implements View.OnClickListener {
        // Context context;

        Dialog newShareDialog;

        Map<String, Object> clickedServerDirItem;

        int clickPosition;

        public ButtonSetOnNewShareDirListener(Context context,
                                              Dialog newShareDialog, Map<String, Object> clickedServerItem,
                                              Map<String, Object> clickedNetServer, int clickPosition) {
            // this.context = context;
            this.newShareDialog = newShareDialog;
            this.clickedServerDirItem = clickedServerItem;
            // this.clickedNetServer1 = clickedNetServer;
            this.clickPosition = clickPosition;
        }

        public void onClick(View v) {
            if (v.getId() == R.id.myOkBut) {
                Userserver = editServer.getText().toString();
                Username = editName.getText().toString();
                Userpass = editpass.getText().toString();
                display = editdisplay.getText().toString();
                folder_position = position.getText().toString().toUpperCase();

                if (null == Userserver || Userserver.trim().equals("")) {
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.input_server));
                    return;
                }

                if (null == folder_position || "".equals(folder_position)) {
                    FileUtil.showToast(SambaActivity.this,
                                       getString(R.string.work_path_null));
                    return;
                }

                progress = new MyProDialog(SambaActivity.this);
                progress.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(DialogInterface arg0, int arg1,
                    KeyEvent arg2) {
                        return true;
                    }
                });
                progress.show();
                MountThread thread = new MountThread(MOUNT_RESULT_4);
                thread.start();
                return;
            }

            if (v.getId() == R.id.myCancelBut) {
                newShareDialog.dismiss();
                return;
            }
        }
    }

    public ListView getListView() {
        return listView;
    }

    /**
     * @brief : In samba, select and open a file view. Click back,
     * back to the server list page, click again to enter the server, forced exit pages
     * CNcomment:在samba中，选择并打开一个文件查看完点击返回， 返回到服务器列表页面，再次点击进入该服务器，页面强制退出
     */
    protected void onDestroy() {
        if (cursor != null) {
            cursor.close();
            sqlite.close();
        }

        super.onDestroy();
    }

    /**
     * @param context
     * @param resId
     * @return
     */
    private View inflateLayout(Context context, int resId) {
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resId, null);
        return view;
    }

    /**
     * @param listMap
     * @return
     */
    private List<HashMap<String, Object>> convertMapToHashMap(
        List<Map<String, Object>> listMap) {
        List<HashMap<String, Object>> lst = new ArrayList<HashMap<String, Object>>(
            1);
        Log.d(TAG, "3126::convertMapToHashMap()_listMap.size=" + listMap);
        HashMap<String, Object> map = null;

        for (int i = 1; i < listMap.size(); i++) {
            map = (HashMap<String, Object>) listMap.get(i);
            lst.add(map);
        }

        return lst;
    }

    /**
     * @param lvList
     * @param dataSize
     */
    private void highlightLastItem(ListView lvList, int dataSize) {
        lvList.smoothScrollToPosition(dataSize - 1);
        lvList.setSelection(dataSize - 1);
    }

    /**
     * To determine whether successfully entered
     * CNcomment:判断是否成功进入
     * @param currentPath
     * @param prePath
     * @return
     */
    public int getFileFlag(String currentPath, String prePath) {
        // enter sub directory
        // CNcomment:进入子目录
        if (currentPath.length() > prePath.length()) {
            prePath = currentPath;
            return 1;
        }
        // return parent directory
        // CNcomment:返回父目录
        else if (currentPath.length() < prePath.length()) {
            prePath = currentPath;
            return -1;
        }
        else {
            return 0;
        }
    }

    private void addShortCut() {
        cursor = sqlite.query(TABLE_NAME, new String[] { ID }, NICK_NAME
                              + "=? and " + WORK_PATH + "=?", new String[] { serverName,
                                                                             folder_position
                                                                           }, null, null, null);

        // Already exists, but the user name, password change
        // CNcomment:已存在，但是用户名、密码被改变
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            ContentValues values = new ContentValues();
            values.put(ACCOUNT, Username);
            values.put(PASSWORD, Userpass);
            sqlite.update(TABLE_NAME, values, ID + "=?",
                          new String[] { String.valueOf(id) });
        }
        else {
            setValues(Userserver, serverName,
                      folder_position.replace("/", "\\"), " ", Username,
                      Userpass, 1);
        }

        FileUtil.showToast(this, getString(R.string.add_short_succ));
        cursor.close();
    }

    private boolean hasTheShortCut() {
        String nick = "\\\\" + serverName + "\\"
                      + folder_position.replace("/", "\\");

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).containsValue(nick)) {
                return true;
            }
        }

        return false;
    }

    // broken into the directory contains many files,click again error
    class MyThread extends MyThreadBase {

        public void run() {
            if (getFlag()) {
                setFlag(false);

                synchronized (lock) {
                    util = new FileUtil(SambaActivity.this, filterCount,
                                        arrayDir, arrayFile, currentFileString);
                }
            }
            else {
                Log.i(TAG, "isSearch = false");
                util = new FileUtil(SambaActivity.this, filterCount,
                                    currentFileString);
            }

            listFile = util.getFiles(sortCount, "net");
            Log.v("\33[32m UTIL", "getRunFlag()" + getStopRun() + "\33[0m");

            if (getStopRun()) {
                if (keyBack) {
                    if (pathTxt.getText().toString()
                        .substring(serverName.length()).equals(ISO_PATH)) {
                        currentFileString = util.currentFilePath;
                    }
                }
            }
            else {
                currentFileString = util.currentFilePath;
                Log.v("\33[32m SMB", " listFile.size(1) " + listFile.size()
                      + "\33[0m");
                handler.sendEmptyMessage(UPDATE_LIST);
            }
        }

        public File getMaxFile(List<File> listFile) {
            int temp = 0;

            for (int i = 0; i < listFile.size(); i++) {
                if (listFile.get(temp).length() <= listFile.get(i).length())
                { temp = i; }
            }

            return listFile.get(temp);
        }
    }


    public Handler getHandler() {
        return handler;
    }

    public void operateSearch(boolean b) {
        if (b) {
            for (int i = 0; i < fileArray.size(); i++) {
                listFile.remove(fileArray.get(i));
            }
        }
    }

    protected void onStop() {
        super.onStop();
        super.cancleToast();
    }

    public TextView getPathTxt() {
        return pathTxt;
    }

    public String getServerName() {
        return serverName;
    }


    // extend the mount timeout to 30s
    class MountThread extends Thread {

        private int mountFlag = 0;
        public MountThread(int mountFlag) {
            this.mountFlag = mountFlag;
        }

        public void run() {
            NativeSamba samba = null;

            switch (mountFlag) {
                case MOUNT_RESULT_1:
                case MOUNT_RESULT_3:
                case MOUNT_RESULT_4:
                    result = jni.UImount(Userserver, folder_position, " ",
                                         Username, Userpass);
                    handler.sendEmptyMessage(mountFlag);
                    /*
                     * if(result == 0) handler.sendEmptyMessage(mountFlag); else
                     * handler.sendEmptyMessage(NET_ERROR);
                     */
                    break;

                case MOUNT_RESULT_2:
                    builder = new StringBuilder(prevServer);
                    builder.append("/").append(prevFolder);
                    String returnStr = jni.getMountPoint(builder.toString());

                    if (returnStr.equals("ERROR")) {
                        result = jni.UImount(Userserver, folder_position, " ",
                                             Username, Userpass);
                    }
                    else {
                        int code = jni.myUmount(returnStr);
                        result = jni.UImount(Userserver, folder_position, " ",
                                             Username, Userpass);
                    }

                    handler.sendEmptyMessage(MOUNT_RESULT_2);
                    /*
                     * if(result == 0) handler.sendEmptyMessage(MOUNT_RESULT_2);
                     * else handler.sendEmptyMessage(NET_ERROR);
                     */
                    break;
            }
        }
    }

    // for grally3D delete the file, flush the data
    protected void onResume() {
        super.onResume();

        if (!currentFileString.equals("")
            && preCurrentPath.equals(currentFileString)) {
            updateList(true);
        }
    }

    private static IMountService getMountService() {
        IBinder service = ServiceManager.getService("mount");

        if (service != null) {
            return IMountService.Stub.asInterface(service);
        }
        else {
            Log.e(TAG, "Can't get mount service");
        }

        return null;
    }
}
