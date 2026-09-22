package medicare;

import java.util.ArrayList;
import java.util.List;

public class HospitalWard {

    private static final int TOTAL_BEDS = 20;

    private final List<Bed> beds;

    public HospitalWard() {

        beds = new ArrayList<>();

        for (int i = 1; i <= TOTAL_BEDS; i++) {

            String bedNumber = String.format("B%02d", i);

            beds.add(new Bed(bedNumber));
        }
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public Bed findBed(String bedNumber) {

        for (Bed bed : beds) {

            if (bed.getBedNumber().equalsIgnoreCase(bedNumber)) {
                return bed;
            }
        }

        return null;
    }

    public boolean allocateBed(String bedNumber, Inpatient patient) {

        Bed bed = findBed(bedNumber);

        if (bed == null) {
            return false;
        }

        if (bed.isOccupied()) {
            return false;
        }

        bed.allocate(patient);

        patient.setBedNumber(bed.getBedNumber());

        return true;
    }

    public boolean releaseBed(String bedNumber) {

        Bed bed = findBed(bedNumber);

        if (bed == null || !bed.isOccupied()) {
            return false;
        }

        Inpatient patient = bed.getPatient();

        if (patient != null) {
            patient.setBedNumber("None");
        }

        bed.release();

        return true;
    }

    public int getAvailableBedCount() {

        int count = 0;

        for (Bed bed : beds) {

            if (!bed.isOccupied()) {
                count++;
            }
        }

        return count;
    }

    public int getOccupiedBedCount() {

        int count = 0;

        for (Bed bed : beds) {

            if (bed.isOccupied()) {
                count++;
            }
        }

        return count;
    }

    public boolean areAllBedsOccupied() {
        return getOccupiedBedCount() == TOTAL_BEDS;
    }

    public void displayLayout() {

        System.out.println("\n========== WARD LAYOUT ==========");

        for (int i = 0; i < beds.size(); i++) {

            Bed bed = beds.get(i);

            String status;

            if (bed.isOccupied()) {
                status = "OCCUPIED";
            } else {
                status = "AVAILABLE";
            }

            System.out.printf(
                "%-6s %-12s",
                bed.getBedNumber(),
                status
            );

            // Five beds per row
            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }

        System.out.println("=================================");
    }

    public void displayAvailableBeds() {

        System.out.println("\n========== AVAILABLE BEDS ==========");

        boolean found = false;

        for (Bed bed : beds) {

            if (!bed.isOccupied()) {

                System.out.println(
                    bed.getBedNumber()
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No beds available.");
        }

        System.out.println("====================================");
    }

    public void displayOccupiedBeds() {

        System.out.println("\n========== OCCUPIED BEDS ==========");

        boolean found = false;

        for (Bed bed : beds) {

            if (bed.isOccupied()) {

                Inpatient patient = bed.getPatient();

                System.out.println(
                    bed.getBedNumber()
                    + " -> "
                    + patient.getFirstName()
                    + " "
                    + patient.getLastName()
                    + " ("
                    + patient.getPatientId()
                    + ")"
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No beds occupied.");
        }

        System.out.println("===================================");
    }
}