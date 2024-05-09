package com.example.hansumproject.service;

import com.example.hansumproject.dto.JoinDTO;
import com.example.hansumproject.entity.UserEntity;
import com.example.hansumproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO){

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        //Username이 존재하는지 확인
        Boolean isExist = userRepository.existsByUsername(username);

        //존재한다면 회원가입을 중단하는 로직
        if (isExist){

            return;
        }

        UserEntity newUser = new UserEntity();

        newUser.setUsername(username);
        //password는 인코딩해서 저장
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        //Spring에서는 권한을 부여할 때 "ROLE_XXX" 포맷을 맞춰야함.
        newUser.setRole("ROLE_ADMIN");

        userRepository.save(newUser);
    }

}
