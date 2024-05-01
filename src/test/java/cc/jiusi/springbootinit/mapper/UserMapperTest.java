package cc.jiusi.springbootinit.mapper;

import cc.jiusi.springbootinit.model.entity.User;
import cn.hutool.core.collection.ListUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-29  21:02
 * @Description: UserMapperTest（不在Spring环境，提高速度）
 */
public class UserMapperTest {
    private static UserMapper mapper;

    @BeforeAll
    public static void setUpMybatisDatabase() {
        SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(UserMapperTest.class.getClassLoader().getResourceAsStream("mybatisTestConfiguration/UserMapperTestConfiguration.xml"));
        //you can use builder.openSession(false) to not commit to database
        mapper = builder.getConfiguration().getMapper(UserMapper.class, builder.openSession(true));
    }

    @Test
    public void testSelectById() {
        System.out.println(mapper.selectById(1L));
    }

    @Test
    public void testSelectAllByCondition() {
        User user = new User();
        user.setUsername("111dsa11d");
        List<User> users = mapper.selectAll(user);
        System.out.println(users.toString());
    }

    @Test
    public void testSelectAll() {
        List<User> users = mapper.selectAll(null);
        System.out.println(users.toString());
    }

    @Test
    public void testSelectPage() {
        PageHelper.startPage(1,10);
        List<User> list = mapper.selectAll(null);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        System.out.println(pageInfo);
    }

    @Test
    public void testCountByCondition() {
        User user = new User();
        user.setPassword("123");
        long count = mapper.count(user);
        System.out.println(count);
    }

    @Test
    public void testCount() {
        long count = mapper.count(null);
        System.out.println(count);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setUsername("hellocode");
        user.setNickname("九思.");
        user.setPassword("hellocode6666");
        int count = mapper.insert(user);
        System.out.println(count);
    }

    @Test
    public void testInsertBatch() {
        List<User> userList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            User user = new User();
            user.setUsername("A" + i);
            user.setPassword("B" + i);
            userList.add(user);
        }
        int count = mapper.insertBatch(userList);
        System.out.println(count);
    }

    @Test
    public void testUpdate() {
        User user = mapper.selectById(1L);
        user.setId(1L);
        user.setUsername("testUpdate");
        int count = mapper.update(user);
        System.out.println(count);
    }

    @Test
    public void testDeleteById() {
        List<Long> ids = new ArrayList<>();
        ids.add(25451951951872L);
        int count = mapper.deleteBatchByIds(ids);
        System.out.println(count);
    }

    @Test
    public void testDeleteBatchByIds() {
        List<Long> ids = ListUtil.of(5586117595136L, 6158619119616L, 6581396574208L);
        int count = mapper.deleteBatchByIds(ids);
        System.out.println(count);
    }
}
