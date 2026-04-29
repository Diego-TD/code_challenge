package mx.edu.cetys.software_quality_lab.poc;

import mx.edu.cetys.software_quality_lab.validators.EmailValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockitoListTest {

    @Mock
    List<String> mockList;

    @Mock
    EmailValidatorService emailValidatorServiceMock;

//    @BeforeEach
//    void setup(){
//        mockList = Mockito.mock(List.class);
//    }

    @Test
    void shouldReturn_CustomSizeWhenMocked(){
        //Arrange: Is already done for my mockList

        //Define Mocks Behavior
        when(mockList.size())
                .thenReturn(999_999_999)
                .thenReturn(1)
                .thenReturn(67)
                .thenThrow(new RuntimeException())
                .thenReturn(0);
        //act var size = mockList.size();
        //assert
        assertEquals(999_999_999,mockList.size());
        assertEquals(1,mockList.size());
        assertEquals(67,mockList.size());
        assertThrows(RuntimeException.class, ()-> mockList.size());
        assertEquals(0,mockList.size());
    }

    @Test
    void shouldMock_ListGetWithParameters(){
        //Define Mock Behavior
        when(mockList.get(0))
                .thenReturn("Hello")
                .thenReturn("Aaron")
                .thenReturn("Cetys");
        when(mockList.get(1)).thenReturn("World");

        assertEquals("Hello",mockList.get(0));
        assertEquals("World",mockList.get(1));
        assertEquals("Aaron",mockList.get(0));
        assertEquals("Cetys",mockList.get(0));
    }

    @Test
    void mock_EmailValidator_withArgumentMatchers(){
        //Define Mock Behavior
        when(emailValidatorServiceMock.isValid(anyString()))
                .thenReturn(true);
        when(emailValidatorServiceMock.isValid(isNull())) //isNotNull
                .thenReturn(false)
        .thenReturn(true);

        assertTrue(emailValidatorServiceMock.isValid("aeiou"));
        assertFalse(emailValidatorServiceMock.isValid(null));
        assertTrue(emailValidatorServiceMock.isValid(null));
        assertTrue(emailValidatorServiceMock.isValid(null));
        assertTrue(emailValidatorServiceMock.isValid(""));
        assertTrue(emailValidatorServiceMock.isValid("@"));
    }





}
