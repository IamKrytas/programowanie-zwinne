package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminStudentServiceTest {

    private @Mock StudentRepository studentRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @InjectMocks AdminStudentService adminStudentService;

    @Test
    public void test_getStudentById_shouldReturnStudentWhenExistsInDatabase() throws Exception {
        Student mockedStudent = new Student("123", "An", "Cz", "a@c.com", true, "pass");
        when(studentRepository.findById(Mockito.any())).thenReturn(Optional.of(mockedStudent));
        Student studentFromService = adminStudentService.getStudentById("123");

        assertThat(studentFromService).isNotNull();
        assertThat(studentFromService.getId()).isEqualTo("123");
        assertThat(studentFromService.getName()).isEqualTo("An");
        assertThat(studentFromService.getEmail()).isEqualTo("a@c.com");
        assertThat(studentFromService.getSurname()).isEqualTo("Cz");
        assertThat(studentFromService.isStationary()).isTrue();

        verify(studentRepository, times(1)).findById(Mockito.any());

    }

    @Test
    public void test_getStudentList_shouldReturnStudentListWhenExistsInDatabase() throws Exception {
        List<Student> mockedStudents = new ArrayList<>();
        mockedStudents.add(new Student("123", "An", "Cz", "a@c.com", true, "pass"));
        mockedStudents.add(new Student("133", "Bn", "Dz", "b@d.com", false, "pass1"));
        mockedStudents.add(new Student("143", "Cn", "Ez", "c@e.com", true, "pass2"));
        mockedStudents.add(new Student("153", "Dn", "Fz", "d@f.com", false, "pass3"));
        mockedStudents.add(new Student("163", "En", "Gz", "e@g.com", true, "pass4"));

        Page<Student> mockedPage = new PageImpl<>(mockedStudents.subList(0, 4));

        when(studentRepository.findAll(PageRequest.of(0, 4))).thenReturn(mockedPage);

        List<Student> studentFromService = adminStudentService.getStudents(0, 4);

        assertThat(studentFromService).isNotNull();
        assertThat(studentFromService).hasSize(4);

        verify(studentRepository, times(1)).findAll(PageRequest.of(0, 4));
    }

    @Test
    public void test_createStudent_shouldCreateNewStudentWhenEmialNotExistsInDatabase() throws Exception {
        Student newStudent = new Student();
        newStudent.setId("1233");
        newStudent.setEmail("a@c.com");
        newStudent.setName("An");
        newStudent.setSurname("Bn");
        newStudent.setStationary(false);
        newStudent.setPassword("pass");
        when(studentRepository.findByEmail("a@c.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("passEncoded");
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        Student creaatedStudent = adminStudentService.createStudent(newStudent);

        assertThat(creaatedStudent).isNotNull();
        assertThat(creaatedStudent.getEmail()).isEqualTo("a@c.com");
        assertThat(creaatedStudent.getPassword()).isEqualTo("passEncoded");

        verify(studentRepository, times(1)).findByEmail("a@c.com");
        verify(passwordEncoder, times(1)).encode("pass");
        verify(studentRepository, times(1)).save(any(Student.class));

    }

    @Test
    public void test_editStudent_shouldEditStudentWhenExistsInDatabase() throws Exception {
        String studentId = "123";
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setEmail("a@c.com");
        existingStudent.setName("An");
        existingStudent.setSurname("Cz");
        existingStudent.setPassword("pass");
        existingStudent.setStationary(true);

        Student editedStudent = new Student();
        editedStudent.setEmail("new@a.com");
        editedStudent.setName("NewName");
        editedStudent.setSurname("NewSurname");
        editedStudent.setPassword("newPassword");
        editedStudent.setStationary(true);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(passwordEncoder.encode("newPassword")).thenReturn("enNewPass");
        //when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student edited = adminStudentService.editStudent(studentId, editedStudent);

        assertThat(edited).isNotNull();
        assertThat(edited.getName()).isEqualTo("NewName");
        assertThat(edited.getSurname()).isEqualTo("NewSurname");
        assertThat(edited.getEmail()).isEqualTo("new@a.com");
        assertThat(edited.getPassword()).isEqualTo("enNewPass");
        assertThat(edited.isStationary()).isTrue();

        verify(studentRepository).findById(studentId);
        verify(studentRepository).findByEmail("new@a.com");
        verify(passwordEncoder).encode("newPassword");
        verify(studentRepository).save(existingStudent);

    }

    @Test
    public void test_getStudentById_shouldThrowExceptionWhenNotExistsInDatabase() throws Exception {
        when(studentRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            adminStudentService.getStudentById("12334");
        }).isInstanceOf(NoSuchElementException.class);

        verify(studentRepository, times(1)).findById(Mockito.any());
    }

}
