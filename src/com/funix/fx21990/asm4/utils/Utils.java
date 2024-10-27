package com.funix.fx21990.asm4.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean verifyOTP(int confirmOTP, int OTP) {
        if (confirmOTP == OTP) {
            return true;
        }
        return false;
    }
    public static int getBasicOTP() {
        return new Random().nextInt(888) + 100;
    }

    public static String getAdvancedOTP(int length) {
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static boolean verifyCCCDNumber(String cccdCode) {
        Pattern pattern = Pattern.compile("\\D");
        Matcher matcher = pattern.matcher(cccdCode);
        return cccdCode.length() == 12 && !matcher.find();
    }

    public static boolean verifyAccoutCode(String cccdCode) {
        return cccdCode.length() == 6 && isNumeric(cccdCode);
    }

    private static boolean isNumeric(String cccdCode) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (cccdCode == null) {
            return false;
        }
        return pattern.matcher(cccdCode).matches();
    }

    public static String getProvince(String provinceCode) {
        switch (provinceCode) {
            case "001": {
                return "Ha Noi";
            }
            case "002": {
                return "Ha Giang";
            }
            case "037": {
                return "Ninh Binh";
            }
            case "048": {
                return "Da Nang";
            }
            case "079": {
                return "Ho Chi Minh";
            }
            default:
                return "Khong xac dinh";
        }
    }

    public static String getGender(String genderCode) {
        return (Integer.parseInt(genderCode) % 2 == 0) ? "Nam" : "Nu";
    }

    public static String getBirthYear(String genderCode, String lastBirthYear) {
        int gender = Integer.parseInt(genderCode);
        if (gender == 0 || gender == 1) {
            return "19" + lastBirthYear;
        } else if (gender == 2 || gender == 3) {
            return "20" + lastBirthYear;
        } else if (gender == 4 || gender == 5) {
            return "21" + lastBirthYear;
        } else if (gender == 6 || gender == 7) {
            return "22" + lastBirthYear;
        } else if (gender == 2 || gender == 3) {
            return "23" + lastBirthYear;
        } else {
            return "24" + lastBirthYear;
        }
    }
    public static String splipCCCD(String cccdCode, int type) {
        Pattern pattern = Pattern.compile("(^\\d{3})(\\d)(\\d{2})(\\d+)");
        Matcher matcher = pattern.matcher(cccdCode);
        while (matcher.find()) {
            switch (type) {
                case 1:
                    return getProvince(matcher.group(1));
                case 2:
                    return getGender(matcher.group(2)) + " | " + getBirthYear(matcher.group(2), matcher.group(3));
                case 3:
                    return matcher.group(4);
                default:
                    return "";
            }
        }
        return null;
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public static String getFormatMoney(double balance) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(balance) + "đ";
    }

}
