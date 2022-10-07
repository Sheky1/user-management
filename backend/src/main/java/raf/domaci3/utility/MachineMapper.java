package raf.domaci3.utility;


import org.springframework.stereotype.Component;
import raf.domaci3.dto.CreateMachineDto;
import raf.domaci3.dto.ErrorMessageDto;
import raf.domaci3.dto.MachineDto;
import raf.domaci3.exceptions.NotFoundException;
import raf.domaci3.model.ErrorMessage;
import raf.domaci3.model.Machine;
import raf.domaci3.model.Status;
import raf.domaci3.model.User;
import raf.domaci3.repositories.UserRepository;

@Component
public class MachineMapper {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public MachineMapper(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Machine createMachineToMachine(CreateMachineDto createMachineDto) {
        Machine newMachine = new Machine();
        User user = userRepository.findUserById(createMachineDto.getUser().getId()).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        newMachine.setName(createMachineDto.getName());
        newMachine.setUser(user);
        newMachine.setCreatedBy(createMachineDto.getUser().getId());
        newMachine.setStatus(Status.STOPPED);
        newMachine.setActive(true);
        return newMachine;
    }

    public MachineDto machineToMachineDto(Machine machine) {
        MachineDto newMachineDto = new MachineDto();
        newMachineDto.setId(machine.getId());
        newMachineDto.setUser(userMapper.userToUserDto(machine.getUser()));
        newMachineDto.setCreatedBy(machine.getCreatedBy());
        newMachineDto.setName(machine.getName());
        newMachineDto.setStatus(machine.getStatus());
        return newMachineDto;
    }

    public ErrorMessageDto errorToErrorDto(ErrorMessage errorMessage) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto();
        errorMessageDto.setId(errorMessage.getId());
        errorMessageDto.setMessage(errorMessage.getMessage());
        errorMessageDto.setAction(errorMessage.getAction());
        errorMessageDto.setMachineId(errorMessage.getMachineId());
        errorMessageDto.setDate(errorMessage.getDate());
        return errorMessageDto;
    }

}
