package raf.domaci3.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raf.domaci3.dto.*;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    List<UserDto> getAllUsers();
    UserDto getUser(Integer id);
    UserDto getLoggedUser(String email);

    UserDto createUser(CreateUserDto createUserDto);
    UserDto updateUser(Integer id, UpdateUserDto updateUserDto);
    UserDto deleteUser(Integer id);

}
