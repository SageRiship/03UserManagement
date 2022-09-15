package com.sage.request;

import lombok.Data;

@Data
public class ActivateRequest {
private String email;
private String tempPassword;
private String newPassword;
private String confirmPassword;
}
