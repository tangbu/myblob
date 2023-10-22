package org.learning;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.learning.entity.UserEntity;
import org.learning.mapper.UserMapper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class MybatisExample {
    @Before
    public void initData() {
        Connection conn = null;
        // 加载HSQLDB驱动
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            // 获取Connection对象
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mybatis",
                    "sa", "");
            // 使用Mybatis的ScriptRunner工具类执行数据库脚本
            ScriptRunner scriptRunner = new ScriptRunner(conn);
            // 不输出sql日志
            scriptRunner.setLogWriter(null);
            scriptRunner.runScript(Resources.getResourceAsReader("create-table.sql"));
            scriptRunner.runScript(Resources.getResourceAsReader("init-data.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void testMybatis () throws IOException {
        // 获取配置文件输入流
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 通过SqlSessionFactoryBuilder的build()方法创建SqlSessionFactory实例
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 调用openSession()方法创建SqlSession实例
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 获取UserMapper代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 执行Mapper方法，获取执行结果
        List<UserEntity> userList = userMapper.listAllUser();
         /*
        // 兼容Ibatis，通过Mapper Id执行SQL操作
        List<UserEntity> userList = sqlSession.selectList(
                "com.blog4java.mybatis.com.blog4java.mybatis.example.mapper.UserMapper.listAllUser");
        */
        System.out.println(userList);
    }
}
