package raf.domaci3.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import raf.domaci3.dto.CreateMachineDto;
import raf.domaci3.dto.ErrorMessageDto;
import raf.domaci3.dto.MachineDto;
import raf.domaci3.dto.UpdateStatusDto;
import raf.domaci3.exceptions.ActionNotAllowedException;
import raf.domaci3.exceptions.CustomException;
import raf.domaci3.exceptions.ForbiddenException;
import raf.domaci3.exceptions.NotFoundException;
import raf.domaci3.model.*;
import raf.domaci3.repositories.ErrorMessageRepository;
import raf.domaci3.repositories.MachineRepository;
import raf.domaci3.repositories.UserRepository;
import raf.domaci3.utility.MachineMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {

    private MachineRepository machineRepository;
    private MachineMapper machineMapper;
    private UserRepository userRepository;
    private ErrorMessageRepository errorMessageRepository;

    public MachineServiceImpl(MachineRepository machineRepository, MachineMapper machineMapper, UserRepository userRepository, ErrorMessageRepository errorMessageRepository) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
        this.userRepository = userRepository;
        this.errorMessageRepository = errorMessageRepository;
    }

    @Override
    public List<MachineDto> getAllMachines(Integer userID) {
        checkEverything("can_search_machines", "search machines");
        List<MachineDto> allMachinesDto = new ArrayList<>();
        for(Machine machine: machineRepository.findAllByCreatedBy(userID)) {
            allMachinesDto.add(machineMapper.machineToMachineDto(machine));
        }
        return allMachinesDto;
    }

    @Override
    public MachineDto getMachine(Integer userID, Integer id) {
        checkEverything("can_search_machines", "search machines");
        Machine machine = machineRepository.findMachineByCreatedByAndId(userID, id).orElseThrow(() -> new NotFoundException(String.format("No machine found.")));
        return machineMapper.machineToMachineDto(machine);
    }

    @Override
    public MachineDto createMachine(CreateMachineDto createMachineDto) {
        checkEverything("can_create_machines", "create machines");
        Machine newMachine = machineMapper.createMachineToMachine(createMachineDto);
        machineRepository.save(newMachine);
        return machineMapper.machineToMachineDto(newMachine);
    }

    public MachineDto destroyMachine(Integer userID, Integer id) {
        checkEverything("can_destroy_machines", "destroy machines");
        Machine machine = machineRepository.findMachineByCreatedByAndId(userID, id).orElseThrow(() -> new NotFoundException(String.format("No machine found.")));
        MachineDto machineDto = machineMapper.machineToMachineDto(machine);
        machineRepository.delete(machine);
        return machineDto;
    }

    @Override
    public MachineDto updateStatus(UpdateStatusDto updateStatusDto) {
        checkEverything("can_" + updateStatusDto.getAction() + "_machines", updateStatusDto.getAction() + " machines");
        Machine machine = machineRepository.findMachineByCreatedByAndId(updateStatusDto.getUserID(), updateStatusDto.getId()).orElseThrow(() -> new NotFoundException(String.format("No machine found.")));
        if(updateStatusDto.getAction().equals("start")) {
            if(machine.getStatus() != Status.STOPPED) throwErrorMessage(updateStatusDto.getAction(), machine.getId(), "stopped");
            machine.setStatus(Status.RUNNING);
        }else if(updateStatusDto.getAction().equals("stop")){
            if(machine.getStatus() != Status.RUNNING) throwErrorMessage(updateStatusDto.getAction(), machine.getId(), "running");
            machine.setStatus(Status.STOPPED);
        }else {
            if(updateStatusDto.getRestartPhase().equals("first")) {
                if(machine.getStatus() != Status.RUNNING) throwErrorMessage(updateStatusDto.getAction(), machine.getId(), "running");
                machine.setStatus(Status.STOPPED);
            } else if (updateStatusDto.getRestartPhase().equals("second")){
                machine.setStatus(Status.RUNNING);
            }
        }
        machineRepository.save(machine);
        return machineMapper.machineToMachineDto(machine);
    }

    @Override
    public List<ErrorMessageDto> getAllErrorMessages() {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        List<Machine> machines = machineRepository.findAllByCreatedBy(loggedUser.getId());
        List<ErrorMessageDto> errorMessageDtos = new ArrayList<>();
        for(Machine machine: machines) {
            List<ErrorMessage> errorMessages = errorMessageRepository.findAllByMachineId(machine.getId());
            for(ErrorMessage errorMessage: errorMessages) {
                errorMessageDtos.add(machineMapper.errorToErrorDto(errorMessage));
            }
        }
        return errorMessageDtos;
    }

    private void throwErrorMessage(String action, Integer id, String currentStatus) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), id, action, "You cannot start a machine that is not " + currentStatus + ".");
        errorMessageRepository.save(errorMessage);
        throw new ActionNotAllowedException("You cannot " + action + " a machine that is not stopped.");
    }

    private void checkEverything(String action, String errorMessage) throws CustomException {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals(action)) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to " + errorMessage + ".");
    }

}
