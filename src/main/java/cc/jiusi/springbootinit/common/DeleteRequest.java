package cc.jiusi.springbootinit.common;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 删除请求
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id集合
     */
    private List<Long> ids;

    private static final long serialVersionUID = 1L;
}