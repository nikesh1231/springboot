package com.evbackend.service;

import com.evbackend.commands.CreateAccountCommand;
import com.evbackend.model.users.Account;
import com.evbackend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Mono<List<Account>> getAllAccounts() {
        return this.accountRepository.getAllAccounts().convert().with(toMono());
    }

    public Mono<Account> create(CreateAccountCommand accountCommand) {

        Account account = Account.builder().accountName(accountCommand.getAccountName()).build();

        Mono<Account> createdAccount = this.accountRepository.create(account).convert().with(toMono());
        return createdAccount.map(u -> u);
    }

}
