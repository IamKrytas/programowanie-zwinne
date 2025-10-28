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
        Student mockedStudent = new Student();
        mockedStudent.setId(123L);
        mockedStudent.setName("An");
        mockedStudent.setSurname("Cz");
        mockedStudent.setEmail("a@c.com");
        mockedStudent.setStationary(true);
        mockedStudent.setPassword("pass");
        
        when(studentRepository.findById(123L)).thenReturn(Optional.of(mockedStudent));
        Student studentFromService = adminStudentService.getStudentById("123");

        assertThat(studentFromService).isNotNull();
        assertThat(studentFromService.getId()).isEqualTo(123L);
        assertThat(studentFromService.getName()).isEqualTo("An");
        assertThat(studentFromService.getEmail()).isEqualTo("a@c.com");
        assertThat(studentFromService.getSurname()).isEqualTo("Cz");
        assertThat(studentFromService.isStationary()).isTrue();

        verify(studentRepository, times(1)).findById(123L);

    }
    @Test
    public void test_getStudentById_shouldThrowExceptionWhenNotExistsInDatabase() throws Exception {
        when(studentRepository.findById(12334L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            adminStudentService.getStudentById("12334");
        }).isInstanceOf(NoSuchElementException.class);

        verify(studentRepository, times(1)).findById(12334L);
    }

    @Test
    public void test_getStudents_shouldReturnStudentListWhenExistsInDatabase() throws Exception {
        List<Student> mockedStudents = new ArrayList<>();
        
        Student student1 = new Student();
        student1.setId(123L);
        student1.setName("An");
        student1.setSurname("Cz");
        student1.setEmail("a@c.com");
        student1.setStationary(true);
        student1.setPassword("pass");
        mockedStudents.add(student1);
        
        Student student2 = new Student();
        student2.setId(133L);
        student2.setName("Bn");
        student2.setSurname("Dz");
        student2.setEmail("b@d.com");
        student2.setStationary(false);
        student2.setPassword("pass1");
        mockedStudents.add(student2);
        
        Student student3 = new Student();
        student3.setId(143L);
        student3.setName("Cn");
        student3.setSurname("Ez");
        student3.setEmail("c@e.com");
        student3.setStationary(true);
        student3.setPassword("pass2");
        mockedStudents.add(student3);
        
        Student student4 = new Student();
        student4.setId(153L);
        student4.setName("Dn");
        student4.setSurname("Fz");
        student4.setEmail("d@f.com");
        student4.setStationary(false);
        student4.setPassword("pass3");
        mockedStudents.add(student4);
        
        Student student5 = new Student();
        student5.setId(163L);
        student5.setName("En");
        student5.setSurname("Gz");
        student5.setEmail("e@g.com");
        student5.setStationary(true);
        student5.setPassword("pass4");
        mockedStudents.add(student5);

        Page<Student> mockedPage = new PageImpl<>(mockedStudents.subList(0, 4));

        when(studentRepository.findAll(PageRequest.of(0, 4))).thenReturn(mockedPage);

        List<Student> studentFromService = adminStudentService.getStudents(0, 4);

        assertThat(studentFromService).isNotNull();
        assertThat(studentFromService).hasSize(4);

        verify(studentRepository, times(1)).findAll(PageRequest.of(0, 4));
    }

    @Test
    public void test_createStudent_shouldCreateNewStudentWhenEmailNotExistsInDatabase() throws Exception {
        Student newStudent = new Student();
        newStudent.setId(1233L);
        newStudent.setEmail("a@c.com");
        newStudent.setName("An");
        newStudent.setSurname("Bn");
        newStudent.setStationary(false);
        newStudent.setPassword("pass");
        when(studentRepository.findByEmail("a@c.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("passEncoded");
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        Student createdStudent = adminStudentService.createStudent(newStudent);

        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getEmail()).isEqualTo("a@c.com");
        assertThat(createdStudent.getPassword()).isEqualTo("passEncoded");

        verify(studentRepository, times(1)).findByEmail("a@c.com");
        verify(passwordEncoder, times(1)).encode("pass");
        verify(studentRepository, times(1)).save(any(Student.class));

    }
    @Test
    public void test_createStudent_shouldThrowExceptionWhenEmailExistsInDatabase() throws Exception {
        Student mockedStudent = new Student();
        mockedStudent.setId(123L);
        mockedStudent.setName("An");
        mockedStudent.setSurname("Cz");
        mockedStudent.setEmail("a@c.com");
        mockedStudent.setStationary(true);
        mockedStudent.setPassword("pass");
        
        when(studentRepository.findByEmail("a@c.com")).thenReturn(Optional.of(mockedStudent));

        assertThatThrownBy(() -> adminStudentService.createStudent(mockedStudent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");

        verify(studentRepository, times(1)).findByEmail("a@c.com");
        verify(studentRepository, never()).save(any());
    }
    @Test
    public void test_editStudent_shouldEditStudentWhenExistsInDatabase() throws Exception {
        String studentId = "123";
        Student existingStudent = new Student();
        existingStudent.setId(123L);
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
        when(studentRepository.findById(123L)).thenReturn(Optional.of(existingStudent));
        when(passwordEncoder.encode("newPassword")).thenReturn("enNewPass");

        Student edited = adminStudentService.editStudent(studentId, editedStudent);

        assertThat(edited).isNotNull();
        assertThat(edited.getName()).isEqualTo("NewName");
        assertThat(edited.getSurname()).isEqualTo("NewSurname");
        assertThat(edited.getEmail()).isEqualTo("new@a.com");
        assertThat(edited.getPassword()).isEqualTo("enNewPass");
        assertThat(edited.isStationary()).isTrue();

        verify(studentRepository).findById(123L);
        verify(studentRepository).findByEmail("new@a.com");
        verify(passwordEncoder).encode("newPassword");
        verify(studentRepository).save(existingStudent);

    }

    @Test
    public void test_deleteStudent_shouldDeleteStudentWhenExistsInDatabase() throws Exception {
        String studentId = "123";
        Student mockedStudent = new Student();
        mockedStudent.setId(123L);
        mockedStudent.setName("An");
        mockedStudent.setSurname("Cz");
        mockedStudent.setEmail("a@c.com");
        mockedStudent.setStationary(true);
        mockedStudent.setPassword("pass");
        
        when(studentRepository.findById(123L)).thenReturn(Optional.of(mockedStudent));

        Student deletedStudent = adminStudentService.deleteStudent(studentId);

        // then
        assertThat(deletedStudent).isNotNull();
        assertThat(deletedStudent.getId()).isEqualTo(123L);

        verify(studentRepository).findById(123L);
        verify(studentRepository).delete(mockedStudent);

    }

}
