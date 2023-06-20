package clickpay.edu.config;

import clickpay.edu.entity.UserInfo;
import clickpay.edu.exception.EnumCode;
import clickpay.edu.exception.MyException;
import clickpay.edu.repository.UserInfoRepository;
import org.aspectj.apache.bcel.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findUserInfoByEmailAndActive(email, EnumCode.Active.getValue());
        if(userInfo==null){
            throw new UsernameNotFoundException("user not found: "+userInfo);
        }
        return new UserInfoUserDetails(userInfo);

    }
}
