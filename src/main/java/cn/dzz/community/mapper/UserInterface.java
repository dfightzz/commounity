package cn.dzz.community.mapper;

import cn.dzz.community.model.User;
import org.apache.ibatis.annotations.*;


public interface UserInterface {

    @Insert("insert into tb_user (ACCOUNT_ID, NAME, TOKEN, GMT_CREATED, GMT_MODIFIED, LOGIN, AVATAR_URL) values (#{accountId}, #{name}, #{token}, #{gmtCreated}, #{gmtModified}, #{login}, #{avatarUrl});")
    void insertUser(User user);

    @Select("select * from tb_user where TOKEN = #{token}")
    User findUserByToken(@Param("token") String token);

    @Select("select * from tb_user where ID = #{id}")
    User findUserById(@Param("id") int id);

    @Select("select * from tb_user where ACCOUNT_ID = #{id}")
    User findUserByAccountId(@Param("id") String id);

    @Update("update tb_user set NAME=#{name}, token=#{token}, GMT_MODIFIED=#{gmtModified},LOGIN=#{login}, AVATAR_URL=#{avatarUrl} where id = #{id}")
    void updateUser(User insertUser);
}
