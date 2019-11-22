package cn.itcast.mapper;

import cn.itcast.domain.Member;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MemberMapper {
    List<Member> findAll();

    Page<Member> selectByCondition(String queryString);

    //添加会员信息
    @Insert("insert into t_member values(null,#{fileNumber},#{name},#{sex},#{idCard},#{phoneNumber},#{regTime},#{password},#{email},#{birthday},#{remark})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void add(Member member);

    void deleteById(Integer id);

    //根据id查询会员信息
    @Select("select * from t_member where id = #{id}")
    Member findById(Integer id);

    //根据手机号查询会员信息
    @Select("select * from t_member where phoneNumber = #{telephone}")
    Member findByTelephone(String telephone);

    void edit(Member member);

    Integer findMemberCountBeforeDate(String date);

    Integer findMemberCountByDate(String date);

    Integer findMemberCountAfterDate(String date);

    Integer findMemberTotalCount();
}
