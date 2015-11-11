package com.example.EpubProject.utils;

public interface Constants {
    String AKUAPEM = "NIV 1984 IOS";
    // String ENGLISH = "NIV";
    String ENGLISH = "Amplified New";
    String BI12_SW = "bi12_SW";
    String NASB_OH = "NASB OH";
    String NIV_1984 = "NIV 1984";
    String NKJV_BIBLE = "NKJV Bible";
    String TWI_AKUAPEMA = "Twi AkuapemA";
    String TWI_BIBLE_ASANTE = "Twi Bible - Asante";


    String[] FILE_NAME = new String[]{AKUAPEM, ENGLISH, BI12_SW, NASB_OH, NIV_1984, NKJV_BIBLE, TWI_AKUAPEMA, TWI_BIBLE_ASANTE};
    String SELECTED_FILE = "fileName";
    String FILE_URI = "uri";

    String FILE_PAGE_NUMBER = "page number";

    public static final String[] highlightColors = new String[]{"#FEC752", "#EB618B", "#FF54FF", "#73BE69"};

    // TODO Remove hard code chapter start position
//    int POS0_START = 67;// 68;
//    int POS1_START = 67;
    int POS0_START = 0;// 68;
    int POS1_START = 0;

    public static final String CSS_NIGHT_MODE = "<style>body {background-color: black;color: white;}</style>";
}
