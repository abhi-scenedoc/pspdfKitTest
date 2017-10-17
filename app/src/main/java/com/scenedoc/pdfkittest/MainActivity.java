package com.scenedoc.pdfkittest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Using the configuration builder you can define options for the activity.
        final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(this).build();

        // Launch the activity, viewing the PDF document directly from the assets.
        PdfActivity.showDocument(this, Uri.parse("file:///android_asset/my-document.pdf"), config);
    }
}
