package br.com.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;


@Data
@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long number;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    private Long version;

    public Account(Long number, String ownerName, BigDecimal balance) {
        this.number = number;
        this.ownerName = ownerName;
        this.balance = balance;
    }
}