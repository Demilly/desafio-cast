package br.com.interfaces.web.controller;

import br.com.application.service.AccountService;
import br.com.domain.dto.TransactionDto;
import br.com.domain.dto.TransferDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/client")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/operations")
    public String operations(Model model) {
        if (!model.containsAttribute("transactionDto")) {
            model.addAttribute("transactionDto", new TransactionDto());
        }
        if (!model.containsAttribute("transferDto")) {
            model.addAttribute("transferDto", new TransferDto());
        }
        return "client/operations";
    }

    @PostMapping("/credit")
    public String credit(@Valid @ModelAttribute TransactionDto transactionDto,
                         BindingResult br,
                         RedirectAttributes redirectAttributes) {

        if (br.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.transactionDto", br);
            redirectAttributes.addFlashAttribute("transactionDto", transactionDto);
            return "redirect:/client/operations";
        }

        accountService.credit(transactionDto);
        redirectAttributes.addFlashAttribute("message", "Crédito realizado com sucesso");
        return "redirect:/client/operations";
    }

    @PostMapping("/debit")
    public String debit(@Valid @ModelAttribute TransactionDto transactionDto,
                        BindingResult br,
                        RedirectAttributes redirectAttributes) {

        if (br.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.transactionDto", br);
            redirectAttributes.addFlashAttribute("transactionDto", transactionDto);
            return "redirect:/client/operations";
        }

        accountService.debit(transactionDto);
        redirectAttributes.addFlashAttribute("message", "Débito realizado com sucesso");
        return "redirect:/client/operations";
    }

    @PostMapping("/transfer")
    public String transfer(@Valid @ModelAttribute TransferDto transferDto,
                           BindingResult br,
                           RedirectAttributes redirectAttributes,
                           @RequestParam(value = "strategy", defaultValue = "optimistic") String strategy) {

        if (br.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.transferDto", br);
            redirectAttributes.addFlashAttribute("transferDto", transferDto);
            return "redirect:/client/operations";
        }

        boolean pessimistic = "pessimistic".equalsIgnoreCase(strategy);
        accountService.transfer(transferDto, pessimistic);
        redirectAttributes.addFlashAttribute("message", "Transferência realizada com sucesso");
        return "redirect:/client/operations";
    }
}
