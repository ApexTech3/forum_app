package com.forum.exceptions;

public class AlreadyLikedDislikedException extends RuntimeException{

    public AlreadyLikedDislikedException(String message) {
        super(message);
    }
}
