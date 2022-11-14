package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.BalanceLoadDetail;
import com.securitytest.securitytest.repositories.BalanceLoadRepo;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.BalanceLoadDetailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BalanceLoadDetailServiceImpl implements BalanceLoadDetailService {
    private final BalanceLoadRepo balanceLoadRepo;
    private final ModelMapper modelMapper;

    public BalanceLoadDetailServiceImpl(BalanceLoadRepo balanceLoadRepo, ModelMapper modelMapper) {
        this.balanceLoadRepo = balanceLoadRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<LoadBalancePageableResponse> myBalanceLoadedDetail(PageRequestObj pageRequestObj) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Request to get loaded balance history by ,{}", authentication.getName());
        Pageable pageable = PageRequest.of(pageRequestObj.getPageNumber(), pageRequestObj.getPageSize(), Sort.by("createdAt").descending());
        Page<BalanceLoadDetail> balanceLoadDetailPage = balanceLoadRepo.findAllByLoadedBy(authentication.getName(), pageable);
        return getLoadBalancePageableResponseApiResponse(balanceLoadDetailPage);
    }

    @Override
    public ApiResponse<LoadBalancePageableResponse> AllBalanceLoadedDetail(PageRequestObj pageRequestObj) {
        log.info("Request to get all loaded balance history.");
        Pageable pageable = PageRequest.of(pageRequestObj.getPageNumber(), pageRequestObj.getPageSize(), Sort.by("created_at").descending());
        Page<BalanceLoadDetail> balanceLoadDetailPage = balanceLoadRepo.findAll(pageable);
        return getLoadBalancePageableResponseApiResponse(balanceLoadDetailPage);
    }

    @Override
    public ApiResponse<LoadBalancePageableResponse> AllBalanceLoadedDetailByEmail(String email, PageRequestObj pageRequestObj) {
        log.info("Request to get loaded balance history by email,{}", email);
        Pageable pageable = PageRequest.of(pageRequestObj.getPageNumber(), pageRequestObj.getPageSize(), Sort.by("created_at").descending());
        Page<BalanceLoadDetail> balanceLoadDetailPage = balanceLoadRepo.findAllByLoadedBy(email, pageable);
        return getLoadBalancePageableResponseApiResponse(balanceLoadDetailPage);
    }

    private ApiResponse<LoadBalancePageableResponse> getLoadBalancePageableResponseApiResponse(Page<BalanceLoadDetail> balanceLoadDetailPage) {
        List<BalanceLoadDetailDto> balanceLoadDetailDtos = balanceLoadDetailPage.getContent().stream().map(b -> modelMapper.map(b, BalanceLoadDetailDto.class)).collect(Collectors.toList());
        LoadBalancePageableResponse response = new LoadBalancePageableResponse();
        response.setContent(balanceLoadDetailDtos);
        response.setPageNumber(balanceLoadDetailPage.getNumber());
        response.setPageSize(balanceLoadDetailPage.getSize());
        response.setTotalNoOfPages(balanceLoadDetailPage.getTotalPages());
        response.setTotalNoOfElements(balanceLoadDetailPage.getTotalElements());
        return new ApiResponse<>(response, "loaded balance pageable response", 0);
    }
}
