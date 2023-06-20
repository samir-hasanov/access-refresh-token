package clickpay.edu.controller;

import clickpay.edu.dto.request.AuthRequest;
import clickpay.edu.dto.request.RefreshTokenRequest;
import clickpay.edu.dto.request.ReqUserInfo;
import clickpay.edu.dto.response.JwtResponse;
import clickpay.edu.dto.response.RespStatusList;
import clickpay.edu.entity.RefreshToken;
import clickpay.edu.exception.ExceptionConstants;
import clickpay.edu.exception.MyException;
import clickpay.edu.service.JwtService;
import clickpay.edu.service.RefreshTokenService;
import clickpay.edu.service.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@Transactional
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signUp")
    public RespStatusList registerUser(@RequestBody ReqUserInfo reqUserInfo){
 log.info("user "+reqUserInfo.getEmail());
        return userInfoService.saveUserInfo(reqUserInfo);

    }


    @PostMapping("/login")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) throws MyException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
                    .token(refreshToken.getToken()).build();
        } else {
            throw new MyException(ExceptionConstants.NOT_FOUND,"user not found in database!");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws MyException {
        log.info("refresh token : "+refreshTokenRequest.getToken());

        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getName());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new MyException(ExceptionConstants.NOT_FOUND,
                        "Refresh token is not in database!"));
    }


    @PostMapping("/addRoleToUser")
    public RespStatusList addRoleToUser(@RequestBody FormClass form) throws MyException {
 log.info("user request "+form.email,form.role);
        return userInfoService.addRoleToUser(form.email,form.role);

    }

    @Data
    public static class FormClass{
        private String email;
        private String role;
    }
}
