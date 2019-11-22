package cn.itcast.service;

import cn.itcast.domain.Member;
import cn.itcast.mapper.MemberMapper;
import cn.itcast.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    //根据手机号查询会员信息
    @Override
    public Member fastLogin(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    public void register(Member registerMember) {
        if (registerMember != null && registerMember.getPassword() != null) {
            registerMember.setPassword(MD5Utils.md5(registerMember.getPhoneNumber()));
        }
        memberMapper.add(registerMember);
    }
}
