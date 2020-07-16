package com.firewolf.pattern.state.standard;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/16
 */
public class NormalAccountState extends AccountState {
    @Override
    public String getDisplay() {
        return "正常";
    }

    public NormalAccountState(Account account) {
        super(account);
    }
}
