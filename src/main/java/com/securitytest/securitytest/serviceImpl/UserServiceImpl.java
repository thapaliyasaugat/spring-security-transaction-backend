package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.resource.UserStatus;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto userById(int id) {
        User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(id)));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> allUsers() {
        List<User> users = userRepo.findAll();
        return users.stream().map(user->modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto blockUser(int id) {
        User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(id)));
        if(user.getStatus().equals(UserStatus.BLOCKED.status)) throw new RuntimeException("User Already Blocked.");
        user.setStatus(UserStatus.BLOCKED.status);
        User blockedUser = userRepo.save(user);
        return modelMapper.map(blockedUser,UserDto.class);
    }

    @Override
    public UserDto activateUser(int id) {
        User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(id)));
        if(user.getStatus().equals(UserStatus.ACTIVE.status)) throw new RuntimeException("User Already Active.");
        user.setStatus(UserStatus.ACTIVE.status);
        User activatedUser = userRepo.save(user);
        return modelMapper.map(activatedUser,UserDto.class);
    }

    @Override
    public UserDto userByEmail(String email) {
        return modelMapper.map(userRepo.findByEmail(email),UserDto.class);
    }

}
