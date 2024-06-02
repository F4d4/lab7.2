package com.f4d4.general.facility;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 5760575944040770153L;
    private String commandMassage;
    private String password;
    private String login;
    private Ticket ticket;
    public Request(String commandMassage){
        this.commandMassage = commandMassage;
    }

    public String getCommandMassage(){
        return commandMassage;
    }

    @Override
    public String toString(){
        return commandMassage;
    }

    public Request(String commandMassage, Ticket ticket , String login , String password){
        this.commandMassage=commandMassage;
        this.ticket = ticket;
        this.login = login;
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public String getLogin(){
        return login;
    }

    public Ticket getTicket(){
        return ticket;
    }
}
