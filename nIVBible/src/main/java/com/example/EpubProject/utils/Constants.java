package com.example.EpubProject.utils;

public interface Constants {
    String ENGLISH = "Amplified New";
    String SWAHILI = "Swahili";
    String NASB = "NASB";
    String NIV = "NIV";
    String NKJV_BIBLE = "NKJV Bible";
    String AKUAPEM = "Akuapem";
    String ASANTE = "Asante";


    String[] FILE_NAME = new String[]{ENGLISH, SWAHILI, NASB, NIV, NKJV_BIBLE, AKUAPEM, ASANTE};
    String SELECTED_FILE = "fileName";
    String FILE_URI = "uri";

    String FILE_PAGE_NUMBER = "page number";

    String[] highlightColors = new String[]{"#FEC752", "#EB618B", "#FF54FF", "#73BE69"};

    // TODO Remove hard code chapter start position
//    int POS0_START = 67;// 68;
//    int POS1_START = 67;
    int POS0_START = 0;// 68;
    int POS1_START = 0;

    String CSS_NIGHT_MODE = "<style>body {background-color: black;color: white;}</style>";
}
