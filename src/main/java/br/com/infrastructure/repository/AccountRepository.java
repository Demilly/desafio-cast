package br.com.infrastructure.repository;

import br.com.domain.Account;
import br.com.domain.port.AccountRepositoryPort;
import br.com.infrastructure.jpa.JpaAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository implements AccountRepositoryPort {

    private final JpaAccountRepository jpaAccountRepository;

    @Override
    public List<Account> findAll() {
        return jpaAccountRepository.findAll();
    }

    @Override
    public Optional<Account> findByNumber(Long number) {
        return jpaAccountRepository.findByNumber(number);
    }

    @Override
    public void save(Account account) {
        jpaAccountRepository.save(account);
    }

    @Override
    public Optional<Account> findByNumberForUpdate(Long number) {
        return jpaAccountRepository.findByNumberForUpdate(number);
    }
}