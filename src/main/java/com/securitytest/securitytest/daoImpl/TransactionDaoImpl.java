package com.securitytest.securitytest.daoImpl;

import com.securitytest.securitytest.dao.TransactionDao;
import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.TransactionPageRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.UserService;
import com.securitytest.securitytest.util.DateValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class TransactionDaoImpl implements TransactionDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ModelMapper modelMapper;
    private final UserService userService;


    public TransactionDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ModelMapper modelMapper, UserService userService) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public Page<Transactions> allTransactions(TransactionPageRequest transactionPageRequest, UserDto user, Pageable pageable) {
        String query = "select * from transactions ";
        Map<String,Object> parameter = new HashMap<>();
        if(user!=null){
            parameter.put("id", user.getId());
            if (transactionPageRequest.getFilter().equals("SEND") || transactionPageRequest.getFilter().equals("RECEIVED")) {
                if (transactionPageRequest.getFilter().equals("SEND")) {
                    query += "where transaction_from = :id ";
                }
                if (transactionPageRequest.getFilter().equals("RECEIVED")) {
                    query += "where transaction_to = :id ";
                }
            } else {
                query += "where (transaction_from = :id or transaction_to = :id )";
            }
        }
        if(transactionPageRequest.getFromDate() != null && transactionPageRequest.getToDate() != null){
            try {
                Date from = new SimpleDateFormat("yyyy-MM-dd").parse(transactionPageRequest.getFromDate());
                Date to = new SimpleDateFormat("yyyy-MM-dd").parse(transactionPageRequest.getToDate());
                DateValidator.ValidateDateRange(from, to);
                parameter.put("fromDate", from);
                parameter.put("toDate", to);
                query += "and created_at between :fromDate and :toDate ";
            }catch (Exception e){
                log.info("ERROR WHILE PARSING DATE :: {}",e.getMessage());
                throw new RuntimeException("Invalid Date type or interval.");
            }
        }
        if(transactionPageRequest.getFromAmount()!=null && transactionPageRequest.getToAmount()!=null){
            query+= "and amount between :fromAmount and :toAmount";
            if(transactionPageRequest.getFromAmount()>transactionPageRequest.getToAmount()){
                throw new RuntimeException("Invalid Amount Range.");
            }
            parameter.put("fromAmount", transactionPageRequest.getFromAmount());
            parameter.put("toAmount", transactionPageRequest.getToAmount());
        }
        if(transactionPageRequest.getCode()!=null){
            query+= " and code=:code";
            parameter.put("code",transactionPageRequest.getCode());
        }
        query+= " order by created_at desc";
        List<Transactions> transactionsList = namedParameterJdbcTemplate.query(query,parameter, this::resultSetToTransactions);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), transactionsList.size());
        Page<Transactions> transactionsPage;
        transactionsPage = new PageImpl<>(transactionsList.subList(start,end),pageable, transactionsList.size());
        return transactionsPage;
    }
    private Transactions resultSetToTransactions(ResultSet resultSet,int index){
        try {
            log.info("converting result set to transaction object.");
            UserDto fromUser = userService.userById(resultSet.getInt("transaction_from")).getData();
            UserDto toUser = userService.userById(resultSet.getInt("transaction_to")).getData();
            return Transactions.builder()
                    .id(resultSet.getInt("id"))
                    .code(resultSet.getString("code"))
                    .customer_from(modelMapper.map(fromUser,User.class))
                    .customer_to(modelMapper.map(toUser,User.class))
                    .amount(resultSet.getDouble("amount"))
                    .build();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("error mapping resultSet");
        }
    }
}
