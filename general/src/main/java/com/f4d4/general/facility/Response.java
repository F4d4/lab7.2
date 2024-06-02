package com.f4d4.general.facility;

import java.io.Serializable;

public class Response  implements Serializable {
    private String massage;
    private boolean result;
    public Response (String massage){
        this.massage = massage;
    }

    public Response(String massage , boolean result){
        this.massage = massage;
        this.result = result;
    }

    public String getMessage(){
        return massage;
    }

    public boolean getResult(){
        return result;
    }
}
