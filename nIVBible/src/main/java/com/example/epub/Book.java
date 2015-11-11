package com.example.epub;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.sax.Element;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

import com.example.EpubProject.Globals;
import com.example.EpubProject.HrefResolver;
import com.example.EpubProject.IResourceSource;
import com.example.EpubProject.ResourceResponse;
import com.example.EpubProject.XmlUtil;
import com.example.EpubProject.utils.Constants;

import org.jsoup.helper.StringUtil;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
 * Represents a book that's been packed into an epub file
 */
public class Book implements IResourceSource {
    private final static String HTTP_SCHEME = "http";

    // the container XML
    private static final String XML_NAMESPACE_CONTAINER = "urn:oasis:names:tc:opendocument:xmlns:container";
    private static final String XML_ELEMENT_CONTAINER = "container";
    private static final String XML_ELEMENT_ROOTFILES = "rootfiles";
    private static final String XML_ELEMENT_ROOTFILE = "rootfile";
    private static final String XML_ATTRIBUTE_FULLPATH = "full-path";
    private static final String XML_ATTRIBUTE_MEDIATYPE = "media-type";

    // the .opf XML
    private static final String XML_NAMESPACE_PACKAGE = "http://www.idpf.org/2007/opf";
    private static final String XML_ELEMENT_PACKAGE = "package";
    private static final String XML_ELEMENT_MANIFEST = "manifest";
    private static final String XML_ELEMENT_MANIFESTITEM = "item";
    private static final String XML_ELEMENT_SPINE = "spine";
    private static final String XML_ATTRIBUTE_TOC = "toc";
    private static final String XML_ELEMENT_ITEMREF = "itemref";
    private static final String XML_ATTRIBUTE_IDREF = "idref";
    private SharedPreferences preferencesBook;
    private Context context;
    int i;
    /*
     * The zip archive
     */
    private ZipFile mZip;

    /*
     * Name of the ".opf" file in the zip archive
     */
    private String mOpfFileName;

    /*
     * Id of the "table of contents" entry in manifest
     */
    private String mTocID;

    // Allow access to state for unit tests.
    public String getOpfFileName() {
        return mOpfFileName;
    }

    public String getTocID() {
        return mTocID;
    }

    public ArrayList<ManifestItem> getSpine() {
        return mSpine;
    }

    public Manifest getManifest() {
        return mManifest;
    }

    public TableOfContents getTableOfContents() {
        return mTableOfContents;
    }

    /*
     * The resources that are in the spine element of the metadata.
     */
    private ArrayList<ManifestItem> mSpine;

    /*
     * The manifest entry in the metadata.
     */
    private Manifest mManifest;

    /*
     * The Table of Contents in the metadata.
     */
    private TableOfContents mTableOfContents;

    /*
     * Intended for unit testing
     */
    public Book() {

        mSpine = new ArrayList<ManifestItem>();
        mManifest = new Manifest();
        mTableOfContents = new TableOfContents();

    }

    /*
     * Constructor
     *
     * @param fileName the filename of the Zip archive file
     */
    public Book(String fileName, Context context) {
        this.context = context;
        mSpine = new ArrayList<ManifestItem>();
        mManifest = new Manifest();
        mTableOfContents = new TableOfContents();
        preferencesBook = context.getSharedPreferences("book", 0);

        try {
            mZip = new ZipFile(fileName);
            parseEpub();
        } catch (IOException e) {
            Log.e(Globals.TAG, "Error opening file", e);
        }
    }

    /*
     * Name of zip file
     */
    public String getFileName() {
        return (mZip == null) ? null : mZip.getName();
    }

    /*
     * Fetch file from zip
     */
    private InputStream fetchFromZip(String fileName) {
        InputStream in = null;
        ZipEntry containerEntry = mZip.getEntry(fileName);
        if (containerEntry != null) {
            try {
                in = mZip.getInputStream(containerEntry);
            } catch (IOException e) {
                Log.e(Globals.TAG, "Error reading zip file " + fileName, e);
            }
        }

        if (in == null) {
            Log.e(Globals.TAG, "Unable to find file in zip: " + fileName);
        }

        return in;
    }

    /*
     * Fetch resource from ebook
     */
    public ResourceResponse fetch(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);
        ManifestItem item = mManifest.findByResourceName(resourceName);
        if (item != null) {
            ResourceResponse response = new ResourceResponse(
                    item.getMediaType(), fetchFromZip(resourceName));
            response.setSize(mZip.getEntry(resourceName).getSize());
            return response;
        }

        // if get here, something went wrong
        Log.e(Globals.TAG, "Unable to find resource in ebook " + resourceName);
        return null;
    }

    public Uri firstChapter() {
        int k = 0;
        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            k = Constants.POS0_START;
        } else {
            if (preferencesBook.getInt("position", Constants.POS0_START) == 0) {
                preferencesBook.edit().putInt("position", Constants.POS0_START).commit();
                k = Constants.POS0_START;

            } else {
                k = Constants.POS1_START;
            }
        }


        Log.e("book", "" + k);
        return 0 < mSpine.size() ? resourceName2Url(mSpine.get(k).getHref())
                : null;

    }

    public Uri selectedChapter(int pos) {
        return 0 < mSpine.size() ? resourceName2Url(mSpine.get(pos).getHref())
                : null;

    }

	/*
     * @return URI of next resource in sequence, or null if not one
	 */
    /*
     * public Uri nextResource(Uri resourceUri) { String resourceName =
	 * url2ResourceName(resourceUri); for (int i = 0; i < mSpine.size() - Constants.POS0_START;
	 * ++i) { if (mSpine.get(i).getHref().equals(resourceName)) { return
	 * resourceName2Url(mSpine.get(i + 1).getHref()); } } // if get here, not
	 * found return null; }
	 */

    public Uri nextResource(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);

        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            for (int i = Constants.POS0_START; i < mSpine.size() - 1; ++i) {
                if (mSpine.get(i).getHref().equals(resourceName)) {
                    return resourceName2Url(mSpine.get(i + 1).getHref());
                }
            }
            return null;
        } else {
            for (int i = Constants.POS1_START; i < mSpine.size() - 2; ++i) {
                if (mSpine.get(i).getHref().equals(resourceName)) {
                    return resourceName2Url(mSpine.get(i + 1).getHref());
                }
            }
            return null;
        }

        /*
         * for (int i = 0; i < mSpine.size()-k; ++i) { if
		 * (mSpine.get(i).getHref().equals(resourceName)) { return
		 * resourceName2Url(mSpine.get(i +1).getHref()); } } // if get here, not
		 * found
		 */
    }

    public Uri nextResourceBook(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);
        Toast.makeText(context, "resource Uri name" + resourceName,
                Toast.LENGTH_LONG).show();
        return resourceName2Url(resourceName + 1);
    }

    // if get here, not found

    /*
     * @return URI of previous resource in sequence, or null if not one
     */
    public Uri previousResource(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);
        int pos = preferencesBook.getInt("position", Constants.POS0_START);
        for (int i = pos + 1; i < mSpine.size(); ++i) {
//        for (int i = 1; i < mSpine.size(); ++i) {
            if (mSpine.get(i).getHref().equals(resourceName)) {
                return resourceName2Url(mSpine.get(i - 1).getHref());
            }
        }
        // if get here not found
        return null;
    }


    /*
     * Build up structure of epub
     */
    private void parseEpub() {
        // clear everything
        mOpfFileName = null;
        mTocID = null;
        mSpine.clear();
        mManifest.clear();
        mTableOfContents.clear();

        // get the "container" file, this tells us where the ".opf" file is
        parseXmlResource("META-INF/container.xml",
                constructContainerFileParser());

        if (mOpfFileName != null) {
            parseXmlResource(mOpfFileName, constructOpfFileParser());
        }

        if (mTocID != null) {
            ManifestItem tocManifestItem = mManifest.findById(mTocID);
            if (tocManifestItem != null) {
                String tocFileName = tocManifestItem.getHref();
                HrefResolver resolver = new HrefResolver(tocFileName);
                parseXmlResource(tocFileName,
                        mTableOfContents.constructTocFileParser(resolver));
            }
        }
    }

    private void parseXmlResource(String fileName, ContentHandler handler) {
        InputStream in = fetchFromZip(fileName);
        if (in != null) {
            XmlUtil.parseXmlResource(in, handler, null);
        }
    }

    /*
     * build parser to parse the container file, i.e. get the name of the ".opf"
     * file in the zip.
     *
     * @return parser
     */
    public ContentHandler constructContainerFileParser() {
        // describe the relationship of the elements
        RootElement root = new RootElement(XML_NAMESPACE_CONTAINER,
                XML_ELEMENT_CONTAINER);
        Element rootfiles = root.getChild(XML_NAMESPACE_CONTAINER,
                XML_ELEMENT_ROOTFILES);
        Element rootfile = rootfiles.getChild(XML_NAMESPACE_CONTAINER,
                XML_ELEMENT_ROOTFILE);

        rootfile.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                String mediaType = attributes.getValue(XML_ATTRIBUTE_MEDIATYPE);
                if ((mediaType != null)
                        && mediaType.equals("application/oebps-package+xml")) {
                    mOpfFileName = attributes.getValue(XML_ATTRIBUTE_FULLPATH);
                }
            }
        });
        return root.getContentHandler();
    }

    /*
     * build parser to parse the ".opf" file,
     *
     * @return parser
     */
    public ContentHandler constructOpfFileParser() {
        // describe the relationship of the elements
        RootElement root = new RootElement(XML_NAMESPACE_PACKAGE,
                XML_ELEMENT_PACKAGE);
        Element manifest = root.getChild(XML_NAMESPACE_PACKAGE,
                XML_ELEMENT_MANIFEST);
        Element manifestItem = manifest.getChild(XML_NAMESPACE_PACKAGE,
                XML_ELEMENT_MANIFESTITEM);
        Element spine = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_SPINE);
        Element itemref = spine.getChild(XML_NAMESPACE_PACKAGE,
                XML_ELEMENT_ITEMREF);

        final HrefResolver resolver = new HrefResolver(mOpfFileName);
        manifestItem.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                mManifest.add(new ManifestItem(attributes, resolver));
            }
        });

        // get name of Table of Contents file from the Spine
        spine.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                mTocID = attributes.getValue(XML_ATTRIBUTE_TOC);
            }
        });

        itemref.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                String temp = attributes.getValue(XML_ATTRIBUTE_IDREF);
                if (temp != null) {
                    ManifestItem item = mManifest.findById(temp);
                    if (item == null) {
                        return;
                    }

                    String href = item.getHref();
                    String[] split = href.split("/");
                    String fileName = split[split.length - 1];

                    // Each chapter name start with number (greater or equal 05) at 2 letters
                    // Use it to add only chapter without add TOC files
                    String twoFirstLetter = fileName.substring(0, 2);
                    boolean isNumber = StringUtil.isNumeric(twoFirstLetter);

                    if (isNumber && Integer.parseInt(twoFirstLetter) >= 5) {
                        mSpine.add(item);
                    }
                }
            }
        });
        return root.getContentHandler();
    }

    /*
     * @param url used by WebView
     *
     * @return resourceName used by zip file
     */
    private static String url2ResourceName(Uri url) {
        // we only care about the path part of the URL
        String resourceName = url.getPath();

        // if path has a '/' prepended, strip it
        if (resourceName.charAt(0) == '/') {
            resourceName = resourceName.substring(1);
        }
        return resourceName;
    }

    /*
     * @param resourceName used by zip file
     *
     * @return URL used by WebView
     */
    public static Uri resourceName2Url(String resourceName) {
        // build path assuming local file.
        // pack resourceName into path section of a file URI
        // need to leave '/' chars in path, so WebView is aware
        // of path to current resource, so it can can correctly resolve
        // path of any relative URLs in the current resource.
        return new Uri.Builder().scheme(HTTP_SCHEME)
                .encodedAuthority("localhost:" + Globals.WEB_SERVER_PORT)
                .appendEncodedPath(Uri.encode(resourceName, "/")).build();
    }

}
