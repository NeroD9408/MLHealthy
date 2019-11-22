package cn.itcast.service;

import cn.itcast.domain.Member;

public interface MemberService {

    //根据手机号查询机会员信息
    Member fastLogin(String telephone);

    //注册会员信息
    void register(Member registerMember);
}
