package com.firewolf.pattern.state.standard;

/**
 * 描述：贫穷状态
 * Author：liuxing
 * Date：2020/7/16
 */
public class RichAccountState extends AccountState{


    @Override
    public String getDisplay() {
        return "富有";
    }

    public RichAccountState(Account account) {
        super(account);
    }
}
