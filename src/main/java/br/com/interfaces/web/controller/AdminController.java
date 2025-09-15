package br.com.interfaces.web.controller;

import br.com.application.service.AccountService;
import br.com.domain.dto.AccountCreateDto;
import br.com.domain.dto.AccountDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AccountService accountService;

    public AdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public String listAccounts(Model model) {
        List<AccountDto> accounts = accountService.findAll();
        model.addAttribute("accounts", accounts);
        return "admin/accounts";
    }

    @GetMapping("/accounts/new")
    public String newAccountForm(Model model) {
        model.addAttribute("accountCreateDto", new AccountCreateDto());
        return "admin/new-account";
    }

    @PostMapping("/accounts")
    public String createAccount(@Valid @ModelAttribute AccountCreateDto dto, BindingResult br, Model model) {
        if (br.hasErrors()) {
            return "admin/new-account";
        }
        accountService.createAccount(dto);
        return "redirect:/admin/accounts";
    }
}