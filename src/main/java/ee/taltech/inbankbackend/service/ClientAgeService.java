package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanClientAgeException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ClientAgeService {
    HashMap<String, Integer> personalCodeFirstNumberAge = new HashMap<>(
            Map.of(
                    "1", 1800,
                    "2", 1800,
                    "3", 1900,
                    "4", 1900,
                    "5", 2000,
                    "6", 2000,
                    "7", 2100,
                    "8", 2100
            )
    );
    protected int getClientAge(String personalCode) {
        int year = personalCodeFirstNumberAge.get(personalCode.substring(0, 1)) + Integer.parseInt(personalCode.substring(1, 3));
        int month = Integer.parseInt(personalCode.substring(3, 5));
        int day = Integer.parseInt(personalCode.substring(5, 7));
        LocalDate clientBirthDay = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        int clientAge = now.getYear() - clientBirthDay.getYear();
        if(LocalDate.of(1900, now.getMonthValue(), now.getDayOfMonth()).isBefore(LocalDate.of(1900, clientBirthDay.getMonthValue(), clientBirthDay.getDayOfMonth()))){
            clientAge--;
        }

        return clientAge;
    }
    protected void isClientAgeValid(String personalCode) throws InvalidLoanClientAgeException {
        int clientAge = getClientAge(personalCode);
        if(clientAge < 18){
            throw new InvalidLoanClientAgeException("Client is under 18 years old!");
        }
        if(clientAge > (DecisionEngineConstants.ARBITRARY_LIFE_TIME_VALUE / 12)){
            throw new InvalidLoanClientAgeException(String.format("Client is over %s years old!", DecisionEngineConstants.ARBITRARY_LIFE_TIME_VALUE / 12));
        }
    }

}
