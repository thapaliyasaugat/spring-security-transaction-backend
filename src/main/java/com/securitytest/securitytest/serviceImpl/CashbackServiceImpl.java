package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.CashbackScheme;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.repositories.CashbackRepo;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.CashbackService;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.util.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@CacheConfig(cacheNames = {"cashback"})
public class CashbackServiceImpl implements CashbackService {
    private final ModelMapper modelMapper;
    private final CashbackRepo cashbackRepo;
    private final RoleService roleService;

    public CashbackServiceImpl(ModelMapper modelMapper, CashbackRepo cashbackRepo, RoleService roleService) {
        this.modelMapper = modelMapper;
        this.cashbackRepo = cashbackRepo;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public ApiResponse<CashbackSchemeDto> createCashBackScheme(CashbackRequest cashbackRequest) {
        log.info("Request to create cashback scheme {}",cashbackRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validateRoles(cashbackRequest.getEligibleRoles());
        CashbackScheme cashbackScheme = modelMapper.map(cashbackRequest, CashbackScheme.class);
        cashbackScheme.setActive(true);
        cashbackScheme.setInitiatedBy(authentication.getName());
        CashbackScheme scheme = cashbackRepo.save(cashbackScheme);
        log.info("Cashback scheme created . {}",cashbackRequest);
        return new ApiResponse<>(modelMapper.map(scheme, CashbackSchemeDto.class), "CashbackScheme created successfully.", 0);
    }

    private void validateRoles(Set<RoleDto> roles) {
        List<Role> allRoles = roleService.getAllRoles();
        if (roles.stream().allMatch(roleDto -> allRoles.stream().anyMatch(role -> role.getName().equals(roleDto.getName())))) {
            log.info("Roles included for cashback scheme are valid.");
        } else {
            throw new RuntimeException("Roles included for cashback scheme are invalid or doesn't exists.");
        }
    }

    @Override
//    @Cacheable
    public ApiResponse<CashbackPageableResponse> allCashbackSchemes(PageRequestObj pageRequestObj) {
        log.info("Request for all cashback Scheme pageNo : {} ,pageSize : {}", pageRequestObj.getPageNumber(), pageRequestObj.getPageSize());
        Pageable pageable = PageRequest.of(pageRequestObj.getPageNumber(), pageRequestObj.getPageSize(), Sort.by("createdAt").descending());
        Page<CashbackScheme> page = cashbackRepo.findAll(pageable);
        return getCashbackPageableResponseApiResponse(page);
    }

    @Override
    public List<CashbackScheme> allCashbackSchemeList() {
        return cashbackRepo.findAll();
    }

    private ApiResponse<CashbackPageableResponse> getCashbackPageableResponseApiResponse(Page<CashbackScheme> page) {
        List<CashbackSchemeDto> cashbackSchemeList = page.getContent().stream().map(content -> modelMapper.map(content, CashbackSchemeDto.class)).collect(Collectors.toList());
        CashbackPageableResponse response = new CashbackPageableResponse(cashbackSchemeList, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        return new ApiResponse<>(response, "cashback schemes", 0);
    }

    @Override
    @Cacheable
    public ApiResponse<CashbackSchemeDto> cashbackSchemeById(int id) {
        log.info("Request for cashback Scheme with id : {}", id);
        CashbackScheme scheme = cashbackRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("cashback", "id", String.valueOf(id)));
        return new ApiResponse<>(modelMapper.map(scheme, CashbackSchemeDto.class), "cashback scheme with id " + id, 0);
    }

    @Override
    public ApiResponse<CashbackSchemeDto> updateCashbackScheme(CashbackRequest cashbackRequest, int id) {
        return null;
    }

    @Override
    public ApiResponse<CashbackSchemeDto> updateRolesOfCashbackScheme(CashbackRoleUpdateRequest roleUpdateRequest) {
        return null;
    }

    @Override
    @CacheEvict()
    public ApiResponse<CashbackSchemeDto> deactivateCashbackScheme(int id) {
        log.info("Request to deactivate cashbackScheme {}",id);
        CashbackScheme scheme = cashbackRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("cashbackScheme","id",String.valueOf(id)));
        if (!scheme.isActive()) throw new RuntimeException("Cashback Scheme already deactivated.");
        scheme.setActive(false);
        CashbackScheme updatedScheme = cashbackRepo.save(scheme);
        log.info("CashbackScheme with id {} deactivated.",id);
        return new ApiResponse<>(modelMapper.map(updatedScheme,CashbackSchemeDto.class),"cashback scheme deactivated.",0);
    }

    @Override
    public ApiResponse<CashbackSchemeDto> activateCashbackScheme(int id) {
        log.info("Request to activate cashbackScheme {}",id);
        CashbackScheme scheme = cashbackRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("cashbackScheme","id",String.valueOf(id)));
        if(scheme.isActive()) throw new RuntimeException("Cashback Scheme already activated.");
        scheme.setActive(true);
        CashbackScheme updatedScheme = cashbackRepo.save(scheme);
        log.info("CashbackScheme with id {} activated.",id);
        return new ApiResponse<>(modelMapper.map(updatedScheme,CashbackSchemeDto.class),"cashback scheme activated.",0);
    }

    @Override
    @Cacheable
    public ApiResponse<CashbackPageableResponse> schemesByInitiatedByEmail(PageRequestObj pageRequestObj,String email) {
        if(!CustomValidator.isValidEmail(email)) throw new RuntimeException("Invalid Email.");
        Pageable pageable = PageRequest.of(pageRequestObj.getPageNumber(), pageRequestObj.getPageSize(),Sort.by("createdAt").descending());
        Page<CashbackScheme> cashbackSchemes = cashbackRepo.findByInitiatedBy(pageable,email);
        return getCashbackPageableResponseApiResponse(cashbackSchemes);
    }
}
