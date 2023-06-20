package clickpay.edu.service;

import clickpay.edu.dto.request.ReqUserInfo;
import clickpay.edu.dto.response.RespStatus;
import clickpay.edu.dto.response.RespStatusList;
import clickpay.edu.entity.Role;
import clickpay.edu.entity.UserInfo;
import clickpay.edu.exception.EnumCode;
import clickpay.edu.exception.ExceptionConstants;
import clickpay.edu.exception.MyException;
import clickpay.edu.repository.RoleRepository;
import clickpay.edu.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RoleRepository roleRepository;



    public RespStatusList saveUserInfo(ReqUserInfo reqUserInfo) {
        RespStatusList respStatusList = new RespStatusList();
        String name = reqUserInfo.getName();
        String username = reqUserInfo.getEmail();

        try {
            if (name == null || username == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "request fail");
            }
            Optional<UserInfo> optional = userInfoRepository.findUserInfoByEmail(username);
            if (optional.isPresent()) {
                throw new MyException(ExceptionConstants.DATA_TAKEN, "account is available");
            }
            UserInfo user = new UserInfo();
            user.setImagePath(reqUserInfo.getImagePath());
            user.setName(reqUserInfo.getName());
            user.setEmail(reqUserInfo.getEmail());
            user.setPassword(passwordEncoder.encode(reqUserInfo.getPassword()));
            userInfoRepository.save(user);
            respStatusList.setStatus(RespStatus.getMessage());
        } catch (Exception e) {

        }

        return respStatusList;


    }

    public RespStatusList addRoleToUser(String email, String role) throws MyException {
        RespStatusList respStatusList=new RespStatusList();

        UserInfo u = userInfoRepository.findUserInfoByEmailAndActive(email, EnumCode.Active.getValue());
       if(u==null){
           throw new MyException(ExceptionConstants.NOT_FOUND,"user not found");
       }
        Role r = roleRepository.findRoleByRole(role);
        if(u==null){
            throw new MyException(ExceptionConstants.NOT_FOUND,"role not found");
        }
        u.getRoles().add(r);
        userInfoRepository.save(u);
        respStatusList.setStatus(RespStatus.getMessage());
        return respStatusList;

    }
}
