package com.scenedoc.pdfkittest;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.pspdfkit.ui.toolbar.ContextualToolbar;
import com.pspdfkit.ui.toolbar.grouping.presets.MenuItem;
import com.pspdfkit.ui.toolbar.grouping.presets.PresetMenuItemGroupingRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishekpatel on 2017-10-20.
 */


public class MyCustomGrouping extends PresetMenuItemGroupingRule {


    public MyCustomGrouping(@NonNull Context context) {
        super(context);
    }

    @Override
    public List<MenuItem> getGroupPreset(@IntRange(from = ContextualToolbar.MIN_TOOLBAR_CAPACITY) int capacity, int itemsCount) {
        if (capacity < ContextualToolbar.MIN_TOOLBAR_CAPACITY) return new ArrayList<>(0);

        return (capacity <= 8) ? SEVEN_ITEMS_GROUPING : SEVEN_ITEMS_GROUPING;
    }

//    private static final List<MenuItem> FOUR_ITEMS_GROUPING = new ArrayList<>(4);
//
//    static {
//        FOUR_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_markup,
//                new int[]{
//                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_highlight,
//                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_squiggly,
//                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_strikeout,
//                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_underline}));
//        FOUR_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_image));
//    }

    /**
     * Annotation toolbar grouping with 7 elements.
     */
    private static final List<MenuItem> SEVEN_ITEMS_GROUPING = new ArrayList<>(8);

    static {
        SEVEN_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_markup,
                new int[]{
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_highlight,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_squiggly,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_strikeout,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_underline}));

        SEVEN_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_freetext));

        SEVEN_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_note));

        SEVEN_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_markup,
                new int[]{
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_ink,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_signature
                }));

        SEVEN_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_markup,
                new int[]{
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_line,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_circle,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_square,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_polygon,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_polyline}));

        SEVEN_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_markup,
                new int[]{
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_image,
                        com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_camera
                }));

        SEVEN_ITEMS_GROUPING.add(new MenuItem(com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_stamp));

    }
}