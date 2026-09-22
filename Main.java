package medicare;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final HospitalSystem hospital =
            new HospitalSystem();

    public static void main(String[] args) {

        try (scanner) {
            int choice;
            
            do {
                
                displayMenu();
                
                choice = readInt(
                        "Enter your choice: "
                );
                
                switch (choice) {
                    
                    case 1 -> registerPatient();
                        
                    case 2 -> searchPatient();
                        
                    case 3 -> updatePatient();
                        
                    case 4 -> deletePatient();
                        
                    case 5 -> hospital.displayAllPatients();
                        
                    case 6 -> allocateBed();
                        
                    case 7 -> releaseBed();
                        
                    case 8 -> hospital.getWard()
                                .displayLayout();
                        
                    case 9 -> hospital.getWard()
                                .displayAvailableBeds();
                        
                    case 10 -> hospital.getWard()
                                .displayOccupiedBeds();
                        
                    case 11 -> hospital.displayReport();
                        
                    case 12 -> sortBySurname();
                        
                    case 13 -> sortByPatientId();
                        
                    case 0 -> System.out.println("""
                                                                                        Thank you for using MediCare Hospital System.""");
                        
                    default -> System.out.println(
                                "Invalid option."
                        );
                }
                
            } while (choice != 0);
        }
    }

    // ==================================
    // MAIN MENU
    // ==================================

    private static void displayMenu() {

        System.out.println(
            "\n=========================================="
        );

        System.out.println(
            "       MEDICARE HOSPITAL ADMISSION"
        );

        System.out.println(
            "=========================================="
        );

        System.out.println(
            "1. Register Patient"
        );

        System.out.println(
            "2. Search Patient"
        );

        System.out.println(
            "3. Update Patient"
        );

        System.out.println(
            "4. Delete Patient"
        );

        System.out.println(
            "5. Display All Patients"
        );

        System.out.println(
            "------------------------------------------"
        );

        System.out.println(
            "6. Allocate Bed"
        );

        System.out.println(
            "7. Release Bed"
        );

        System.out.println(
            "8. Display Ward Layout"
        );

        System.out.println(
            "9. Display Available Beds"
        );

        System.out.println(
            "10. Display Occupied Beds"
        );

        System.out.println(
            "------------------------------------------"
        );

        System.out.println(
            "11. Display Ward Report"
        );

        System.out.println(
            "12. Sort Patients by Surname"
        );

        System.out.println(
            "13. Sort Patients by Patient ID"
        );

        System.out.println(
            "------------------------------------------"
        );

        System.out.println(
            "0. Exit"
        );

        System.out.println(
            "=========================================="
        );
    }

    // ==================================
    // REGISTER
    // ==================================

    private static void registerPatient() {

        System.out.println(
            "\n========== REGISTER PATIENT =========="
        );

        String patientId =
            readString("Patient ID: ");

        if (hospital.searchPatient(patientId)
                != null) {

            System.out.println(
                "ERROR: Patient ID already exists."
            );

            return;
        }

        String firstName =
            readString("First Name: ");

        String lastName =
            readString("Last Name: ");

        int age =
            readInt("Age: ");

        String gender =
            readString("Gender: ");

        String condition =
            readString("Medical Condition: ");

        PatientCategory category =
            readCategory();

        Patient patient;

        if (category ==
                PatientCategory.INPATIENT) {

            patient = new Inpatient(
                patientId,
                firstName,
                lastName,
                age,
                gender,
                condition,
                "Ward 1"
            );

        } else {

            patient = new Patient(
                patientId,
                firstName,
                lastName,
                age,
                gender,
                condition,
                category
            );
        }

        if (hospital.registerPatient(patient)) {

            System.out.println(
                "Patient registered successfully."
            );

        } else {

            System.out.println(
                "Patient registration failed."
            );
        }
    }

    // ==================================
    // SEARCH
    // ==================================

    private static void searchPatient() {

        System.out.println(
            "\n========== SEARCH PATIENT =========="
        );

        String patientId =
            readString("Enter Patient ID: ");

        Patient patient =
            hospital.searchPatient(patientId);

        if (patient == null) {

            System.out.println(
                "Patient not found."
            );

        } else {

            patient.displayDetails();
        }
    }

    // ==================================
    // UPDATE
    // ==================================

    private static void updatePatient() {

        System.out.println(
            "\n========== UPDATE PATIENT =========="
        );

        String patientId =
            readString("Patient ID: ");

        Patient patient =
            hospital.searchPatient(patientId);

        if (patient == null) {

            System.out.println(
                "Patient not found."
            );

            return;
        }

        String firstName =
            readString("New First Name: ");

        String lastName =
            readString("New Last Name: ");

        int age =
            readInt("New Age: ");

        String gender =
            readString("New Gender: ");

        String condition =
            readString(
                "New Medical Condition: "
            );

        PatientCategory category =
            readCategory();

        boolean updated =
            hospital.updatePatient(
                patientId,
                firstName,
                lastName,
                age,
                gender,
                condition,
                category
            );

        if (updated) {

            System.out.println(
                "Patient updated successfully."
            );

        } else {

            System.out.println(
                "Patient update failed."
            );
        }
    }

    // ==================================
    // DELETE
    // ==================================

    private static void deletePatient() {

        System.out.println(
            "\n========== DELETE PATIENT =========="
        );

        String patientId =
            readString("Patient ID: ");

        boolean deleted =
            hospital.deletePatient(patientId);

        if (deleted) {

            System.out.println(
                "Patient deleted successfully."
            );

        } else {

            System.out.println(
                "Patient not found."
            );
        }
    }

    // ==================================
    // ALLOCATE BED
    // ==================================

    private static void allocateBed() {

        System.out.println(
            "\n========== ALLOCATE BED =========="
        );

        String patientId =
            readString(
                "Inpatient Patient ID: "
            );

        Patient patient =
            hospital.searchPatient(patientId);

        if (patient == null) {

            System.out.println(
                "Patient not found."
            );

            return;
        }

        if (!(patient instanceof Inpatient)) {

            System.out.println(
                "Only INPATIENT patients "
                + "can be allocated a bed."
            );

            return;
        }

        String bedNumber =
            readString(
                "Bed Number (e.g. B01): "
            );

        boolean allocated =
            hospital.allocateBed(
                patientId,
                bedNumber
            );

        if (allocated) {

            System.out.println(
                "Bed "
                + bedNumber
                + " allocated successfully."
            );

        } else {

            System.out.println(
                "Bed allocation failed."
            );

            System.out.println(
                "Check that:"
            );

            System.out.println(
                "- The bed exists."
            );

            System.out.println(
                "- The bed is available."
            );

            System.out.println(
                "- The inpatient does not "
                + "already have a bed."
            );

            System.out.println(
                "- The ward is not full."
            );
        }
    }

    // ==================================
    // RELEASE BED
    // ==================================

    private static void releaseBed() {

        System.out.println(
            "\n========== RELEASE BED =========="
        );

        String bedNumber =
            readString(
                "Bed Number (e.g. B01): "
            );

        boolean released =
            hospital.releaseBed(bedNumber);

        if (released) {

            System.out.println(
                "Bed "
                + bedNumber
                + " released successfully."
            );

        } else {

            System.out.println(
                "Bed could not be released."
            );
        }
    }

    // ==================================
    // SORT BY SURNAME
    // ==================================

    private static void sortBySurname() {

        hospital.sortBySurname();

        System.out.println(
            "\nPatients sorted by surname:"
        );

        hospital.displayAllPatients();
    }

    // ==================================
    // SORT BY PATIENT ID
    // ==================================

    private static void sortByPatientId() {

        hospital.sortByPatientId();

        System.out.println(
            "\nPatients sorted by Patient ID:"
        );

        hospital.displayAllPatients();
    }

    // ==================================
    // PATIENT CATEGORY
    // ==================================

    private static PatientCategory readCategory() {

        while (true) {

            System.out.println(
                "\nPatient Category:"
            );

            System.out.println(
                "1. Inpatient"
            );

            System.out.println(
                "2. Outpatient"
            );

            System.out.println(
                "3. Emergency"
            );

            int choice =
                readInt("Choose category: ");

            switch (choice) {

                case 1 -> {
                    return PatientCategory.INPATIENT;
                }

                case 2 -> {
                    return PatientCategory.OUTPATIENT;
                }

                case 3 -> {
                    return PatientCategory.EMERGENCY;
                }

                default -> System.out.println(
                        "Invalid category."
                    );
            }
        }
    }

    // ==================================
    // INPUT METHODS
    // ==================================

    private static String readString(
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                "Input cannot be empty."
            );
        }
    }

    private static int readInt(
            String message) {

        while (true) {

            System.out.print(message);

            try {

                int value =
                    Integer.parseInt(
                        scanner.nextLine().trim()
                    );

                if (value < 0) {

                    System.out.println(
                        "Please enter a positive number."
                    );

                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                    "Please enter a valid number."
                );
            }
        }
    }
}