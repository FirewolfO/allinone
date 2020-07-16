package com.firewolf.pattern.state.standard;

/**
 * 描述：贫穷状态
 * Author：liuxing
 * Date：2020/7/16
 */
public class NeedyAccountState extends AccountState{


    @Override
    public String getDisplay() {
        return "贫穷";
    }

    public NeedyAccountState(Account account) {
        super(account);
    }
}
