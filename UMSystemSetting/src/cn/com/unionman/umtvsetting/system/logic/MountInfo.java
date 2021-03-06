
package cn.com.unionman.umtvsetting.system.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.storage.IMountService;
import android.os.storage.StorageVolume;

import cn.com.unionman.umtvsetting.system.util.SocketClient;

/**
 * the info of local upgrade
 *
 * @author huyq
 */
public class MountInfo {
    private static final String TAG = "MountInfo";
    public String[] path = new String[64];
    public int[] type = new int[64];
    public String[] label = new String[64];
    public String[] partition = new String[64];
    public int index = 0;

    public MountInfo(Context context) {
        try {
            // support for DevType
            IBinder service = ServiceManager.getService("mount");
            if (service != null) {
                IMountService mountService = IMountService.Stub
                        .asInterface(service);
                List<android.os.storage.ExtraInfo> mountList = mountService
                        .getAllExtraInfos();
                index = mountList.size();

                for (int i = 0; i < index; i++) {
                    path[i] = mountList.get(i).mMountPoint;
                    if (mountList.get(i).mLabel != null) {
                        label[i] = mountList.get(i).mDiskLabel + ": "
                                + mountList.get(i).mLabel;
                    } else {
                        label[i] = mountList.get(i).mDiskLabel;
                    }
                    partition[i] = label[i];
                    String typeStr = mountList.get(i).mDevType;
                    if (path[i].contains("/mnt/nand")) {
                        type[i] = 2;
                    } else if (typeStr.equals("SDCARD")) {
                        type[i] = 2;
                    } else if (typeStr.equals("SATA")) {
                        type[i] = 1;
                    } else if (typeStr.equals("USB")) {
                        type[i] = 0;
                    } else if (typeStr.equals("USB2.0")) {
                        type[i] = 0;
                    }else if (typeStr.equals("USB3.0")) {
                        type[i] = 0;
                    }else if (typeStr.equals("UNKOWN")) {
                        type[i] = 3;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * get mount file path
     *
     * @param path
     * @return
     */
    public String getMountDevices(String path) {
        int start = 0;
        start = path.lastIndexOf("/");
        String mountPath = path.substring(start + 1);
        return mountPath;
    }

    private String[] getDevicePath(StorageVolume[] storageVolumes) {
        String[] tmpPath = new String[storageVolumes.length];
        for (int i = 0; i < storageVolumes.length; i++) {
            tmpPath[i] = getMountDevices(storageVolumes[i].getPath());
        }
        int count = storageVolumes.length;
        // delete repeat
        for (int i = 0; i < storageVolumes.length; i++) {
            for (int j = i + 1; j < storageVolumes.length; j++) {
                try {
                    if (tmpPath[i] != null) {
                        if (tmpPath[j].equals(tmpPath[i]) && tmpPath[j] != null) {
                            tmpPath[j] = null;
                            count--;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        String[] path = new String[count];
        int j = 0;
        for (int i = 0; i < storageVolumes.length; i++) {
            if (tmpPath[i] != null) {
                path[j] = tmpPath[i];
                j++;
            }
        }
        // sort
        for (int i = 0; i < count; i++) {
            for (int k = i + 1; k < count; k++) {
                if (path[i].compareTo(path[k]) > 0) {
                    String tmp = path[k];
                    path[k] = path[i];
                    path[i] = tmp;
                }
            }
        }
        return path;
    }

    private String getUpdateFilePath(StorageVolume[] storageVolumes,
            String fileSuffix) {
        if (storageVolumes != null && storageVolumes.length > 0) {
            for (int i = 0; i < storageVolumes.length; i++) {
                if (storageVolumes[i].getPath().contains(fileSuffix)) {
                    return storageVolumes[i].getPath();
                }
            }
        }
        return "/mnt/nand";

    }

    public void isSata(int i) {
        SocketClient sc1 = new SocketClient();
        sc1.writeMsg("system find /sys/devices/platform/hiusb-ehci.0/usb1/1-1 -name \"sd*\" >> /mnt/SATA.txt");
        sc1.readNetResponseSync();

        String fileName = "/mnt/SATA.txt";
        File f = new File(fileName);
        long size = f.length();

        if (size > 0) {

            String out = "";
            out = readFile(fileName);

            String sataName = "";
            sataName = out.substring(out.lastIndexOf("/"));

            if (path[i].contains(sataName)) {
                type[i] = 0;

            } else {
                type[i] = 1;
            }

            SocketClient sc3 = new SocketClient();
            sc3.writeMsg("system rm /mnt/SATA.txt");
            sc3.readNetResponseSync();

        } else {
            type[i] = 1;
            SocketClient sc4 = new SocketClient();
            sc4.writeMsg("system rm /mnt/SATA.txt");
            sc4.readNetResponseSync();
        }
    }

    /**
     * read file by fileName
     *
     * @param fileName
     * @return
     */
    public String readFile(String fileName) {
        String output = "";
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isFile()) {

                try {
                    BufferedReader input = new BufferedReader(new FileReader(
                            file));
                    output = input.readLine();

                    input.close();
                } catch (IOException ioException) {
                    System.err.println("File Error!");
                }
            }
        } else {
            System.err.println("File Does Not Exit!");
        }
        return output;
    }
}
