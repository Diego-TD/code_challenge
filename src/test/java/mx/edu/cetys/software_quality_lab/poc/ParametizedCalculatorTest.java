package mx.edu.cetys.software_quality_lab.poc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParametizedCalculatorTest {

    //    @Test
    @DisplayName("Test Sum of 2 parameters with CVS Source")
    @ParameterizedTest
    @CsvSource({
            "10,10,20",
            "0,0,0,5", //extra params
            "-5,5,0",
            "'1','2','3'" //valid char instead of integer
    })
    //@Test Suspision ¬¬
    void testSumWithCsvSource(int a, int b, int expected) {
        //arrange
        //act
        var sum = a + b;
        assertEquals(expected,sum);
    }

    @DisplayName("Validate String not empty") //null o vacia
    @ParameterizedTest
    @ValueSource(strings = {
            "hello",
            "world",
            "aaron"
    })
    void testValidateStringNotEmpty(String values) {
        var isEmpty = values.isEmpty();
        assertFalse(isEmpty);
    }

    @ParameterizedTest
    @DisplayName("Validate double of an integer with MethodSource")
    @MethodSource("provideNumbers")
    void testDouble(int a, int expected) {
        //Act
        //var doubleValue = a + a;
        var doubleValue = a * 2;
        assertEquals(expected, doubleValue);
    }

    @ParameterizedTest
    @DisplayName("Validate Pet is older than 10 years old")
    @MethodSource("providePets")
    void testDouble(Pet pet, boolean expected) {
        //Act
        var isOlderThanTen = pet.age() > 10 ? true : false;
        assertEquals(expected, isOlderThanTen);
    }

    public static Stream<Object[]> provideNumbers() {
        return Stream.of(
                new Object[]{2, 4},
                new Object[]{5,10}
                //new Object[]{"Hola", "HolaHola"}
                );
    }

    public static Stream<Object[]> providePets() {
        return Stream.of(
                new Object[]{new Pet("Perrito", "negrito", 15,"Perro"), true},
                new Object[]{new Pet(null,null,10,null), false},
                new Object[]{new Pet(null,null,-10,null), false},
                new Object[]{new Pet(null,null,11,null), true}
        );
    }

    //POJO - Plain Old Java Object: Clase con getters and setters
    //Records - POJO inmutable sin BoilerPlate
    private record Pet(String name, String color, int age, String race){};
}
