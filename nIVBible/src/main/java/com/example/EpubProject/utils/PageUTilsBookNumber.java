package com.example.EpubProject.utils;

import java.util.ArrayList;
import java.util.List;

public class PageUTilsBookNumber {

    public enum LanguageTypeBookNumber {
        ENGLISHBOOK, EQBOOK;
    }

    public static int getPageCountBookNumber(
            LanguageTypeBookNumber languageType, int pageNumer) {

        switch (languageType) {
            case ENGLISHBOOK:
                getEng(pageNumer);
                break;
            case EQBOOK:
                getEq(pageNumer);
                break;
            default:
                break;
        }
        return 0;
    }

//    public static int getPagePosition(int pageNumber) {
//        int position = 0;
//        for (; pageNumber == 1; pageNumber--) {
//            // Page number start with 1
//            position += PageUTilsBook.getListPageBookEnglis().get(pageNumber - 1).pageCountBook;
//        }
//
//        return position + Constants.POS0_START;
//    }

    public static int getEng(int pageNumber) {
        pageNumber -= Constants.POS1_START;
        if (0 <= pageNumber && pageNumber < 50) {
            return pageNumber + 1;
        }
        if (50 <= pageNumber && pageNumber < 90) {
            pageNumber -= 49;
            return pageNumber;
        }
        if (90 <= pageNumber && pageNumber < 117) {
            pageNumber -= 89;
            return pageNumber;
        }
        if (117 <= pageNumber && pageNumber < 153) {
            pageNumber -= 116;
            return pageNumber;
        }
        if (153 <= pageNumber && pageNumber < 187) {
            pageNumber -= 152;
            return pageNumber;
        }
        if (187 <= pageNumber && pageNumber < 211) {
            pageNumber -= 186;
            return pageNumber;
        }
        if (211 <= pageNumber && pageNumber < 232) {
            pageNumber -= 210;
            return pageNumber;
        }
        if (232 <= pageNumber && pageNumber < 236) {
            pageNumber -= 231;
            return pageNumber;
        }
        if (236 <= pageNumber && pageNumber < 267) {
            pageNumber -= 235;
            return pageNumber;
        }
        if (267 <= pageNumber && pageNumber < 291) {
            pageNumber -= 266;
            return pageNumber;
        }
        if (291 <= pageNumber && pageNumber < 313) {
            pageNumber -= 290;
            return pageNumber;
        }
        if (313 <= pageNumber && pageNumber < 338) {
            pageNumber -= 312;
            return pageNumber;
        }
        if (338 <= pageNumber && pageNumber < 367) {
            pageNumber -= 337;
            return pageNumber;
        }
        if (367 <= pageNumber && pageNumber < 403) {
            pageNumber -= 366;
            return pageNumber;
        }
        if (403 <= pageNumber && pageNumber < 413) {
            pageNumber -= 402;
            return pageNumber;
        }
        if (413 <= pageNumber && pageNumber < 426) {
            pageNumber -= 412;
            return pageNumber;
        }

        if (426 <= pageNumber && pageNumber < 436) {
            pageNumber -= 425;

            return pageNumber;

        }
        if (436 <= pageNumber && pageNumber < 478) {
            pageNumber -= 435;
            return pageNumber;
        }
        if (478 <= pageNumber && pageNumber < 628) {
            pageNumber -= 477;
            return pageNumber;
        }
        if (628 <= pageNumber && pageNumber < 659) {
            pageNumber -= 627;
            return pageNumber;
        }
        if (659 <= pageNumber && pageNumber < 671) {
            pageNumber -= 658;
            return pageNumber;
        }

        if (671 <= pageNumber && pageNumber < 679) {
            pageNumber -= 670;
            return pageNumber;
        }

        if (679 <= pageNumber && pageNumber < 745) {
            pageNumber -= 678;
            return pageNumber;
        }
        if (745 <= pageNumber && pageNumber < 797) {
            pageNumber -= 744;
            return pageNumber;
        }
        if (797 <= pageNumber && pageNumber < 802) {
            pageNumber -= 796;
            return pageNumber;
        }
        if (802 <= pageNumber && pageNumber < 850) {
            pageNumber -= 801;
            return pageNumber;
        }
        if (850 <= pageNumber && pageNumber < 862) {
            pageNumber -= 849;
            return pageNumber;
        }
        if (862 <= pageNumber && pageNumber < 876) {
            pageNumber -= 861;
            return pageNumber;
        }
        if (876 <= pageNumber && pageNumber < 879) {
            pageNumber -= 875;
            return pageNumber;
        }

        if (879 <= pageNumber && pageNumber < 888) {
            pageNumber -= 878;
            return pageNumber;
        }
        if (888 <= pageNumber && pageNumber < 889) {
            pageNumber -= 887;
            return pageNumber;

        }
        if (889 <= pageNumber && pageNumber < 893) {
            pageNumber -= 888;
            return pageNumber;
        }
        if (893 <= pageNumber && pageNumber < 900) {
            pageNumber -= 892;
            return pageNumber;
        }
        if (900 <= pageNumber && pageNumber < 903) {
            pageNumber -= 899;
            return pageNumber;
        }
        if (903 <= pageNumber && pageNumber < 906) {
            pageNumber -= 902;
            return pageNumber;
        }
        if (906 <= pageNumber && pageNumber < 909) {
            pageNumber -= 905;
            return pageNumber;
        }
        if (909 <= pageNumber && pageNumber < 911) {
            pageNumber -= 908;
            return pageNumber;
        }
        if (911 <= pageNumber && pageNumber < 925) {
            pageNumber -= 910;
            return pageNumber;
        }
        if (925 <= pageNumber && pageNumber < 929) {
            pageNumber -= 924;
            return pageNumber;
        }

        if (929 <= pageNumber && pageNumber < 957) {
            pageNumber -= 928;
            return pageNumber;
        }
        if (957 <= pageNumber && pageNumber < 973) {
            pageNumber -= 956;
            return pageNumber;
        }
        if (973 <= pageNumber && pageNumber < 997) {
            pageNumber -= 972;
            return pageNumber;
        }
        if (997 <= pageNumber && pageNumber < 1018) {
            pageNumber -= 996;
            return pageNumber;
        }
        if (1018 <= pageNumber && pageNumber < 1046) {
            pageNumber -= 1017;
            return pageNumber;
        }
        if (1046 <= pageNumber && pageNumber < 1062) {
            pageNumber -= 1045;
            return pageNumber;
        }
        if (1062 <= pageNumber && pageNumber < 1078) {
            pageNumber -= 1061;
            return pageNumber;
        }
        if (1078 <= pageNumber && pageNumber < 1091) {
            pageNumber -= 1077;
            return pageNumber;
        }
        if (1091 <= pageNumber && pageNumber < 1097) {
            pageNumber -= 1090;
            return pageNumber;

        }
        if (1097 <= pageNumber && pageNumber < 1103) {
            pageNumber -= 1096;
            return pageNumber;
        }
        if (1103 <= pageNumber && pageNumber < 1107) {
            pageNumber -= 1102;
            return pageNumber;
        }
        if (1107 <= pageNumber && pageNumber < 1111) {
            pageNumber -= 1107;
            return pageNumber;
        }
        if (1111 <= pageNumber && pageNumber < 1116) {
            pageNumber -= 1110;
            return pageNumber;
        }
        if (1116 <= pageNumber && pageNumber < 1119) {
            pageNumber -= 1115;
            return pageNumber;
        }
        if (1119 <= pageNumber && pageNumber < 1125) {
            pageNumber -= 1118;
            return pageNumber;
        }
        if (1125 <= pageNumber && pageNumber < 1129) {
            pageNumber -= 1124;
            return pageNumber;
        }
        if (1129 <= pageNumber && pageNumber < 1132) {
            pageNumber -= 1128;
            return pageNumber;
        }
        if (1132 <= pageNumber && pageNumber < 1133) {
            pageNumber -= 1131;
            return pageNumber;
        }
        if (1133 <= pageNumber && pageNumber < 1146) {
            pageNumber -= 1132;
            return pageNumber;
        }
        if (1146 <= pageNumber && pageNumber < 1151) {
            pageNumber -= 1146;
            return pageNumber;
        }
        if (1151 <= pageNumber && pageNumber < 1156) {
            pageNumber -= 1150;
            return pageNumber;
        }
        if (1156 <= pageNumber && pageNumber < 1159) {
            pageNumber -= 1155;
            return pageNumber;
        }
        if (1159 <= pageNumber && pageNumber < 1164) {
            pageNumber -= 1158;
            return pageNumber;
        }
        if (1164 <= pageNumber && pageNumber < 1165) {
            pageNumber -= 1163;
            return pageNumber;
        }
        if (1165 <= pageNumber && pageNumber < 1166) {
            pageNumber -= 1164;
            return pageNumber;
        }
        if (1166 <= pageNumber && pageNumber < 1167) {
            pageNumber -= 1165;
            return pageNumber;
        }
        if (1167 <= pageNumber) {
            pageNumber -= 1166;
            return pageNumber;
        }
        return pageNumber;

    }

    public static int getEq(int pageNumber) {

        pageNumber -= Constants.POS0_START;
        if (0 <= pageNumber && pageNumber < 50) {
            return pageNumber + 1;

        }
        if (50 <= pageNumber && pageNumber < 90) {
            pageNumber -= 49;
            return pageNumber;
        }
        if (90 <= pageNumber && pageNumber < 117) {
            pageNumber -= 89;
            return pageNumber;
        }
        if (117 <= pageNumber && pageNumber < 153) {
            pageNumber -= 116;
            return pageNumber;
        }
        if (153 <= pageNumber && pageNumber < 187) {
            pageNumber -= 152;
            return pageNumber;
        }
        if (187 <= pageNumber && pageNumber < 211) {
            pageNumber -= 186;
            return pageNumber;
        }
        if (211 <= pageNumber && pageNumber < 232) {
            pageNumber -= 210;
            return pageNumber;
        }
        if (232 <= pageNumber && pageNumber < 236) {
            pageNumber -= 231;
            return pageNumber;
        }
        if (236 <= pageNumber && pageNumber < 267) {
            pageNumber -= 235;
            return pageNumber;
        }
        if (267 <= pageNumber && pageNumber < 291) {
            pageNumber -= 266;
            return pageNumber;
        }
        if (291 <= pageNumber && pageNumber < 313) {
            pageNumber -= 290;
            return pageNumber;
        }
        if (313 <= pageNumber && pageNumber < 338) {
            pageNumber -= 312;
            return pageNumber;
        }
        if (338 <= pageNumber && pageNumber < 367) {
            pageNumber -= 337;
            return pageNumber;
        }
        if (367 <= pageNumber && pageNumber < 403) {
            pageNumber -= 366;
            return pageNumber;
        }
        if (403 <= pageNumber && pageNumber < 413) {

            pageNumber -= 402;
            return pageNumber;
        }
        if (413 <= pageNumber && pageNumber < 426) {
            pageNumber -= 412;
            return pageNumber;
        }

        if (426 <= pageNumber && pageNumber < 436) {
            pageNumber -= 425;
            return pageNumber;
        }
        if (436 <= pageNumber && pageNumber < 478) {
            pageNumber -= 435;
            return pageNumber;
        }
        if (478 <= pageNumber && pageNumber < 628) {
            pageNumber -= 477;
            return pageNumber;
        }
        if (628 <= pageNumber && pageNumber < 659) {
            pageNumber -= 627;
            return pageNumber;
        }
        if (659 <= pageNumber && pageNumber < 671) {
            pageNumber -= 658;
            return pageNumber;
        }

        if (671 <= pageNumber && pageNumber < 679) {
            pageNumber -= 670;
            return pageNumber;
        }

        if (679 <= pageNumber && pageNumber < 745) {
            pageNumber -= 678;

            return pageNumber;

        }
        if (745 <= pageNumber && pageNumber < 797) {
            pageNumber -= 744;
            return pageNumber;
        }
        if (797 <= pageNumber && pageNumber < 802) {
            pageNumber -= 796;
            return pageNumber;
        }
        if (802 <= pageNumber && pageNumber < 850) {
            pageNumber -= 801;
            return pageNumber;
        }
        if (850 <= pageNumber && pageNumber < 862) {
            pageNumber -= 849;
            return pageNumber;
        }
        if (862 <= pageNumber && pageNumber < 876) {
            pageNumber -= 861;
            return pageNumber;
        }
        if (876 <= pageNumber && pageNumber < 879) {
            pageNumber -= 875;
            return pageNumber;
        }

        if (879 <= pageNumber && pageNumber < 888) {
            pageNumber -= 878;
            return pageNumber;
        }
        if (888 <= pageNumber && pageNumber < 889) {
            pageNumber -= 887;
            return pageNumber;
        }
        if (889 <= pageNumber && pageNumber < 893) {
            pageNumber -= 888;
            return pageNumber;
        }
        if (893 <= pageNumber && pageNumber < 900) {
            pageNumber -= 892;
            return pageNumber;
        }
        if (900 <= pageNumber && pageNumber < 903) {
            pageNumber -= 899;
            return pageNumber;
        }
        if (903 <= pageNumber && pageNumber < 906) {
            pageNumber -= 902;
            return pageNumber;
        }
        if (906 <= pageNumber && pageNumber < 909) {
            pageNumber -= 905;
            return pageNumber;
        }
        if (909 <= pageNumber && pageNumber < 911) {
            pageNumber -= 908;
            return pageNumber;
        }
        if (911 <= pageNumber && pageNumber < 925) {
            pageNumber -= 910;
            return pageNumber;
        }
        if (925 <= pageNumber && pageNumber < 929) {
            pageNumber -= 924;
            return pageNumber;
        }

        if (929 <= pageNumber && pageNumber < 957) {
            pageNumber -= 928;
            return pageNumber;
        }
        if (957 <= pageNumber && pageNumber < 973) {
            pageNumber -= 956;
            return pageNumber;
        }
        if (973 <= pageNumber && pageNumber < 997) {
            pageNumber -= 972;
            return pageNumber;

        }
        if (997 <= pageNumber && pageNumber < 1018) {
            pageNumber -= 996;
            return pageNumber;
        }
        if (1018 <= pageNumber && pageNumber < 1046) {
            pageNumber -= 1017;
            return pageNumber;
        }
        if (1046 <= pageNumber && pageNumber < 1062) {
            pageNumber -= 1045;
            return pageNumber;
        }
        if (1062 <= pageNumber && pageNumber < 1078) {
            pageNumber -= 1061;
            return pageNumber;
        }
        if (1078 <= pageNumber && pageNumber < 1091) {
            pageNumber -= 1077;
            return pageNumber;
        }
        if (1091 <= pageNumber && pageNumber < 1097) {
            pageNumber -= 1090;
            return pageNumber;
        }
        if (1097 <= pageNumber && pageNumber < 1103) {
            pageNumber -= 1096;
            return pageNumber;
        }
        if (1103 <= pageNumber && pageNumber < 1107) {
            pageNumber -= 1102;
            return pageNumber;
        }
        if (1107 <= pageNumber && pageNumber < 1111) {
            pageNumber -= 1106;
            return pageNumber;
        }
        if (1111 <= pageNumber && pageNumber < 1116) {
            pageNumber -= 1110;
            return pageNumber;
        }
        if (1116 <= pageNumber && pageNumber < 1119) {
            pageNumber -= 1115;
            return pageNumber;
        }
        if (1119 <= pageNumber && pageNumber < 1125) {
            pageNumber -= 1118;
            return pageNumber;
        }
        if (1125 <= pageNumber && pageNumber < 1129) {
            pageNumber -= 1124;
            return pageNumber;
        }
        if (1129 <= pageNumber && pageNumber < 1132) {
            pageNumber -= 1128;
            return pageNumber;

        }
        if (1132 <= pageNumber && pageNumber < 1133) {
            pageNumber -= 1131;
            return pageNumber;
        }
        if (1133 <= pageNumber && pageNumber < 1146) {
            pageNumber -= 1132;
            return pageNumber;
        }
        if (1146 <= pageNumber && pageNumber < 1151) {
            pageNumber -= 1145;
            return pageNumber;

        }
        if (1151 <= pageNumber && pageNumber < 1156) {
            pageNumber -= 1150;
            return pageNumber;

        }
        if (1156 <= pageNumber && pageNumber < 1159) {
            pageNumber -= 1155;
            return pageNumber;

        }
        if (1159 <= pageNumber && pageNumber < 1164) {
            pageNumber -= 1158;
            return pageNumber;

        }
        if (1164 <= pageNumber && pageNumber < 1165) {
            pageNumber -= 1163;
            return pageNumber;

        }
        if (1165 <= pageNumber && pageNumber < 1166) {
            pageNumber -= 1164;
            return pageNumber;

        }
        if (1166 <= pageNumber && pageNumber < 1167) {
            pageNumber -= 1165;
            return pageNumber;

        }
        if (1167 <= pageNumber) {
            pageNumber -= 1166;
            return pageNumber;
        }
        return pageNumber;

    }

}
