package com.example.pc.testapplication;

import java.io.Serializable;

/**
 * Created by pc on 24-Jul-17.
 */

public class Picture implements Serializable {

    private String id;
    private String secret;
    private String server;
    private String farm;


    public Picture(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
