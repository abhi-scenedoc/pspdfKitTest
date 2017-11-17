package com.scenedoc.pdfkittest;

/**
 * Created by abhishekpatel on 2017-10-20.
 */

import android.content.Context;

import com.pspdfkit.ui.toolbar.AnnotationCreationToolbar;


public class OverrideAnnotation extends AnnotationCreationToolbar {
    public OverrideAnnotation(Context context) {
        super(context);
        this.closeButton.setVisibility(GONE);
    }
}
