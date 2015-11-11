package com.example.EpubProject;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.ActionMode;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.EpubProject.utils.Constants;
import com.example.EpubProject.utils.HighlightModel;
import com.example.XmlFilter.InlineImageElementFilter;
import com.example.XmlFilter.RemoveSvgElementFilter;
import com.example.XmlFilter.StyleSheetElementFilter;
import com.example.XmlFilter.XmlSerializerToXmlFilterAdapter;
import com.nivbible.EpubReader;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * EpubWebView for use with Android 3.0 and above
 * The class uses shouldInterceptRequest() to load
 * resources that are referenced (i.e. Links) into the
 * view.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EpubWebView30 extends EpubWebView {

    private String mHighlightHtmlBodyStart;
    private String mHighlightHtmlBodyEnd;

    public EpubWebView30(Context context) {
        super(context);
    }

    public EpubWebView30(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * Do any Web settings specific to the derived class
     */
    protected void addWebSettings(WebSettings settings) {
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        // settings.setDefaultTextEncodingName("UTF-8");
    }

    // private PointF mLastTouchPoint;

    // final GestureDetector gestureDetector = new GestureDetector(getContext(),
    // new GestureDetector.SimpleOnGestureListener() {
    // public void onLongPress(MotionEvent e) {
    // Log.e("", "Longpress detected");
    // }
    // });
    //
    // public boolean onTouchEvent(MotionEvent event) {
    // mLastTouchPoint = new PointF(event.getX(), event.getY());
    // return super.onTouchEvent(event);
    // };

    // public PointF getLastTouchPoint() {
    // return mLastTouchPoint;
    // }

    protected void LoadUri(Uri uri) {
        load(uri);

//        loadUrl("file:///android_asset/sample.html");
    }

    private void load(Uri uri) {
        String xhtml = "<html><head /><body>Failed to convert XHTML</body></html>";
        try {
            // build SAX pipeline to convert XHTML
            // Chain is Reader -> stylesheetFilter -> imageFilter -> Serializer
            XMLFilterImpl stylesheetFilter = new StyleSheetElementFilter(uri, getBook());
            XMLFilterImpl svgFilter = new RemoveSvgElementFilter();
            XMLFilterImpl imageFilter = new InlineImageElementFilter(uri, getBook());
            svgFilter.setParent(stylesheetFilter);
            imageFilter.setParent(svgFilter);

            StringWriter writer = new StringWriter();
            XmlSerializer serializer = Xml.newSerializer();
            XmlSerializerToXmlFilterAdapter serializerFilter = new XmlSerializerToXmlFilterAdapter(serializer);
            serializerFilter.setParent(imageFilter);
            // convert the XHTML
            serializer.setOutput(writer);

            // serializer.setOutput(outputStream, "UTF-8");

            XmlUtil.parseXmlResource(getBook().fetch(uri).getData(), stylesheetFilter, serializerFilter);

            xhtml = writer.toString();

        } catch (Exception e) {
            Log.e(Globals.TAG, "Error generating XML ", e);
        }

        // get WebView to show the XHTML
        // loadData(xhtml, "text/html", "charset=utf-8");

        // String start = "<p class=\"biblebookname\">";
        xhtml = getPeraReplacement(xhtml);
        xhtml = getFilterAgain(xhtml);

        int bodys = xhtml.indexOf("<body>");
        if (bodys >= 0) {
            mHighlightHtmlBodyStart = xhtml.substring(0, bodys + 6);
        }
        int bodye = xhtml.indexOf("</body>");
        if (bodye >= 0) {
            mHighlightHtmlBodyEnd = xhtml.substring(bodye);
        }

        // xhtml = getFilterAgain(xhtml);
        // TODO Do not replace <a> by <span> and do not ađ style
//        xhtml = xhtml.replaceAll("<a", "<span");
//        xhtml = xhtml.replaceAll("</a", "</span");

//        String style = "<style type='" + "text/css"
//                + "'> span {font-family: 'Arial';text-transform: none;} * {-webkit-touch-callout: all;-webkit-user-select: all;-khtml-user-select: all;-moz-user-select: all;-ms-user-select: all;user-select: all;}</style>";
//        xhtml = xhtml.replaceAll("</head>", style + "</head>");

//        final String cssEndTag = "type=\"text/css\" />";
//        xhtml.replaceAll(cssEndTag, Constants.CSS_NIGHT_MODE + "");

        // xhtml = xhtml.replaceAll("</head>", "\n<script
        // type='text/javascript'>" + "</head>");

        // boolean spanEpub = isEpubSpan(xhtml);
        //
        // if (spanEpub) {
        // xhtml = xhtml.replaceAll("</head>",
        // "\n<script type='text/javascript'
        // src='file:///android_asset/highlight_web_view_span.js'></script>\n"
        // + "</head>");
        // } else {
        // xhtml = xhtml.replaceAll("</head>",
        // "\n<script type='text/javascript'
        // src='file:///android_asset/highlight_web_view.js'></script>\n"
        // + "</head>");
        // }

        xhtml = xhtml.replaceAll("</head>",
                "\n<script type='text/javascript' src='file:///android_asset/jquery-1.8.3.js'></script>\n" + "</head>");
        xhtml = xhtml.replaceAll("</head>",
                "\n<script type='text/javascript' src='file:///android_asset/android.selection.js'></script>\n"
                        + "</head>");
        xhtml = xhtml.replaceAll("</head>",
                "\n<script type='text/javascript' src='file:///android_asset/jpntext.js'></script>\n" + "</head>");
        xhtml = xhtml.replaceAll("</head>",
                "\n<script type='text/javascript' src='file:///android_asset/rangy-core.js'></script>\n" + "</head>");
        xhtml = xhtml.replaceAll("</head>",
                "\n<script type='text/javascript' src='file:///android_asset/rangy-serializer.js'></script>\n"
                        + "</head>");
        xhtml = xhtml.replaceAll("</head>",
                "\n<script type='text/javascript' src='file:///android_asset/rangy-textrange.js'></script>\n"
                        + "</head>");
        xhtml = xhtml.replaceAll("</head>",
                "\n<script type='text/javascript' src='file:///android_asset/rangy-selectionsaverestore.js'></script>\n"
                        + "</head>");

        // CSS
//        loadCss();
//        injectCSS();

        // xhtml = xhtml.replaceAll("</head>", "</script>" + "</head>");

        loadDataWithBaseURL(null, xhtml, "text/html", "UTF-8", null);// "charset
        // utf-8"
    }

    private void loadCss() {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><HEAD><LINK href=\"selections.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
        sb.append("</body></HTML>");
        this.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "utf-8", null);
    }

    // Inject CSS method: read style.css from assets folder
// Append stylesheet to document head
    private void injectCSS() {
        try {
            InputStream inputStream = getContext().getAssets().open("selections.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            this.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String getHighlightBodyStart() {
//        return this.mHighlightHtmlBodyStart;
//    }
//
//    public String getHighlightBodyEnd() {
//        return this.mHighlightHtmlBodyEnd;
//    }

//    private boolean isEpubSpan(String xhtml) {
//        String regexString = "(chapter)(\\d+)(_verse)(\\d+)";
//
//        Pattern pattern = Pattern.compile(regexString);
//
//        int nCount = 0;
//        Matcher matcher = pattern.matcher(xhtml);
//        while (matcher.find()) {
//            if (matcher.groupCount() == 4) {// "", "chapter", digits, "_verse",
//                // digits, ""
//                nCount++;
//                if (nCount > 2)
//                    return true;
//            }
//        }
//        return false;
//    }

    private String getPeraReplacement(String xhtml) {
        String start = "<p class=\"biblebookname\">";
        String end = "</p>";

        String regexString = Pattern.quote(start) + "(.*?)" + Pattern.quote(end);

        Pattern pattern = Pattern.compile(regexString);

        Matcher matcher = pattern.matcher(xhtml);
        String textInBetween;
        while (matcher.find()) {
            textInBetween = matcher.group(1);
            String toReplace = start + textInBetween + end;

            String replaced = "<p style='font-size:1em; color:#999; font-weight:bold;text-transform:lowercase;'>"
                    + textInBetween + "</p>";
            xhtml = xhtml.trim().replaceAll(toReplace, replaced);
        }
        return xhtml;
    }

    private String getFilterAgain(String xhtml) {
        String start = "<p class=\"st\">";
        String end = "</p>";
        String regexString = Pattern.quote(start) + "(.*?)" + Pattern.quote(end);

        Pattern pattern = Pattern.compile(regexString);

        Matcher matcher = pattern.matcher(xhtml);
        String textInBetween;

        // <a id="page872" /><b>Æ†sÉ›nkafo</b>
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                textInBetween = matcher.group(i);
                if (textInBetween.contains("<b>")) {
                    regexString = Pattern.quote("<b>") + "(.*?)" + Pattern.quote("</b>");
                    pattern = Pattern.compile(regexString);
                    matcher = pattern.matcher(textInBetween);

                    while (matcher.find()) {
                        for (int j = 0; j < matcher.groupCount(); j++) {
                            String mainText = matcher.group(j);
                            String replaced = "<p style='font-size:1.5em; color:#000; font-weight:bold; text-transform:lowercase; text-align:center'>"
                                    + mainText + "</p>";
                            xhtml = xhtml.trim().replaceAll(textInBetween, replaced);

                        }
                    }
                }
            }

        }
        return xhtml;
    }

    //

    /*
     * Called when Android 3.0 webview wants to load a URL.
     *
     * @return the requested resource from the ebook
     */
    public WebResourceResponse onRequest(String url) {
        Uri resourceUri = Uri.parse(url);
        WebResourceResponse webResponse = new WebResourceResponse("", "UTF-8", null);
        ResourceResponse response = getBook().fetch(resourceUri);

        // if don't have resource, give error
        if (response == null) {
            getWebViewClient().onReceivedError(this, WebViewClient.ERROR_FILE_NOT_FOUND,
                    "Unable to find resource in epub", url);
        } else {
            webResponse.setData(response.getData());
            webResponse.setMimeType(response.getMimeType());
        }
        return webResponse;
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        // Remove default action mode
//        return super.startActionMode(callback);
        return null;
    }

    public void setHighlightCellIdWithColor(String cellId, String color, boolean isCancel) {
        int isSetColor = 1;
        if (isCancel) {
            isSetColor = -1;
        } else {
            if (color == null) {
                color = "transparent";
                isSetColor = 0;
            }
        }

        loadUrl("javascript:Ph_ChangeHighlightSentence('" + cellId + "', '" + color + "', '" + isSetColor + "')");
    }

    public void setHighlightWithLanguage(EpubReader application, int page) {
        String language = PreferenceManager.getDefaultSharedPreferences(application).getString(Constants.SELECTED_FILE,
                Constants.AKUAPEM);

        for (HighlightModel aHighlight : application.getGlobals().highlightArray) {
            if (aHighlight.language.equals(language) && aHighlight.page == page) {
                String verseId = aHighlight.phHighlightClassId;
                if (!verseId.startsWith("Ph_"))
                    continue;

                String color = aHighlight.color;
                loadUrl("javascript:Ph_SetHighlightSentence('" + verseId + "', '" + color + "', '" + aHighlight.html
                        + "')");
            }
        }
    }
}
