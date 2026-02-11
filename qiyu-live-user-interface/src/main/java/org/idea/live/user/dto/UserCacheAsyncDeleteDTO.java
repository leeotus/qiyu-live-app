package org.idea.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserCacheAsyncDeleteDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2291922809338528918L;

    private int code;
    private String json;
}
