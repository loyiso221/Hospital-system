package medicare;

public class Bed {

    private final String bedNumber;
    private boolean occupied;
    private Inpatient patient;

    public Bed(String bedNumber) {
        this.bedNumber = bedNumber;
        this.occupied = false;
        this.patient = null;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Inpatient getPatient() {
        return patient;
    }

    public void allocate(Inpatient patient) {
        this.patient = patient;
        this.occupied = true;
    }

    public void release() {
        this.patient = null;
        this.occupied = false;
    }
}
