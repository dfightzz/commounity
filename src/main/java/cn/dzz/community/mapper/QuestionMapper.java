package cn.dzz.community.mapper;

import cn.dzz.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, commit_count, view_count, like_count, tag) values (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{commitCount}, #{viewCount}, #{likeCount}, #{tag})")
    void create(Question question);

    @Select("<script>" +
            "select * from question where 1=1 " +
            "<if test=\"userId != null \">and creator = #{userId} </if></script>")
    List<Question> list(@Param("userId") Integer userId);

    @Select("select * from question where id = #{id}")
    Question getQuestionById(@Param("id") Integer id);

    @Update("update question set title = #{title}, description=#{description}, gmt_modified=#{gmtModified}, tag=#{tag} where id = #{id}")
    void update(Question question);
}
