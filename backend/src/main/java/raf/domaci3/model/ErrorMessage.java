package raf.domaci3.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Date date;
    @Column
    private Integer machineId;
    @Column
    private String action;
    @Column
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(Date date, Integer machineId, String action, String message) {
        this.date = date;
        this.machineId = machineId;
        this.action = action;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
