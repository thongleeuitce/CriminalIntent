package com.edu.thongleeuos.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by thongleeuteam on 29/10/2016.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private java.util.Date mDate;
    private Boolean mSolved;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }
    public Crime(UUID uuid){
        mId = uuid;
        mDate = new Date();
    }
    public void setSolved(Boolean solved){
        mSolved = solved;
    }
    public Boolean getSolved(){
        return mSolved;
    }
    public Date getDate(){
        return mDate;
    }
    public void setDate(Date date){
        mDate = date;
    }
    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
