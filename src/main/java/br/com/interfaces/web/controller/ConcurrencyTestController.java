package br.com.interfaces.web.controller;

import br.com.application.exception.BusinessException;
import br.com.application.service.AccountTransactionService;
import br.com.domain.dto.TransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test/concurrency")
public class ConcurrencyTestController {

    private final AccountTransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> simulateTransfer(@RequestBody TransferDto dto,
                                              @RequestParam String strategy) {
        try {
            if ("optimistic".equalsIgnoreCase(strategy)) {
                transactionService.transferWithOptimisticRetry(dto);
            } else {
                transactionService.transferWithPessimisticLock(dto);
            }
            return ResponseEntity.ok(Map.of("success", true));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", "Erro inesperado: " + e.getMessage()
            ));
        }
    }
}


