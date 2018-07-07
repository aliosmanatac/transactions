package application.controller;

import application.data.Statistics;
import application.data.Transaction;
import application.service.TransactionService;
import application.service.TransactionServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(@NonNull final TransactionServiceImpl transactionServiceImpl) {
        this.transactionService = transactionServiceImpl;
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity transactions(@RequestBody Transaction transaction) {
        if(transactionService.addTransaction(transaction))
            return new ResponseEntity(HttpStatus.CREATED);
        else
            return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public ResponseEntity<Statistics> statistics() {
        return ResponseEntity.ok(transactionService.getStatistics());
    }
}
