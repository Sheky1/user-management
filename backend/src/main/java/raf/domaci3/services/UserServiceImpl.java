package raf.domaci3.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raf.domaci3.dto.*;
import raf.domaci3.exceptions.CustomException;
import raf.domaci3.exceptions.ForbiddenException;
import raf.domaci3.exceptions.NotAcceptableException;
import raf.domaci3.exceptions.NotFoundException;
import raf.domaci3.model.Permission;
import raf.domaci3.model.User;
import raf.domaci3.repositories.UserRepository;
import raf.domaci3.utility.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        checkEverything("can_read_users", "read users");
        List<UserDto> allUsersDto = new ArrayList<>();
        for(User user: userRepository.findAll()) {
            allUsersDto.add(userMapper.userToUserDto(user));
        }
        return allUsersDto;
    }

    @Override
    public UserDto getUser(Integer id) {
        checkEverything("can_read_users", "read users");
        User user = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        checkEverything("can_create_users", "create users");
        User newUser = userMapper.createUserToUser(createUserDto);
        try {
            userRepository.save(newUser);
        }catch (Exception e) {
            throw new NotAcceptableException("Email already in usage.");
        }
        return userMapper.userToUserDto(newUser);
    }

    @Override
    public UserDto updateUser(Integer id, UpdateUserDto updateUserDto) {
        checkEverything("can_update_users", "update users");
        User user = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
        user = userMapper.updateUser(user, updateUserDto);
        try {
            userRepository.save(user);
        }catch (Exception e) {
            throw new NotAcceptableException("Email already in usage.");
        }
        return userMapper.userToUserDto(user);
    }

    public UserDto deleteUser(Integer id) {
        checkEverything("can_delete_users", "delete users");
        User user = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
        UserDto userDto = userMapper.userToUserDto(user);
        userRepository.delete(user);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("No user with email: %s found.", email)));
        if(user == null) {
            throw new UsernameNotFoundException("User with email "+email+" not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getLoggedUser(String email) {
        User user = this.userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("No user with email: %s found.", email)));
        return userMapper.userToUserDto(user);
    }

    private void checkEverything(String action, String errorMessage) throws CustomException {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals(action)) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to " + errorMessage + ".");
    }
}
