package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.*;
import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {
    private final LoanInputValidator loanInputValidator = new LoanInputValidator();
    private final CreditModifierService creditModifierCalculator = new CreditModifierService();
    private final ClientAgeService clientAgeService = new ClientAgeService();
    private int creditModifier = 0;

    /**
     * Calculates the maximum loan amount and period for the customer based on their ID code,
     * the requested loan amount and the loan period.
     * The loan period must be between 12 and 60 months (inclusive).
     * The loan amount must be between 2000 and 10000€ months (inclusive).
     *
     * @param personalCode ID code of the customer that made the request.
     * @param loanAmount Requested loan amount
     * @param loanPeriod Requested loan period
     * @return A Decision object containing the approved loan amount and period, and an error message (if any)
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException If the requested loan period is invalid
     * @throws NoValidLoanException If there is no valid loan found for the given ID code, loan amount and loan period
     */
    public Decision calculateApprovedLoan(String personalCode, Long loanAmount, int loanPeriod)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException,
            NoValidLoanException, InvalidLoanClientAgeException {
        loanInputValidator.verifyInputs(personalCode, loanAmount, loanPeriod);
        creditModifier = creditModifierCalculator.getCreditModifier(personalCode);
        clientAgeService.isClientAgeValid(personalCode);
        int outputLoanAmount;
        while (highestValidLoanAmount(loanPeriod) < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
            loanPeriod++;
        }
        if (loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            outputLoanAmount = Math.min(DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT, highestValidLoanAmount(loanPeriod));
        } else {
            throw new NoValidLoanException("No valid loan found!");
        }
        if(isLoanApproved(outputLoanAmount, loanPeriod, creditModifier)) {
            return new Decision(outputLoanAmount, loanPeriod, null);
        }
        throw new NoValidLoanException("No valid loan found!");
    }

    /**
     * Calculates the largest valid loan for the current credit modifier and loan period.
     *
     * @return Largest valid loan amount
     */
    private int highestValidLoanAmount(int loanPeriod) {
        return creditModifier * loanPeriod;
    }
    /**
     * Checks if the loan is approved based on the credit modifier, loan amount and loan period.
     *
     * @return true if the loan is approved, false otherwise
     */
    private boolean isLoanApproved(int loanAmount, int loanPeriod, int creditModifier) {
        return (double) creditModifier / loanAmount * loanPeriod >= 1;
    }
}
