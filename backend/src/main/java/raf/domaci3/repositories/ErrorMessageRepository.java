package raf.domaci3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.domaci3.model.ErrorMessage;

import java.util.List;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Integer> {

    public List<ErrorMessage> findAllByMachineId(Integer machineId);

}
