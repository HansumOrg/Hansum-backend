package com.example.hansumproject.service;

import com.example.hansumproject.dto.UserDto;
import com.example.hansumproject.entity.UserEntity;
import com.example.hansumproject.exception.DuplicateDataException;
import com.example.hansumproject.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) throws DuplicateDataException {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 회원가입 메서드
    public UserEntity joinProcess(UserDto userDto){

        String username = userDto.getUsername();
        String password = userDto.getPassword();
        String name = userDto.getName();
        String phone = userDto.getPhone();
        String sex = userDto.getSex();
        String birthday = userDto.getBirthday();
        String nickname = userDto.getNickname();
        String mbti = userDto.getMbti();
        int userAgreement = userDto.getUserAgreement();

        //Username이 존재하는지 확인
        Boolean isExist = userRepository.existsByUsername(username);

        //존재한다면 회원가입을 중단하는 로직
        if (isExist){

            //중단 메세지 오류 추가
            throw new DuplicateDataException("The username is already in use.");
        }

        UserEntity newUser = new UserEntity();

        newUser.setUsername(username);
        //password는 인코딩해서 저장
        newUser.setPassword(bCryptPasswordEncoder.encode(password));

        newUser.setName(name);
        newUser.setPhone(phone);
        newUser.setSex(sex);
        newUser.setBirthday(birthday);
        newUser.setNickname(nickname);
        newUser.setMbti(mbti);
        newUser.setUserAgreement(userAgreement);

        //Spring에서는 권한을 부여할 때 "ROLE_XXX" 포맷을 맞춰야함.
        newUser.setRole("ROLE_ADMIN");

        userRepository.save(newUser);

        return newUser;
    }


}
