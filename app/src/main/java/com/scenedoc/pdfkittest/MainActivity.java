package com.scenedoc.pdfkittest;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.pspdfkit.PSPDFKit;
import com.pspdfkit.annotations.Annotation;
import com.pspdfkit.annotations.AnnotationType;
import com.pspdfkit.annotations.appearance.AssetAppearanceStreamGenerator;
import com.pspdfkit.annotations.defaults.StampAnnotationDefaultsProvider;
import com.pspdfkit.annotations.stamps.StampPickerItem;
import com.pspdfkit.configuration.PdfConfiguration;
import com.pspdfkit.document.DocumentSaveOptions;
import com.pspdfkit.document.PdfDocument;
import com.pspdfkit.document.processor.NewPage;
import com.pspdfkit.document.processor.PdfProcessor;
import com.pspdfkit.document.processor.PdfProcessorTask;
import com.pspdfkit.exceptions.PSPDFKitInitializationFailedException;
import com.pspdfkit.listeners.DocumentListener;
import com.pspdfkit.preferences.PSPDFKitPreferences;
import com.pspdfkit.ui.PdfFragment;
import com.pspdfkit.ui.inspector.PropertyInspectorCoordinatorLayout;
import com.pspdfkit.ui.inspector.annotation.AnnotationCreationInspectorController;
import com.pspdfkit.ui.inspector.annotation.AnnotationEditingInspectorController;
import com.pspdfkit.ui.inspector.annotation.DefaultAnnotationCreationInspectorController;
import com.pspdfkit.ui.inspector.annotation.DefaultAnnotationEditingInspectorController;
import com.pspdfkit.ui.special_mode.controller.AnnotationCreationController;
import com.pspdfkit.ui.special_mode.controller.AnnotationEditingController;
import com.pspdfkit.ui.special_mode.controller.TextSelectionController;
import com.pspdfkit.ui.special_mode.manager.AnnotationManager;
import com.pspdfkit.ui.special_mode.manager.TextSelectionManager;
import com.pspdfkit.ui.toolbar.AnnotationCreationToolbar;
import com.pspdfkit.ui.toolbar.AnnotationEditingToolbar;
import com.pspdfkit.ui.toolbar.ContextualToolbar;
import com.pspdfkit.ui.toolbar.TextSelectionToolbar;
import com.pspdfkit.ui.toolbar.ToolbarCoordinatorLayout;
import com.pspdfkit.utils.Size;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DocumentListener, ToolbarCoordinatorLayout.OnContextualToolbarLifecycleListener, TextSelectionManager.OnTextSelectionModeChangeListener, AnnotationManager.OnAnnotationEditingModeChangeListener, AnnotationManager.OnAnnotationCreationModeChangeListener {
    private static final String TAG = "TestBench";
    private static final PdfConfiguration config = new PdfConfiguration.Builder().autosaveEnabled(false).build();
    private ToolbarCoordinatorLayout toolbarCoordinatorLayout;
    private AnnotationCreationToolbar annotationCreationToolbar;
    private TextSelectionToolbar textSelectionToolbar;
    private AnnotationEditingToolbar annotationEditingToolbar;
    private boolean annotationCreationActive = false;
    private PropertyInspectorCoordinatorLayout inspectorCoordinatorLayout;
    private AnnotationEditingInspectorController annotationEditingInspectorController;
    private AnnotationCreationInspectorController annotationCreationInspectorController;
    private PdfFragment fragment;
    private File outputFile;

    public static int getResourseId(Context context, String pVariableName,
                                    String pResourcename) {
        try {
            return context.getResources().getIdentifier(pVariableName,
                    pResourcename, context.getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            String value = "XTtApxw4u7m-KzUkC05GPlxltsgMID003scFquP-tzjC6QSsOM1UQbZoh9uqIy5zpcciZlXOj6EzaU5walEWVI5YKOf8H5XjuJsHD57UUW4UWtVDVnaWxkALYNmZI_qPVr_V1ISW39SUJ6rulQW1FZNzTS_zb89J67alZGXL6c7TsPFL_lwgw5xDEopXYG4CC9CcvQ72jMWpQCTYifwuzfHwDl6zclErx_hL9xsaIfeIQJWjt537GLABMjz_wF4DuqTNpnWG1zcDig84UCQqaBlCnpsYi4WLzJKHu5PD-_BspM_XJyCFR8VT0rr6cLOT40YcVY4ItaLyTH6frEU-yFI3P_skQusEnuTMezH9ACKsLKTktER4ocbnlieh0abNYujxHuqLD1St58Q6YTC7mns8Z3UFePBLLODBdUjR4Es0w7qsz5_0ayPI4Swsix7x";
            PSPDFKit.initialize(this, value);
        } catch (PSPDFKitInitializationFailedException e) {
            e.printStackTrace();
        }
        setupBlankFile();

        toolbarCoordinatorLayout = (ToolbarCoordinatorLayout) findViewById(R.id.toolbarCoordinatorLayout);
//
        annotationCreationToolbar = new OverrideAnnotation(this);
        textSelectionToolbar = new TextSelectionToolbar(this);
        annotationEditingToolbar = new AnnotationEditingToolbar(this);

        // Use this if you want to use annotation inspector with annotation creation and editing toolbars.
        inspectorCoordinatorLayout = (PropertyInspectorCoordinatorLayout) findViewById(R.id.inspectorCoordinatorLayout);
        annotationEditingInspectorController = new DefaultAnnotationEditingInspectorController(this, inspectorCoordinatorLayout);
        annotationCreationInspectorController = new DefaultAnnotationCreationInspectorController(this, inspectorCoordinatorLayout);

        fragment = (PdfFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = PdfFragment.newInstance(Uri.fromFile(outputFile), config);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
        fragment.addOnAnnotationCreationModeChangeListener(this);
        fragment.addOnAnnotationEditingModeChangeListener(this);
        fragment.addOnTextSelectionModeChangeListener(this);
        fragment.addDocumentListener(this);

        toolbarCoordinatorLayout.setOnContextualToolbarLifecycleListener(this);
        fragment.setAnnotationDefaultsProvider(AnnotationType.STAMP, new StampAnnotationDefaultsProvider(this) {
            @NonNull
            @Override
            public List<StampPickerItem> getStampsForPicker() {
                final List<StampPickerItem> stamps = new ArrayList<StampPickerItem>();
                try {

                    for (int i = 0; i < 4; i++) {
                        AssetAppearanceStreamGenerator appearanceStreamGenerator = new AssetAppearanceStreamGenerator("icon_" + i + ".pdf");

                        stamps.add(StampPickerItem.fromSubject(MainActivity.this, "Custom subject")
                                .withSize(StampAnnotationDefaultsProvider.DEFAULT_STAMP_ANNOTATION_PDF_WIDTH)
                                .withAppearanceStreamGenerator(appearanceStreamGenerator)
                                .build());
//                    stamps.add(StampPickerItem.fromBitmap(BitmapFactory.decodeResource(SketchDemo.this.getResources(),getResourseId(SketchDemo.this,"icon_"+i,"drawable"))).build());
//                    stamps.add(StampPickerItem.fromBitmap(getOptimumBitmap_Thumbnail(TestBench.this, getResourseId(TestBench.this, "icon_" + i, "drawable"))).build());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
//
                return stamps;
            }
        });

    }

    private void setupBlankFile() {
        outputFile = new File(getFilesDir(), "new-document.pdf");
        if (outputFile.exists()) {
            outputFile.delete();
        }


        PSPDFKitPreferences.get(this).setAnnotationCreator("Abhi");
        final PdfProcessorTask task = new PdfProcessorTask(NewPage.emptyPage(new Size(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight())).build());

        PdfProcessor.processDocument(task, outputFile);
    }


    @Override
    public void onDocumentLoaded(@NonNull PdfDocument pdfDocument) {
        fragment.enterAnnotationCreationMode();
    }

    @Override
    public void onDocumentLoadFailed(@NonNull Throwable throwable) {

    }

    @Override
    public boolean onDocumentSave(@NonNull PdfDocument pdfDocument, @NonNull DocumentSaveOptions documentSaveOptions) {
        return false;
    }

    @Override
    public void onDocumentSaved(@NonNull PdfDocument pdfDocument) {

    }

    @Override
    public void onDocumentSaveFailed(@NonNull PdfDocument pdfDocument, @NonNull Throwable throwable) {

    }

    @Override
    public void onDocumentSaveCancelled(PdfDocument pdfDocument) {

    }

    @Override
    public boolean onPageClick(@NonNull PdfDocument pdfDocument, int i, @Nullable MotionEvent motionEvent, @Nullable PointF pointF, @Nullable Annotation annotation) {
        return false;
    }

    @Override
    public boolean onDocumentClick() {
        return false;
    }

    @Override
    public void onPageChanged(@NonNull PdfDocument pdfDocument, int i) {

    }

    @Override
    public void onDocumentZoomed(@NonNull PdfDocument pdfDocument, int i, float v) {

    }

    @Override
    public void onPageUpdated(@NonNull PdfDocument pdfDocument, int i) {

    }

    @Override
    public void onPrepareContextualToolbar(@NonNull ContextualToolbar contextualToolbar) {
//        if (contextualToolbar instanceof AnnotationCreationToolbar) {
//            contextualToolbar.setMenuItemGroupingRule(new MyCustomGrouping(this));
//            final List<ContextualToolbarMenuItem> menuItems = ((AnnotationCreationToolbar) contextualToolbar).getMenuItems();
//            contextualToolbar.setMenuItems(menuItems);
//
//
//            contextualToolbar.setOnMenuItemClickListener(new ContextualToolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onToolbarMenuItemClick(@NonNull final ContextualToolbar contextualToolbar, @NonNull final ContextualToolbarMenuItem menuItem) {
//                    try {
//                        if (menuItem.getId() == com.pspdfkit.R.id.pspdf__annotation_creation_toolbar_item_stamp) {
////                            StampAnnotation stampAnnotation = new StampAnnotation(0, new RectF(300f, 500f, 500f, 300f), "Stamp with custom AP stream");
////
////// Create appearance stream generator from PDF containing vector logo stored in assets.
////                            stampAnnotation.setAppearanceStreamGenerator(new AssetAppearanceStreamGenerator("icon_1.pdf"));
////
////// Add the newly created annotation to the document.
////                            fragment.getDocument().getAnnotationProvider().addAnnotationToPage(stampAnnotation);
////                            showStampPicker();
//                            return true;
//                        }
//                        return false;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }
//
//            });
//        }
    }

    @Override
    public void onDisplayContextualToolbar(@NonNull ContextualToolbar contextualToolbar) {

    }

    @Override
    public void onRemoveContextualToolbar(@NonNull ContextualToolbar contextualToolbar) {

    }

    @Override
    public void onEnterTextSelectionMode(@NonNull TextSelectionController textSelectionController) {
        textSelectionToolbar.bindController(textSelectionController);
        toolbarCoordinatorLayout.displayContextualToolbar(textSelectionToolbar, true);
    }

    @Override
    public void onExitTextSelectionMode(@NonNull TextSelectionController textSelectionController) {
        toolbarCoordinatorLayout.removeContextualToolbar(true);
        textSelectionToolbar.unbindController();
        fragment.enterAnnotationCreationMode();
    }

    @Override
    public void onEnterAnnotationEditingMode(@NonNull AnnotationEditingController annotationEditingController) {
        annotationEditingInspectorController.bindAnnotationEditingController(annotationEditingController);

        annotationEditingToolbar.bindController(annotationEditingController);
        toolbarCoordinatorLayout.displayContextualToolbar(annotationEditingToolbar, true);
    }

    @Override
    public void onChangeAnnotationEditingMode(@NonNull AnnotationEditingController annotationEditingController) {

    }

    @Override
    public void onExitAnnotationEditingMode(@NonNull AnnotationEditingController annotationEditingController) {
        toolbarCoordinatorLayout.removeContextualToolbar(true);
        annotationEditingToolbar.unbindController();

        annotationEditingInspectorController.unbindAnnotationEditingController();
        fragment.enterAnnotationCreationMode();
    }

    @Override
    public void onEnterAnnotationCreationMode(@NonNull AnnotationCreationController annotationCreationController) {
//        When entering the annotation creation mode we bind the creation inspector to the provided
//        controller.
        // Controller handles request for toggling annotation inspector.
        annotationCreationInspectorController.bindAnnotationCreationController(annotationCreationController);

        // When entering the annotation creation mode we bind the toolbar to the provided controller, and
        // issue the coordinator layout to animate the toolbar in place.
        // Whenever the user presses an action, the toolbar forwards this command to the controller.
        // Instead of using the `AnnotationEditingToolbar` you could use a custom UI that operates on the controller.
        // Same principle is used on all other toolbars.
        annotationCreationToolbar.bindController(annotationCreationController);
        toolbarCoordinatorLayout.displayContextualToolbar(annotationCreationToolbar, true);
        annotationCreationActive = true;
    }

    @Override
    public void onChangeAnnotationCreationMode(@NonNull AnnotationCreationController annotationCreationController) {

    }

    @Override
    public void onExitAnnotationCreationMode(@NonNull AnnotationCreationController annotationCreationController) {
        // Once we're done with editing, unbind the controller from the toolbar, and remove it from the
        // toolbar coordinator layout (with animation in this case).
        // Same principle is used on all other toolbars.
        toolbarCoordinatorLayout.removeContextualToolbar(true);
        annotationCreationToolbar.unbindController();
        annotationCreationActive = false;

        // Also unbind the annotation creation controller from the inspector controller.
        annotationCreationInspectorController.unbindAnnotationCreationController();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragment.removeDocumentListener(this);
        fragment.removeOnAnnotationCreationModeChangeListener(this);
        fragment.removeOnAnnotationEditingModeChangeListener(this);
        fragment.removeOnTextSelectionModeChangeListener(this);
    }
}
