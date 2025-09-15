package br.com.infrastructure.jpa;


import br.com.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface JpaAccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumber(Long number);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.number = :number")
    Optional<Account> findByNumberForUpdate(Long number);
}
