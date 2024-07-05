package com.areksoft.api.models.request;

import com.areksoft.api.models.Key;
import com.areksoft.api.models.User;
import lombok.Data;

@Data
public class AddUserRequest {
    User user;
    Key key;
    AddUserRequest(User user, Key key){
        this.user = user;
        this.key = key;
    }
}
