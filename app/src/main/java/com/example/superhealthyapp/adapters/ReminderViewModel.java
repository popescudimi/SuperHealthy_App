package com.example.superhealthyapp.adapters;

public class ReminderViewModel {
    private int ID;
    private String title;
    private String date;
    private String details;
    private String repeat;
    private String repeatNo;
    private String repeatType;

    ReminderViewModel(int ID, String title, String date, String details, String repeat, String repeatNo, String repeatType) {
        this.ID = ID;
        this.title = title;
        this.date = date;
        this.details = details;
        this.repeat = repeat;
        this.repeatNo = repeatNo;
        this.repeatType = repeatType;

    }

    public int getId()
    {
        return ID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDate()
    {
        return date;
    }

    public String getDetails()
    {
        return getDetails();
    }

    public String getRepeat()
    {
        return getRepeat();
    }

    public String getRepeatNo()
    {
        return getRepeatNo();
    }

    public String getRepeatType()
    {
        return getRepeatType();
    }



}
