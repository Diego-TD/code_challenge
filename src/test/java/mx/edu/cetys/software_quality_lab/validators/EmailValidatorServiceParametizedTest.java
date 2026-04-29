package mx.edu.cetys.software_quality_lab.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidatorServiceParametizedTest {

    private EmailValidatorService emailValidator;

    @BeforeEach
    void beforeEach() {
        //Arrange
         emailValidator = new EmailValidatorService();
    }

//    @Test
//    void shouldReturnFalse_WhenEmailParameterIsNull() {
//        var isValid = emailValidator.isValid(null);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailParameterIsEmptyString() {
//        var isValid = emailValidator.isValid("");
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailParameterContainsOnlyWhitespaceCharacters() {
//                var isValid = emailValidator.isValid("   ");
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailTotalLengthExceedsMaximumAllowedOfFortySevenCharacters() {
//                var tooLong = "a4#b." + "aaaaa" + "x".repeat(47);
//        var isValid = emailValidator.isValid(tooLong);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailDoesNotContainMandatoryDigitFourAnywhereInTheString() {
//                var email = "user#provider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailDoesNotContainExactlyOneHashSeparatorBetweenUserAndProvider() {
//                var email = "user4provider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailContainsMoreThanOneHashSeparatorCharacter() {
//                var email = "us4er#pro#vider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenUserSectionBeforeHashSeparatorIsEmpty() {
//                var email = "#prov4ider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenProviderAndDomainSectionAfterHashSeparatorIsEmpty() {
//                var email = "user4#";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenProviderAndDomainSectionDoesNotContainDotSeparator() {
//                var email = "user4#provider";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenProviderSectionBeforeLastDotIsEmpty() {
//                var email = "user4#.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenDomainSectionAfterLastDotIsEmpty() {
//                var email = "user4#provider.";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenDomainSectionLengthExceedsMaximumOfFiveCharacters() {
//                var email = "user4#provider.abcdef";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenUserSectionContainsCharacterOutsideAllowedAlphanumericAndSpecialCharacters() {
//                var email = "us$er4#provider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenProviderSectionContainsCharacterOtherThanLowercaseLettersDigitsOrDot() {
//                var email = "user4#pro-vider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailContainsUppercaseAlphabeticCharactersAnywhereInTheString() {
//                var email = "User4#provider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }
//
//    @Test
//    void shouldReturnFalse_WhenEmailContainsTwoConsecutiveVowelCharactersFormingADiphthong() {
//                var email = "us4er#aa.provider.com";
//        var isValid = emailValidator.isValid(email);
//        assertFalse(isValid);
//    }

    @Test
    void shouldReturnTrue_WhenEmailSatisfiesAllStructuralCharacterLengthAndBusinessRules() {
                var email = "u.ser_4+ok#pro.vider.c0m";
        var isValid = emailValidator.isValid(email);
        assertTrue(isValid);
    }

    //Prueba parameterizada
    @DisplayName("Validate Bad Emails") //null o vacia
    @ParameterizedTest
    @ValueSource(strings = {
            "user4#provider.abcdef",
            "us$er4#provider.com",
            "user4#pro-vider.com",
            "User4#provider.com",
            "us4er#aa.provider.com"
    })
    void invalidEmails(String email) {
        var isValid = emailValidator.isValid(email);
        assertFalse(isValid);
    }

}