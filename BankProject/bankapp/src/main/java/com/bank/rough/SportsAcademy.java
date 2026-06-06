package com.bank.rough;

import ch.qos.logback.core.read.ListAppender;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class SportsAcademy {

    public static void main(String args[]) {

        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.nextLine();

        String[] applicationRecord = new String[N];
        for (int i = 0; i < N; i++) {
            applicationRecord[i] = scanner.nextLine();
        }

        scanner.close();

        try {
            Map<Long, String> result = new TreeMap<>();
            result = SportsAcademy.formCommittee(applicationRecord);

            for (Long key : result.keySet()) {
                System.out.println(result.get(key) + "-" + key);
            }

        } catch (SportsCommitteeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Applicant> initialCheckAndFilter(String[] applicationData) throws SportsCommitteeException{
        int n = applicationData.length;
        if(n<5) throw new SportsCommitteeException("Committee cannot be constituted as there are not enough applications present");

        List<Applicant>list = new ArrayList<>();

        for(String data: applicationData){
            String applicant[] = data.split(" ");

            if(applicant.length < 4) continue;
            else{
                LocalDate endDate = LocalDate.of(2026, 1, 1);
                LocalDate birthDate = LocalDate.parse(applicant[1]);
                long age = ChronoUnit.YEARS.between(birthDate, endDate);
                System.out.println(applicant[0]+"'s Age is: "+age);
                if(age < 18)continue;
            }
                String name = applicant[0];
                LocalDate birthDate = LocalDate.parse(applicant[1]);
                Long mobile = Long.valueOf(applicant[2]);
                LocalDateTime timeStamp  = LocalDateTime.parse(applicant[3]);
                Applicant app = new Applicant(name, birthDate, mobile, timeStamp);
                list.add(app);
        }

        if(list.size()<5) throw new SportsCommitteeException("Not enough valid applicant data available to proceed with committee formation");
        return list;
    }

    public static Map<Long, String> formCommittee(String[] applicationData)
            throws SportsCommitteeException {

        Map<Long, String> resultMap = new TreeMap<>();

        List<Applicant> list = SportsAcademy.initialCheckAndFilter(applicationData);
        Set<Applicant>set = new HashSet<>(list);
        List<Applicant>sorted = new ArrayList<>(set);
        sorted.sort(Comparator.comparing((Applicant a)-> a.getTimeStamp()));

        if(sorted.size() < 5)throw new SportsCommitteeException("Not enough valid applicants remain after removing duplicates");
        for(Applicant app: sorted){
            resultMap.put(app.getContactNumber(), app.getName());
            if(resultMap.size()>=5)break;
        }
        return resultMap;
    }
}

class SportsCommitteeException extends Exception {

    private static final long serialVersionUID = 1L;

    public SportsCommitteeException(String message) {
        super(message);
    }
}

class Applicant {

    private String name;
    private LocalDate dateOfBirth;
    private Long contactNumber;
    private LocalDateTime timeStamp;

    public Applicant() {
        super();
    }

    public Applicant(String name, LocalDate dateOfBirth,
                     Long contactNumber, LocalDateTime timeStamp) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", contactNumber=" + contactNumber +
                ", timeStamp=" + timeStamp +
                '}';
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Long getContactNumber() {
        return contactNumber;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactNumber, dateOfBirth, name, timeStamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Applicant other = (Applicant) obj;

        return Objects.equals(contactNumber, other.contactNumber)
                && Objects.equals(dateOfBirth, other.dateOfBirth)
                && Objects.equals(name, other.name)
                && Objects.equals(timeStamp, other.timeStamp);
    }
}