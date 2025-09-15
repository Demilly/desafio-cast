package br.com.domain.port;

import br.com.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    List<Account> findAll();

    Optional<Account> findByNumber(Long number);

    void save(Account account);

    Optional<Account> findByNumberForUpdate(Long number);
}
