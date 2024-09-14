package com.app.Bank.common;

public class Constants {
    public enum Role{
        ADMIN,BANK_MANAGER,CUSTOMER
    }
    public enum Mop{
        IMPS, NEFT, RTGS, UPI
    }
    public enum TxnStatus{
        SUCCESS, FAILED, IN_PROGRESS, HOLD
    }
    public enum Branch{
        DEL,MUM,BLR
    }
    public enum Status{
        IN_ACTIVE, ACTIVE
    }
}
