package raf.domaci3.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.domaci3.dto.CreateMachineDto;
import raf.domaci3.dto.UpdateStatusDto;
import raf.domaci3.exceptions.CustomException;
import raf.domaci3.services.MachineService;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "/machines", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class MachineController {

    @Qualifier(value = "machineServiceImpl")
    private MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<?> getAllMachines(@PathVariable("userID") Integer userID) {
        try{
            return new ResponseEntity<>(machineService.getAllMachines(userID), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @GetMapping("/{userID}/{id}")
    public ResponseEntity<?> getMachine(@PathVariable("userID") Integer userID, @PathVariable("id") Integer id) {
        try{
            return new ResponseEntity<>(machineService.getMachine(userID, id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @PostMapping()
    public ResponseEntity<?> createMachine(@RequestBody @Valid CreateMachineDto createMachineDto) {
        try{
            return new ResponseEntity<>(machineService.createMachine(createMachineDto), HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @DeleteMapping("/{userID}/{id}")
    public ResponseEntity<?> destroyMachine(@PathVariable("userID") Integer userID, @PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<>(machineService.destroyMachine(userID, id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody @Valid UpdateStatusDto updateStatusDto) {
        try{
            return new ResponseEntity<>(machineService.updateStatus(updateStatusDto), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @GetMapping("/error-messages")
    public ResponseEntity<?> getAllErrorMessages() {
        try{
            return new ResponseEntity<>(machineService.getAllErrorMessages(), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }


}
