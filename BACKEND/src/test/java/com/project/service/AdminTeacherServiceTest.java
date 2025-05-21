package com.project.service;

import com.project.model.Teacher;
import com.project.repository.TeacherRepository;
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
public class AdminTeacherServiceTest {

    private @Mock TeacherRepository TeacherRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @InjectMocks AdminTeacherService adminTeacherService;

    @Test
    public void test_getTeacherById_shouldReturnTeacherWhenExistsInDatabase() throws Exception {
        Teacher mockedTeacher = new Teacher("123", "An", "Cz", "a@c.com", "pass");
        when(TeacherRepository.findById(Mockito.any())).thenReturn(Optional.of(mockedTeacher));
        Teacher TeacherFromService = adminTeacherService.getTeacherById("123");

        assertThat(TeacherFromService).isNotNull();
        assertThat(TeacherFromService.getId()).isEqualTo("123");
        assertThat(TeacherFromService.getName()).isEqualTo("An");
        assertThat(TeacherFromService.getEmail()).isEqualTo("a@c.com");
        assertThat(TeacherFromService.getSurname()).isEqualTo("Cz");

        verify(TeacherRepository, times(1)).findById(Mockito.any());

    }

    @Test
    public void test_getTeacherList_shouldReturnTeacherListWhenExistsInDatabase() throws Exception {
        List<Teacher> mockedTeachers = new ArrayList<>();
        mockedTeachers.add(new Teacher("123", "An", "Cz", "a@c.com", "pass"));
        mockedTeachers.add(new Teacher("133", "Bn", "Dz", "b@d.com", "pass1"));
        mockedTeachers.add(new Teacher("143", "Cn", "Ez", "c@e.com", "pass2"));
        mockedTeachers.add(new Teacher("153", "Dn", "Fz", "d@f.com", "pass3"));
        mockedTeachers.add(new Teacher("163", "En", "Gz", "e@g.com", "pass4"));

        Page<Teacher> mockedPage = new PageImpl<>(mockedTeachers.subList(0, 4));

        when(TeacherRepository.findAll(PageRequest.of(0, 4))).thenReturn(mockedPage);

        List<Teacher> TeacherFromService = adminTeacherService.getTeachers(0, 4);

        assertThat(TeacherFromService).isNotNull();
        assertThat(TeacherFromService).hasSize(4);

        verify(TeacherRepository, times(1)).findAll(PageRequest.of(0, 4));
    }

    @Test
    public void test_createTeacher_shouldCreateNewTeacherWhenEmailNotExistsInDatabase() throws Exception {
        Teacher newTeacher = new Teacher();
        newTeacher.setId("1233");
        newTeacher.setEmail("a@c.com");
        newTeacher.setName("An");
        newTeacher.setSurname("Bn");
        newTeacher.setPassword("pass");
        when(TeacherRepository.findByEmail("a@c.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("passEncoded");
        when(TeacherRepository.save(any(Teacher.class))).thenReturn(newTeacher);

        Teacher creaatedTeacher = adminTeacherService.createTeacher(newTeacher);

        assertThat(creaatedTeacher).isNotNull();
        assertThat(creaatedTeacher.getEmail()).isEqualTo("a@c.com");
        assertThat(creaatedTeacher.getPassword()).isEqualTo("passEncoded");

        verify(TeacherRepository, times(1)).findByEmail("a@c.com");
        verify(passwordEncoder, times(1)).encode("pass");
        verify(TeacherRepository, times(1)).save(any(Teacher.class));

    }

    @Test
    public void test_editTeacher_shouldEditTeacherWhenExistsInDatabase() throws Exception {
        String TeacherId = "123";
        Teacher existingTeacher = new Teacher();
        existingTeacher.setId(TeacherId);
        existingTeacher.setEmail("a@c.com");
        existingTeacher.setName("An");
        existingTeacher.setSurname("Cz");
        existingTeacher.setPassword("pass");

        Teacher editedTeacher = new Teacher();
        editedTeacher.setEmail("new@a.com");
        editedTeacher.setName("NewName");
        editedTeacher.setSurname("NewSurname");
        editedTeacher.setPassword("newPassword");
        when(TeacherRepository.findById(TeacherId)).thenReturn(Optional.of(existingTeacher));
        when(passwordEncoder.encode("newPassword")).thenReturn("enNewPass");
        //when(TeacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Teacher edited = adminTeacherService.editTeacher(TeacherId, editedTeacher);

        assertThat(edited).isNotNull();
        assertThat(edited.getName()).isEqualTo("NewName");
        assertThat(edited.getSurname()).isEqualTo("NewSurname");
        assertThat(edited.getEmail()).isEqualTo("new@a.com");
        assertThat(edited.getPassword()).isEqualTo("enNewPass");

        verify(TeacherRepository).findById(TeacherId);
        verify(TeacherRepository).findByEmail("new@a.com");
        verify(passwordEncoder).encode("newPassword");
        verify(TeacherRepository).save(existingTeacher);

    }

    @Test
    public void test_deleteTeacher_shouldDeleteTeacherWhenExistsInDatabase() throws Exception {
        String TeacherId = "123";
        Teacher mockedTeacher = new Teacher(TeacherId, "An", "Cz", "a@c.com", "pass");
        when(TeacherRepository.findById(TeacherId)).thenReturn(Optional.of(mockedTeacher));

        Teacher deletedTeacher = adminTeacherService.deleteTeacher(TeacherId);

        // then
        assertThat(deletedTeacher).isNotNull();
        assertThat(deletedTeacher.getId()).isEqualTo("123");

        verify(TeacherRepository).findById(TeacherId);
        verify(TeacherRepository).delete(mockedTeacher);

    }

    @Test
    public void test_getTeacherById_shouldThrowExceptionWhenNotExistsInDatabase() throws Exception {
        when(TeacherRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            adminTeacherService.getTeacherById("12334");
        }).isInstanceOf(NoSuchElementException.class);

        verify(TeacherRepository, times(1)).findById(Mockito.any());
    }

    @Test
    public void test_createTeacher_shouldThrowExceptionWhenEmailExistsInDatabase() throws Exception {
        Teacher mockedTeacher = new Teacher("123", "An", "Cz", "a@c.com", "pass");
        when(TeacherRepository.findByEmail("a@c.com")).thenReturn(Optional.of(mockedTeacher));

        assertThatThrownBy(() -> adminTeacherService.createTeacher(mockedTeacher))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");

        verify(TeacherRepository, times(1)).findByEmail("a@c.com");
        verify(TeacherRepository, never()).save(any());
    }

}
