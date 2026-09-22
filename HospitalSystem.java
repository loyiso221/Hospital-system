package medicare;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HospitalSystem
{

    private final List<Patient> patients;
    private final HospitalWard ward;

    public HospitalSystem()
    {

        patients = new ArrayList<>();
        ward = new HospitalWard();
    }

    // ==========================
    // PATIENT MANAGEMENT
    // ==========================

    public boolean registerPatient(Patient patient)
    {

        if (patient == null)
        {
            return false;
        }

        // Prevent duplicate Patient IDs
        if (searchPatient(patient.getPatientId()) != null)
        {
            return false;
        }

        patients.add(patient);

        return true;
    }

    public Patient searchPatient(String patientId)
    {

        for (Patient patient : patients)
        {

            if (patient.getPatientId()
                    .equalsIgnoreCase(patientId))
            {

                return patient;
            }
        }

        return null;
    }

    public boolean updatePatient(
            String patientId,
            String firstName,
            String lastName,
            int age,
            String gender,
            String medicalCondition,
            PatientCategory category)
    {

        Patient patient = searchPatient(patientId);

        if (patient == null)
        {
            return false;
        }

        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setMedicalCondition(medicalCondition);

        /*
         * Inpatient patients must remain INPATIENT
         * because they may already have a bed.
         */
        if (patient instanceof Inpatient) {
            patient.setCategory(PatientCategory.INPATIENT);
        } else
        {
            patient.setCategory(category);
        }

        return true;
    }

    public boolean deletePatient(String patientId)
    {

        Patient patient = searchPatient(patientId);

        if (patient == null)
        {
            return false;
        }

        // Release inpatient's bed before deleting patient
        if (patient instanceof Inpatient inpatient) {


            if (!inpatient.getBedNumber().equalsIgnoreCase("None"))
            {

                ward.releaseBed(
                    inpatient.getBedNumber()
                );
            }
        }

        patients.remove(patient);

        return true;
    }

    public List<Patient> getPatients()
    {
        return patients;
    }

    // ==========================
    // BED MANAGEMENT
    // ==========================

    public boolean allocateBed(
            String patientId,
            String bedNumber)
    {

        Patient patient = searchPatient(patientId);

        if (patient == null)
        {
            return false;
        }

        // Only Inpatients can have beds
        if (!(patient instanceof Inpatient)) {
            return false;
        }

        Inpatient inpatient = (Inpatient)patient;

        // One inpatient can only have one bed
        if (!inpatient.getBedNumber()
                .equalsIgnoreCase("None"))
        {

            return false;
        }

        // Prevent allocation when all beds are occupied
        if (ward.areAllBedsOccupied())
        {
            return false;
        }

        return ward.allocateBed(
            bedNumber,
            inpatient
        );
    }

    public boolean releaseBed(String bedNumber)
    {

        return ward.releaseBed(bedNumber);
    }

    public HospitalWard getWard()
    {
        return ward;
    }

    // ==========================
    // REPORTS
    // ==========================

    public int getTotalPatients()
    {
        return patients.size();
    }

    public int getTotalOccupiedBeds()
    {
        return ward.getOccupiedBedCount();
    }

    public int getTotalAvailableBeds()
    {
        return ward.getAvailableBedCount();
    }

    public double getOccupancyPercentage()
    {

        return (
            (double)getTotalOccupiedBeds() / 20
        ) * 100;
    }

    public void displayAllPatients()
    {

        if (patients.isEmpty())
        {

            System.out.println(
                "\nNo patients registered."
            );

            return;
        }

        System.out.println(
            "\n========== REGISTERED PATIENTS =========="
        );

        for (Patient patient : patients)
        {
            patient.displayDetails();
        }
    }

    public void displayReport()
    {

        System.out.println(
            "\n=========================================="
        );
        System.out.println(
            "           MEDICARE WARD REPORT"
        );
        System.out.println(
            "=========================================="
        );

        System.out.println(
            "Total Registered Patients: "
            + getTotalPatients()
        );

        System.out.println(
            "Total Available Beds: "
            + getTotalAvailableBeds()
        );

        System.out.println(
            "Total Occupied Beds: "
            + getTotalOccupiedBeds()
        );

        System.out.printf(
            "Ward Occupancy: %.2f%%%n",
            getOccupancyPercentage()
        );

        System.out.println(
            "=========================================="
        );
    }

    // ==========================
    // SORTING
    // ==========================

    public void sortBySurname()
    {

        patients.sort(
            Comparator.comparing(
                Patient::getLastName,
                String.CASE_INSENSITIVE_ORDER
            )
        );
    }

    public void sortByPatientId()
    {

        patients.sort(
            Comparator.comparing(
                Patient::getPatientId,
                String.CASE_INSENSITIVE_ORDER
            )
        );
    }
}
