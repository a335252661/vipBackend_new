package vip.IDCLoad;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Classname FileRegexFilter
 * @Description 文件正则表达式过滤器
 * @Date 2020-01-03 16:37:54
 * @Author gaohe
 * @Version 1.0
 */
public class FileRegexFilter implements FilenameFilter {

    private String regex;

    public FileRegexFilter(String regex){
        this.regex = regex;
    }

    @Override
    public boolean accept(File dir, String name) {
        if(null == this.regex){
            return true;
        }
        //根据表达式匹配
        return name.matches(regex);
    }
}
