package com.lzj.admin.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 乐字节  踏实教育 用心服务
 *
 * @author 乐字节--老李
 * @version 1.0
 */
public class PageResultUtil {

    public static Map<String,Object> getResult(Long total, List<?> records){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("count",total);
        result.put("data",records);
        result.put("code",0);
        result.put("msg","");
        return result;
    }
}
