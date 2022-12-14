package com.ossbar.common.validator.core;


import com.ossbar.common.exception.ValidateException;
import com.ossbar.utils.tool.CharUtil;
import com.ossbar.utils.tool.CommonUtil;
import com.ossbar.utils.tool.RegexUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class ValidateProcess {

    private final static int[] factorArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private final static char[] parityBit = {'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};

    private final static String REGEX_AREA = "^[0-9]{2}$";
    private final static String REGEX_DATE8 = "^[0-9]{8}$";
    private final static String REGEX_IP = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)";
    //private final static String REGEX_DATE = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
    //private final static String REGEX_CHINESE = "^[\\u4e00-\\u9fa5]{1,}$";
    private final static String REGEX_ENGLISH = "^[a-zA-z]{1,}$";
    private final static String REGEX_PHONE = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
    private final static String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    private final static int MIN_YEAR = 1700;
    private final static int MAX_YEAR = 2500;

    private final static Map<Integer, String> zoneNum = new HashMap<>();

    private final static String NotNullErrorMsg = "??????????????????";
    private final static String RegexErrorMsg = "??????????????????";
    private final static String MaxErrorMsg = "?????????????????????";
    private final static String MinErrorMsg = "?????????????????????";
    private final static String MaxLengthErrorMsg = "????????????????????????";
    private final static String MinLengthErrorMsg = "????????????????????????";
    private final static String DateFormatErrorMsg = "????????????????????????";
    private final static String IdCardErrorMsg = "?????????????????????";
    private final static String IpErrorMsg = "?????????????????????";
    private final static String ChineseErrorMsg = "??????????????????";
    private final static String EnglishErrorMsg = "??????????????????";
    private final static String EmailErrorMsg = "??????????????????";
    private final static String PhoneNumErrorMsg = "?????????????????????";

    static {
        zoneNum.put(11, "??????");
        zoneNum.put(12, "??????");
        zoneNum.put(13, "??????");
        zoneNum.put(14, "??????");
        zoneNum.put(15, "?????????");
        zoneNum.put(21, "??????");
        zoneNum.put(22, "??????");
        zoneNum.put(23, "?????????");
        zoneNum.put(31, "??????");
        zoneNum.put(32, "??????");
        zoneNum.put(33, "??????");
        zoneNum.put(34, "??????");
        zoneNum.put(35, "??????");
        zoneNum.put(36, "??????");
        zoneNum.put(37, "??????");
        zoneNum.put(41, "??????");
        zoneNum.put(42, "??????");
        zoneNum.put(43, "??????");
        zoneNum.put(44, "??????");
        zoneNum.put(45, "??????");
        zoneNum.put(46, "??????");
        zoneNum.put(50, "??????");
        zoneNum.put(51, "??????");
        zoneNum.put(52, "??????");
        zoneNum.put(53, "??????");
        zoneNum.put(54, "??????");
        zoneNum.put(61, "??????");
        zoneNum.put(62, "??????");
        zoneNum.put(63, "??????");
        zoneNum.put(64, "??????");
        zoneNum.put(71, "??????");
        zoneNum.put(81, "??????");
        zoneNum.put(82, "??????");
        zoneNum.put(91, "??????");
    }

    static void notNull(Object value) {
        if (null == value) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(String value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Number number) {
        if (null == number) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Collection<?> value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Map<?, ?> value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Object[] value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void regex(String regex, String value) {
        regex(regex, value, RegexErrorMsg + ", regex:" + regex + ", value:" + value);
    }

    private static void regex(String regex, String value, String msg) {
        if (!CommonUtil.isNull(value)) {
            if (!RegexUtil.test(regex, value)) {
                throw new ValidateException(msg);
            }
        }
    }

    static void max(int max, int value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(long max, long value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(float max, float value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(double max, double value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(byte max, byte value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(short max, short value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void maxLength(int max, String value) {
        if (!CommonUtil.isNull(value)) {
            if (value.length() > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value);
        }
    }

    static void maxLength(int max, Collection<?> value) {
        if (null != value) {
            if (value.size() > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value.size());
        }
    }

    static void maxLength(int max, Map<?, ?> value) {
        if (null != value) {
            if (value.size() > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value.size());
        }
    }

    static void maxLength(int max, Object[] value) {
        if (null != value) {
            if (value.length > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value.length);
        }
    }

    static void min(int min, int value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(long min, long value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(float min, float value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(double min, double value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(byte min, byte value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(short min, short value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void minLength(int min, String value) {
        if (!CommonUtil.isNull(value)) {
            if (value.length() < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
        }
    }

    static void minLength(int min, Collection<?> value) {
        if (null != value) {
            if (value.size() < min)
                throw new ValidateException(MinLengthErrorMsg + ", min:" + min + ", value:" + value.size());
        }
    }

    static void minLength(int min, Map<?, ?> value) {
        if (null != value) {
            if (value.size() < min)
                throw new ValidateException(MinLengthErrorMsg + ", min:" + min + ", value:" + value.size());
        }
    }

    static void minLength(int min, Object[] value) {
        if (null != value) {
            if (value.length < min)
                throw new ValidateException(MinLengthErrorMsg + ", min:" + min + ", value:" + value.length);
        }
    }

    static void date(String format, String value) {
        if (!CommonUtil.isNull(value)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                simpleDateFormat.parse(value);
            } catch (ParseException e) {
                throw new ValidateException(DateFormatErrorMsg + ", format:" + format + ", value:" + value);
            }
        }
    }

    /**
     * ?????????15??????????????????dddddd yymmdd xx p
     * dddddd????????????
     * yymmdd: ???????????????
     * xx: ???????????????
     * p: ????????????????????????????????????
     * <p/>
     * ?????????18??????????????????dddddd yyyymmdd xxx y
     * dddddd????????????
     * yyyymmdd: ???????????????
     * xxx:?????????????????????????????????????????????
     * y: ????????????????????????????????????17???????????????
     * <p/>
     * 18????????????????????????(????????????) wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2,1 ]
     * ????????? Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
     * ????????????????????????Y_P = mod( ???(Ai??wi),11 )
     * i???????????????????????????????????? 2...18 ???; Y_P???????????????????????????????????????
     */
    static void idCard(String value) {
        if (!CommonUtil.isNull(value)) {
            String idCard = value.toLowerCase();
            int length = idCard.length();
            //????????????
            if (length != 15 && length != 18) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            //????????????
            if (!isArea(idCard.substring(0, 2))) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            //????????????
            if (15 == length && !isDate6(idCard.substring(6, 12))) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            if (18 == length && !isDate8(idCard.substring(6, 14))) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            //??????18????????????
            if (18 == length) {
                char[] idCardArray = idCard.toCharArray();
                int sum = 0;
                for (int i = 0; i < idCardArray.length - 1; i++) {
                    if (idCardArray[i] < '0' || idCardArray[i] > '9') {
                        throw new ValidateException(IdCardErrorMsg + ", value:" + value);
                    }
                    sum += (idCardArray[i] - '0') * factorArr[i];
                }
                if (idCardArray[idCardArray.length - 1] != parityBit[sum % 11]) {
                    throw new ValidateException(IdCardErrorMsg + ", value:" + value);
                }
            }
        }
    }

    private static boolean isArea(String area) {
        return RegexUtil.test(REGEX_AREA, area) && zoneNum.containsKey(Integer.valueOf(area));
    }

    private static boolean isDate6(String date) {
        return isDate8("19" + date);
    }

    private static boolean isDate8(String date) {
        if (!RegexUtil.test(REGEX_DATE8, date)) {
            return false;
        }
        int[] iaMonthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1] = 29;
        return !(year < MIN_YEAR || year > MAX_YEAR) && !(month < 1 || month > 12) && !(day < 1 || day > iaMonthDays[month - 1]);
    }


    static void isIp(String value) {
        regex(REGEX_IP, value, IpErrorMsg + ", value:" + value);
    }

    static void chinese(String value) {
        boolean ret = CharUtil.isChinese(value);
        if (!ret) {
            throw new ValidateException(ChineseErrorMsg + ", value:" + value);
        }
    }

    static void english(String value) {
        regex(REGEX_ENGLISH, value, EnglishErrorMsg + ", value:" + value);
    }

    static void phone(String value) {
        regex(REGEX_PHONE, value, PhoneNumErrorMsg + ", value:" + value);
    }

    static void email(String value) {
        regex(REGEX_EMAIL, value, EmailErrorMsg + ", value:" + value);
    }
}
