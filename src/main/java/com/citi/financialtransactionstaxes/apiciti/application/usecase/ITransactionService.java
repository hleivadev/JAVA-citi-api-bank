package com.citi.financialtransactionstaxes.apiciti.application.usecase;

import java.util.List;

import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionResponseDTO;

/** Application Layer */
public interface ITransactionService {

    TransactionResponseDTO createTransaction(TransactionDTO transactionDTO);

    List<TransactionResponseDTO> getAllTransactions();
    
    TransactionResponseDTO getTransactionById(String id);

}
