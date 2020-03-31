package com.xz.daywallpaper.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author czr
 * @date 2020/3/27
 */
public class SignMD5 {
    /**
     * 腾讯接口鉴权
     *
     * <p>
     * 1. 计算步骤
     * 用于计算签名的参数在不同接口之间会有差异，但算法过程固定如下4个步骤。
     * 将<key, value>请求参数对按key进行字典升序排序，得到有序的参数对列表N
     * 将列表N中的参数对按URL键值对的格式拼接成字符串，得到字符串T（如：key1=value1&key2=value2），URL键值拼接过程value部分需要URL编码，URL编码算法用大写字母，例如%E8，而不是小写%e8
     * 将应用密钥以app_key为键名，组成URL键值拼接到字符串T末尾，得到字符串S（如：key1=value1&key2=value2&app_key=密钥)
     * 对字符串S进行MD5运算，将得到的MD5值所有字符转换成大写，得到接口请求签名
     * 2. 注意事项
     * 不同接口要求的参数对不一样，计算签名使用的参数对也不一样
     * 参数名区分大小写，参数值为空不参与签名
     * URL键值拼接过程value部分需要URL编码
     * 签名有效期5分钟，需要请求接口时刻实时计算签名信息
     * 更多注意事项，请查看常见问题
     * </p>
     *
     * @param params
     */
    public static String getSignMD5(String secret, Map<String, String> params) {
        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
            public int compare(String obj1, String obj2) {
                // 升序排序
                return obj1.compareTo(obj2);
            }
        });
        Set<Map.Entry<String, String>> entrys = params.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            String name = entry.getKey().toString();
            if (!name.equals("MD5")) {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        //map.put("app_key", secret);//添加密钥
        String macParams = "";
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            macParams = macParams + key + "=" + urlEncoder(map.get(key) + "") + "&";
        }

        //最后加上密钥
        macParams = macParams + "app_key=" + secret;
        //Logger.w(macParams);
        return MD5Util.getMD5(macParams);
    }


    /**
     * url编码
     *
     * @param st
     * @return
     */
    public static String urlEncoder(String st) {
        try {
            return URLEncoder.encode(st, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * url解码
     *
     * @param st
     * @return
     */
    public static String urlDecoder(String st) {
        try {
            return URLEncoder.encode(st, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }


}
