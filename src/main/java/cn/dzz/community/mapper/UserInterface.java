package cn.dzz.community.mapper;

import cn.dzz.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInterface {

    @Insert("insert into tb_user (ACCOUNT_ID, NAME, TOKEN, GMT_CREATED, GMT_MODIFIED, LOGIN) values (#{accountId}, #{name}, #{token}, #{gmtCreated}, #{gmtModified}, #{login});")
    void insertUser(User user);

    @Select("select * from tb_user where TOKEN = #{token}")
    User findUserByToken(@Param("token") String token);
}
