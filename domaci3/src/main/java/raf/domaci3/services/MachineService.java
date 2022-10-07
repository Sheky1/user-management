package raf.domaci3.services;

import org.springframework.stereotype.Service;
import raf.domaci3.dto.CreateMachineDto;
import raf.domaci3.dto.ErrorMessageDto;
import raf.domaci3.dto.MachineDto;
import raf.domaci3.dto.UpdateStatusDto;

import java.util.List;

@Service
public interface MachineService {

    List<MachineDto> getAllMachines(Integer userID);
    MachineDto getMachine(Integer userID, Integer id);

    MachineDto createMachine(CreateMachineDto createMachineDto);
    MachineDto destroyMachine(Integer userID, Integer id);
    MachineDto updateStatus(UpdateStatusDto updateStatusDto);
    List<ErrorMessageDto> getAllErrorMessages();

}
