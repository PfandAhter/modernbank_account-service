package com.modernbank.account_service.api.request;

import com.modernbank.account_service.validator.annotations.*;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.modernbank.account_service.constants.ErrorMessageConstants.*;

@Getter
@Setter
public class CreateUserRequest {

    //@UniqueTCKN(message = TCKN_NOT_UNIQUE)
    //@ValidTCKN(message = TCKN_NOT_VALID)
    //@NotBlank(message = TCKN_NOT_BLANK)
    //private String tckn;

    //@ValidString(message = FIRST_NAME_NOT_VALID)
    //@NotBlank(message = FIRST_NAME_NOT_BLANK)
    private String name;

    //@Nullable
    //private String secondName;

    //@ValidString(message = LAST_NAME_NOT_VALID)
    //@NotBlank(message = LAST_NAME_NOT_BLANK)
    //private String lastName;

    @UniqueEmail(message = EMAIL_NOT_VALID)
    @NotBlank(message = EMAIL_NOT_BLANK)
    private String email;

    @ValidPassword(message = PASSWORD_NOT_VALID)
    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;

    private String confirmPassword;

    @UniqueGSM(message = GSM_NOT_VALID)
    @NotBlank(message = GSM_NOT_BLANK)
    private String gsm;

    //@ValidDateOfBirth(message = DATE_OF_BIRTH_NOT_VALID)
    //@NotBlank(message = DATE_OF_BIRTH_NOT_BLANK)
    //private String dateOfBirth;
}
