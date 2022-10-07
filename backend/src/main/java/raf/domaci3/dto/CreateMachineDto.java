package raf.domaci3.dto;

import javax.validation.constraints.NotBlank;

public class CreateMachineDto {

    @NotBlank
    private String name;
    @NotBlank
    private UserDto user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
