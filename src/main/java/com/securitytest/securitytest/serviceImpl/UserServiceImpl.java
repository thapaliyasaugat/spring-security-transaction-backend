package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.BalanceLoadDetail;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.repositories.BalanceLoadRepo;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final BalanceLoadRepo balanceLoadRepo;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepo userRepo, BalanceLoadRepo balanceLoadRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.balanceLoadRepo = balanceLoadRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<UserDto> userById(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", String.valueOf(id)));
        return new ApiResponse<>(modelMapper.map(user, UserDto.class), "user with id ".concat(String.valueOf(id)), 0);
    }

    @Override
    public ApiResponse<UserPageableResponse> allUsers(PageRequestObj pageRequest) {
        Pageable p = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
        Page<User> users = userRepo.findAll(p);
        List<UserDto> userDtos = users.getContent().stream().map(u -> modelMapper.map(u, UserDto.class)).collect(Collectors.toList());
        UserPageableResponse response = new UserPageableResponse();
        response.setContent(userDtos);
        response.setPageNumber(users.getNumber());
        response.setPageSize(users.getSize());
        response.setTotalNoOfPages(users.getTotalPages());
        response.setTotalNoOfElements(users.getTotalElements());
        return new ApiResponse<>(response, "pageable response of user data.", 0);
    }

    @Override
    public ApiResponse<UserDto> blockUser(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", String.valueOf(id)));
        log.info("Request to block user {}", user.getEmail());
        if (user.getStatus().equals(UserStatus.BLOCKED)) throw new RuntimeException("User Already Blocked.");
        user.setStatus(UserStatus.BLOCKED);
        User blockedUser = userRepo.save(user);
        return new ApiResponse<>(modelMapper.map(blockedUser, UserDto.class), "", 0);
    }

    @Override
    public ApiResponse<UserDto> activateUser(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", String.valueOf(id)));
        log.info("Request to activate user {}", user.getEmail());
        if (user.getStatus().equals(UserStatus.ACTIVE)) throw new RuntimeException("User Already Active.");
        user.setStatus(UserStatus.ACTIVE);
        User activatedUser = userRepo.save(user);
        return new ApiResponse<>(modelMapper.map(activatedUser, UserDto.class), "activated: ".concat(user.getEmail()), 0);
    }

    @Override
    public UserDto userByEmail(String email) {
        try {
            return modelMapper.map(userRepo.findByEmail(email), UserDto.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException("user", "email", email);
        }
    }

    @Override
    public UserDto save(User user) {
        return modelMapper.map(userRepo.save(user), UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepo.findByEmail(userDto.getEmail());
        if (userDto.getUserName() != null) user.setUserName(userDto.getUserName());
        if (userDto.getBalance() != null) user.setBalance(userDto.getBalance());
        if (userDto.getStatus() != null) user.setStatus(userDto.getStatus());
        User user1 = userRepo.save(user);
        return modelMapper.map(user1, UserDto.class);
    }

    @Override
    public void addUserRole(RoleDto roleDto, int id) {
        try {
            User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", String.valueOf(id)));
            user.addRole(modelMapper.map(roleDto, Role.class));
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("error adding role to user.");
        }
    }

    @Override
    public ApiResponse<UserDto> getMyDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ApiResponse<>(userByEmail(authentication.getName()), "detail of: ".concat(authentication.getName()), 0);
    }

    @Override
    @Transactional
    public ApiResponse<UserDto> loadBalance(LoadBalanceRequest loadBalanceRequest) {
        log.info("Request to load balance , {}", loadBalanceRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(authentication.getName());
        if (loadBalanceRequest.getAmount() > 100000) throw new RuntimeException("can't load more than 100000.");
        userRepo.updateBalanceByEmail(authentication.getName(), loadBalanceRequest.getAmount(), user.getBalance());
        log.info("balance updated.");
        BalanceLoadDetail balanceLoadDetail = new BalanceLoadDetail();
        log.info("Saving balance loaded detail");
        balanceLoadDetail.setLoadedBy(authentication.getName());
        balanceLoadDetail.setLoadedFrom(loadBalanceRequest.getLoadedFrom());
        balanceLoadDetail.setAmount(loadBalanceRequest.getAmount());
        balanceLoadRepo.save(balanceLoadDetail);
        return new ApiResponse<>(modelMapper.map(user, UserDto.class), "Balance Loaded Sucessfully.", 0);
    }
}
