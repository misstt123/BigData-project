package com.goddong;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/4/4
 **/
public class test {

    public static String dataenc(String jqnonce) {
        int ktime = 365;
        // 模拟js算出jqsign
        StringBuffer g = new StringBuffer();
        String d[] = new String[]{};
        for (int c = 0; c < jqnonce.length(); c++) {
            int b = ktime % 10;
            if (b == 0) b = 1;

            char[] temp = jqnonce.toCharArray();
            String unicodeValue = getUnicode(temp[c]);

            int f = Integer.valueOf(unicodeValue.toString()).intValue() ^ b;

            if (c != 0) {
                g.append(",");
            }
            g.append(String.valueOf(f));
            String h = asciiToString(g.toString());

        }
        String jqsign = d.toString();
        System.out.println( "jqnonce  " + jqnonce + "  jqsign  " + jqsign);
        return jqsign;
    }


    //getUnicode函数
    public static String getUnicode(char c) {
        String returnUniCode = null;
        returnUniCode = String.valueOf((int) c);
        return returnUniCode;

    }

    /**
     * Ascii转换为字符串
     *
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
}
