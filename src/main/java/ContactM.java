package pshopmvc;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ContactM {

    @NotNull
    @Pattern(regexp = ".+")
    private String firstname;
    @NotNull
    @Pattern(regexp = ".+")
    private String fullname;
    @NotNull
    @Pattern(regexp = "^.+@.+\\..+$")
    private String senderemail;
    @NotNull
    @Pattern(regexp = "^.+@.+\\..+$")
    private String recemail;
    @NotNull
    @Pattern(regexp = ".+")
    private String message;
    @NotNull
    @Pattern(regexp = ".+")
    private String mlogin;
    @NotNull
    @Pattern(regexp = ".+")
    private String mpass;
    @NotNull
    @Pattern(regexp = ".+")
    private String host;
    @NotNull
    @Min(1)
    private Integer port;
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSenderemail() {
        return senderemail;
    }

    public void setSenderemail(String senderemail) {
        this.senderemail = senderemail;
    }

    public String getRecemail() {
        return recemail;
    }

    public void setRecemail(String recemail) {
        this.recemail = recemail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMlogin() {
        return mlogin;
    }

    public void setMlogin(String mlogin) {
        this.mlogin = mlogin;
    }

    public String getMpass() {
        return mpass;
    }

    public void setMpass(String mpass) {
        this.mpass = mpass;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    

}
