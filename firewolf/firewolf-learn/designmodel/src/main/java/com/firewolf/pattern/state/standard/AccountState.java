package com.firewolf.pattern.state.standard;

import lombok.Data;

/**
 * 描述：抽象状态类
 * Author：liuxing
 * Date：2020/7/16
 */
@Data
public abstract class AccountState {

    private Account account;

    public abstract String getDisplay();

    public AccountState(Account account) {
        this.account = account;
    }
}
