
package com.um.gallery3d.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SlideshowDream extends Activity {
    @Override
    public void onCreate(Bundle bndl) {
        super.onCreate(bndl);
        Intent i = new Intent(Intent.ACTION_VIEW,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // Uri.fromFile(Environment.getExternalStoragePublicDirectory(
        // Environment.DIRECTORY_PICTURES)))
                .putExtra(Gallery.EXTRA_SLIDESHOW, true).setFlags(getIntent().getFlags());
        startActivity(i);
        finish();
    }
}
