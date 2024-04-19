package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;
import org.springframework.stereotype.Service;

@Service
public class CreditModifierService {
    /**
     * Calculates the credit modifier based on the last 4 digits of the personal code.
     *
     * @return Credit modifier value as int
     */
    protected int getCreditModifier(String personalCode) throws NoValidLoanException {
        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));

        if (segment < 2500) {
            throw new NoValidLoanException("No valid loan found!");
        } else if (segment < 5000) {
            return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
        } else if (segment < 7500) {
            return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
        }

        return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
    }
}
