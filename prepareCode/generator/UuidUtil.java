package com.hzecool.fdn.utils.generator;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * 分布式的uuid工具类
 * 
 * @author zhangyz created on 2012-4-15
 * @since Framework 1.0
 */
public class UuidUtil {   
    public static String LOCAL_ID = "";
    
    /**
     * 是否属于本地产生的guid
     * @param id
     * @param localIdPre
     * @return
     * @author zhangyz created on 2012-10-13
     */
    public static boolean isLocalGenId(String id, String localIdPre){
        if (id.length() == 36 && id.startsWith(localIdPre))//注意是36位长度
            return true;
        else
            return false;
    }
    
    /**
     * 获取32位uuid
     * @return
     * @author zhangyz created on 2013-8-27
     * @author fengdg updated on 2016-10-19
     */
    public static String getSimpleUuid32(){
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "");
//        StringBuilder builder = new StringBuilder();            
//        //去掉“-”符号 ,并且添加前缀
//        return builder.append(s.substring(0,8))
//                .append(s.substring(9,13))
//                .append(s.substring(14,18))
//                .append(s.substring(19,23))
//                .append(s.substring(24))
//                .toString();
    }
    
    /**
     * 获取分布式的唯一字符串码
     * @return
     * @author zhangyz created on 2012-8-13
     */
    public static String getUuid(){
        if (LOCAL_ID.length() == 0){
            return UUID.randomUUID().toString();
        }
        else{
            String s = UUID.randomUUID().toString();
            StringBuilder builder = new StringBuilder(LOCAL_ID);            
            //去掉“-”符号 ,并且添加前缀
            return builder.append(s.substring(0,8))
                    .append(s.substring(9,13))
                    .append(s.substring(14,18))
                    .append(s.substring(19,23))
                    .append(s.substring(24))
                    .toString();
        }
    }
    public static String getUuid(String local_id){
        if (local_id.length() == 0){
            return UUID.randomUUID().toString();
        }
        else{
            String s = UUID.randomUUID().toString();
            StringBuilder builder = new StringBuilder(local_id);            
            //去掉“-”符号 ,并且添加前缀
            return builder.append(s.substring(0,8))
                    .append(s.substring(9,13))
                    .append(s.substring(14,18))
                    .append(s.substring(19,23))
                    .append(s.substring(24))
                    .toString();
        }
    }
}
