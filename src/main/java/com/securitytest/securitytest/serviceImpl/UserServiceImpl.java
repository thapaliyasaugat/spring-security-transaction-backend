package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public UserPageableResponse allUsers(PageRequest pageRequest) {
        Pageable p = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),pageRequest.getPageSize());
        Page<User> users = userRepo.findAll(p);
        List<UserDto> userDtos = users.getContent().stream().map(u->modelMapper.map(u, UserDto.class)).collect(Collectors.toList());
        UserPageableResponse response = new UserPageableResponse();
        response.setContent(userDtos);
        response.setPageNumber(users.getNumber());
        response.setPageSize(users.getSize());
        response.setTotalNoOfPages(users.getTotalPages());
        response.setTotalNoOfElements(users.getTotalElements());
        return response;
    }

    @Override
    public UserDto blockUser(int id) {
        User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(id)));
        if(user.getStatus().equals(UserStatus.BLOCKED)) throw new RuntimeException("User Already Blocked.");
        user.setStatus(UserStatus.BLOCKED);
        User blockedUser = userRepo.save(user);
        return modelMapper.map(blockedUser,UserDto.class);
    }

    @Override
    public UserDto activateUser(int id) {
        User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(id)));
        if(user.getStatus().equals(UserStatus.ACTIVE)) throw new RuntimeException("User Already Active.");
        user.setStatus(UserStatus.ACTIVE);
        User activatedUser = userRepo.save(user);
        return modelMapper.map(activatedUser,UserDto.class);
    }

    @Override
    public UserDto userByEmail(String email) {
        try {
            return modelMapper.map(userRepo.findByEmail(email), UserDto.class);
        }catch (Exception e){
            throw new ResourceNotFoundException("user","email",email);
        }
    }

    @Override
    public UserDto save(User user) {
        return modelMapper.map(userRepo.save(user),UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepo.findByEmail(userDto.getEmail());
        if(userDto.getUserName() != null) user.setUserName(userDto.getUserName());
        if(userDto.getBalance() != null) user.setBalance(userDto.getBalance());
        if(userDto.getStatus() != null) user.setStatus(userDto.getStatus());
        User user1 = userRepo.save(user);
        return modelMapper.map(user1,UserDto.class);
    }

    @Override
    public void addUserRole(RoleDto roleDto,int id) {
        try {
            User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", String.valueOf(id)));
            user.addRole(modelMapper.map(roleDto, Role.class));
            userRepo.save(user);
        }catch (Exception e){
            throw new RuntimeException("error adding role to user.");
        }
    }

    @Override
    public UserDto getMyDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userByEmail(authentication.getName());
    }

}
