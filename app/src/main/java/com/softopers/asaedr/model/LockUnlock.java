package com.softopers.asaedr.model;

import java.io.Serializable;

public class LockUnlock implements Serializable
{
    private LockUnLockDay LockUnLockDay;

    public LockUnLockDay getLockUnLockDay ()
    {
        return LockUnLockDay;
    }

    public void setLockUnLockDay (LockUnLockDay LockUnLockDay)
    {
        this.LockUnLockDay = LockUnLockDay;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [LockUnLockDay = "+LockUnLockDay+"]";
    }
}