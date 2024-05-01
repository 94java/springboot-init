package cc.jiusi.springbootinit.common;

import cc.jiusi.springbootinit.constant.CommonConstant;
import lombok.Data;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 分页请求
 */
@Data
public class PageRequest {
    /**
     * 当前页号
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;
}
