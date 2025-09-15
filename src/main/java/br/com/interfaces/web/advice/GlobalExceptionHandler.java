package br.com.interfaces.web.advice;

import br.com.application.exception.AccountNotFoundException;
import br.com.application.exception.BusinessException;
import br.com.application.exception.OptimisticLockRetryException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/client/operations";
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFound(AccountNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/client/operations";
    }

    @ExceptionHandler(OptimisticLockRetryException.class)
    public String handleOptimisticLock(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Falha por concorrência, tente novamente");
        return "redirect:/client/operations";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArg(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/client/operations";
    }
}
