package com.um.videoplayer.utility.base;

import java.io.File;

import com.um.videoplayer.utility.FileTool;

public class ImageFileFilter implements java.io.FileFilter {
    private static final String MIME_IMAGE = "jpg,jpeg,png,gif";

    public boolean accept(File pFile) {
        if (MIME_IMAGE.contains(FileTool.getExtension(pFile))) {
            return true;
        }

        return false;
    }
}
