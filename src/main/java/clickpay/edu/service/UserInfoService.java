package clickpay.edu.service;

import clickpay.edu.dto.request.ReqUserInfo;
import clickpay.edu.dto.response.RespStatus;
import clickpay.edu.dto.response.RespStatusList;
import clickpay.edu.entity.UserInfo;
import clickpay.edu.exception.ExceptionConstants;
import clickpay.edu.exception.MyException;
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
}
