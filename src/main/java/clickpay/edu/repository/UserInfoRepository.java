package clickpay.edu.repository;

import clickpay.edu.entity.UserInfo;
import clickpay.edu.exception.EnumCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

    Optional<UserInfo> findUserInfoByEmail(String email);
    UserInfo findUserInfoByEmailAndActive(String email, Integer active);
}
