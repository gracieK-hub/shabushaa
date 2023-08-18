package com.unitechs.biz.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static List<String> findGroup(String groupPattern, String content){
        Pattern pattern=Pattern.compile(groupPattern);

        Matcher matcher = pattern.matcher(content);

        List<String> list=new ArrayList<>();
        while(matcher.find()){
            list.add(matcher.group(1));
        }
        return list;
    }
}
