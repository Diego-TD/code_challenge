package mx.edu.cetys.software_quality_lab.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidatorServiceTest {

    @Test
    void shouldReturnFalse_WhenEmailParameterIsNull() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var isValid = emailValidator.isValid(null);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailParameterIsEmptyString() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var isValid = emailValidator.isValid("");
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailParameterContainsOnlyWhitespaceCharacters() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var isValid = emailValidator.isValid("   ");
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailTotalLengthExceedsMaximumAllowedOfFortySevenCharacters() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var tooLong = "a4#b." + "aaaaa" + "x".repeat(47);
        var isValid = emailValidator.isValid(tooLong);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailDoesNotContainMandatoryDigitFourAnywhereInTheString() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user#provider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailDoesNotContainExactlyOneHashSeparatorBetweenUserAndProvider() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user4provider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailContainsMoreThanOneHashSeparatorCharacter() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "us4er#pro#vider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenUserSectionBeforeHashSeparatorIsEmpty() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "#prov4ider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenProviderAndDomainSectionAfterHashSeparatorIsEmpty() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user4#";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenProviderAndDomainSectionDoesNotContainDotSeparator() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user4#provider";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenProviderSectionBeforeLastDotIsEmpty() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user4#.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenDomainSectionAfterLastDotIsEmpty() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user4#provider.";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenDomainSectionLengthExceedsMaximumOfFiveCharacters() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user4#provider.abcdef";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenUserSectionContainsCharacterOutsideAllowedAlphanumericAndSpecialCharacters() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "us$er4#provider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenProviderSectionContainsCharacterOtherThanLowercaseLettersDigitsOrDot() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "user4#pro-vider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailContainsUppercaseAlphabeticCharactersAnywhereInTheString() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "User4#provider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalse_WhenEmailContainsTwoConsecutiveVowelCharactersFormingADiphthong() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "us4er#aa.provider.com";
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnTrue_WhenEmailSatisfiesAllStructuralCharacterLengthAndBusinessRules() {
        EmailValidatorService emailValidator = new EmailValidatorService();
        var email = "u.ser_4+ok#pro.vider.c0m";
        var isValid = emailValidator.isValid(email);
        assertTrue(isValid);
    }
}