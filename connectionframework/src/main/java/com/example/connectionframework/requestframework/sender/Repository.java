/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.example.connectionframework.requestframework.sender;

public class Repository {
    private static Repository repository;
    private String messageError;
    private int statusCode;
    private int action;
    private Repository() {
    }

    public static Repository newInstance(){
        if (repository == null){
            repository = new Repository();
        }
        return repository;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
