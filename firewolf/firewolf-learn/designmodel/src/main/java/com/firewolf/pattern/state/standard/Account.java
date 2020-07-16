package com.firewolf.pattern.state.standard;

/**
 * 描述：环境类
 * Author：liuxing
 * Date：2020/7/16
 */
public class Account {
    /**
     * 账户名
     */
    private String account;

    /**
     * 账户金额
     */
    private Integer money;


    private AccountState state;


    public Account(String account) {
        this.account = account;
        this.money = 0;
        this.state = new NeedyAccountState(this);
    }

    /**
     * 存钱
     *
     * @param money
     */
    public void saveMoney(int money) {
        this.money += money;
        checkState();
    }

    /**
     * 取钱
     *
     * @param money
     */
    public void getMoney(int money) {
        this.money -= money;
        checkState();
    }


    public void display() {
        System.out.println("账户当前状态是：" + state.getDisplay());
    }

    private void checkState() {
        if (this.money <= 20) {
            state = new NeedyAccountState(this);
        } else if (this.money > 100) {
            state = new RichAccountState(this);
        } else {
            state = new NormalAccountState(this);
        }
    }
}
