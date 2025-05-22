package com.project.service;

import com.project.model.Teacher;
import com.project.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class AdminTeacherServiceTest {

    private @Mock TeacherRepository teacherRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @InjectMocks AdminTeacherService adminTeacherService;

    @Test
    public void test_getTeacherById_shouldReturnTeacherWhenExistsInDatabase() throws Exception {
        Teacher mockedTeacher = new Teacher("123", "An", "Cz", "a@c.com", "pass");
        when(teacherRepository.findById(Mockito.any())).thenReturn(Optional.of(mockedTeacher));
        Teacher TeacherFromService = adminTeacherService.getTeacherById("123");

        assertThat(TeacherFromService).isNotNull();
        assertThat(TeacherFromService.getId()).isEqualTo("123");
        assertThat(TeacherFromService.getName()).isEqualTo("An");
        assertThat(TeacherFromService.getEmail()).isEqualTo("a@c.com");
        assertThat(TeacherFromService.getSurname()).isEqualTo("Cz");

        verify(teacherRepository, times(1)).findById(Mockito.any());

    }
    @Test
    public void test_getTeacherById_shouldThrowExceptionWhenNotExistsInDatabase() throws Exception {
        when(teacherRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            adminTeacherService.getTeacherById("12334");
        }).isInstanceOf(NoSuchElementException.class);

        verify(teacherRepository, times(1)).findById(Mockito.any());
    }
    @Test
    public void test_getTeachers_shouldReturnTeacherListWhenExistsInDatabase() throws Exception {
        List<Teacher> mockedTeachers = new ArrayList<>();
        mockedTeachers.add(new Teacher("123", "An", "Cz", "a@c.com", "pass"));
        mockedTeachers.add(new Teacher("133", "Bn", "Dz", "b@d.com", "pass1"));
        mockedTeachers.add(new Teacher("143", "Cn", "Ez", "c@e.com", "pass2"));
        mockedTeachers.add(new Teacher("153", "Dn", "Fz", "d@f.com", "pass3"));
        mockedTeachers.add(new Teacher("163", "En", "Gz", "e@g.com", "pass4"));

        Page<Teacher> mockedPage = new PageImpl<>(mockedTeachers.subList(0, 4));

        when(teacherRepository.findAll(PageRequest.of(0, 4))).thenReturn(mockedPage);

        List<Teacher> TeacherFromService = adminTeacherService.getTeachers(0, 4);

        assertThat(TeacherFromService).isNotNull();
        assertThat(TeacherFromService).hasSize(4);

        verify(teacherRepository, times(1)).findAll(PageRequest.of(0, 4));
    }

    @Test
    public void test_createTeacher_shouldCreateNewTeacherWhenEmailNotExistsInDatabase() throws Exception {
        Teacher newTeacher = new Teacher();
        newTeacher.setId("1233");
        newTeacher.setEmail("a@c.com");
        newTeacher.setName("An");
        newTeacher.setSurname("Bn");
        newTeacher.setPassword("pass");
        when(teacherRepository.findByEmail("a@c.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("passEncoded");
        when(teacherRepository.save(any(Teacher.class))).thenReturn(newTeacher);

        Teacher createdTeacher = adminTeacherService.createTeacher(newTeacher);

        assertThat(createdTeacher).isNotNull();
        assertThat(createdTeacher.getEmail()).isEqualTo("a@c.com");
        assertThat(createdTeacher.getPassword()).isEqualTo("passEncoded");

        verify(teacherRepository, times(1)).findByEmail("a@c.com");
        verify(passwordEncoder, times(1)).encode("pass");
        verify(teacherRepository, times(1)).save(any(Teacher.class));

    }
    @Test
    public void test_createTeacher_shouldThrowExceptionWhenEmailExistsInDatabase() throws Exception {
        Teacher mockedTeacher = new Teacher("123", "An", "Cz", "a@c.com", "pass");
        when(teacherRepository.findByEmail("a@c.com")).thenReturn(Optional.of(mockedTeacher));

        assertThatThrownBy(() -> adminTeacherService.createTeacher(mockedTeacher))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");

        verify(teacherRepository, times(1)).findByEmail("a@c.com");
        verify(teacherRepository, never()).save(any());
    }
    @Test
    public void test_editTeacher_shouldEditTeacherWhenExistsInDatabase() throws Exception {
        String teacherId = "123";
        Teacher existingTeacher = new Teacher();
        existingTeacher.setId(teacherId);
        existingTeacher.setEmail("a@c.com");
        existingTeacher.setName("An");
        existingTeacher.setSurname("Cz");
        existingTeacher.setPassword("pass");

        Teacher editedTeacher = new Teacher();
        editedTeacher.setEmail("new@a.com");
        editedTeacher.setName("NewName");
        editedTeacher.setSurname("NewSurname");
        editedTeacher.setPassword("newPassword");
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(existingTeacher));
        when(passwordEncoder.encode("newPassword")).thenReturn("enNewPass");

        Teacher edited = adminTeacherService.editTeacher(teacherId, editedTeacher);

        assertThat(edited).isNotNull();
        assertThat(edited.getName()).isEqualTo("NewName");
        assertThat(edited.getSurname()).isEqualTo("NewSurname");
        assertThat(edited.getEmail()).isEqualTo("new@a.com");
        assertThat(edited.getPassword()).isEqualTo("enNewPass");

        verify(teacherRepository).findById(teacherId);
        verify(teacherRepository).findByEmail("new@a.com");
        verify(passwordEncoder).encode("newPassword");
        verify(teacherRepository).save(existingTeacher);

    }

    @Test
    public void test_deleteTeacher_shouldDeleteTeacherWhenExistsInDatabase() throws Exception {
        String TeacherId = "123";
        Teacher mockedTeacher = new Teacher(TeacherId, "An", "Cz", "a@c.com", "pass");
        when(teacherRepository.findById(TeacherId)).thenReturn(Optional.of(mockedTeacher));

        Teacher deletedTeacher = adminTeacherService.deleteTeacher(TeacherId);

        // then
        assertThat(deletedTeacher).isNotNull();
        assertThat(deletedTeacher.getId()).isEqualTo("123");

        verify(teacherRepository).findById(TeacherId);
        verify(teacherRepository).delete(mockedTeacher);

    }

}
