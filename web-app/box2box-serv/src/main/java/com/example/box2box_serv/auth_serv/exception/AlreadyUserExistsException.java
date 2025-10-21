package com.example.box2box_serv.auth_serv.exception;

public class AlreadyUserExistsException extends Exception{
    public AlreadyUserExistsException(String message){
        super(message);
    }
}
