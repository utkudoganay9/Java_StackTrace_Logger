package org.example.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionResult {

    private TransactionStatus transactionStatus;
    private List<String> messages;
    private Object data;
    private String accessToken;

    public TransactionResult() {
        initialize();
    }

    private void initialize() {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        if (this.transactionStatus == null) {
            this.transactionStatus = TransactionStatus.NOT_STARTED;
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
