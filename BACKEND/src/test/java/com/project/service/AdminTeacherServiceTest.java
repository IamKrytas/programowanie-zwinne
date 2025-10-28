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
        Teacher mockedTeacher = new Teacher();
        mockedTeacher.setId(123L);
        mockedTeacher.setName("An");
        mockedTeacher.setSurname("Cz");
        mockedTeacher.setEmail("a@c.com");
        mockedTeacher.setPassword("pass");
        
        when(teacherRepository.findById(123L)).thenReturn(Optional.of(mockedTeacher));
        Teacher TeacherFromService = adminTeacherService.getTeacherById("123");

        assertThat(TeacherFromService).isNotNull();
        assertThat(TeacherFromService.getId()).isEqualTo(123L);
        assertThat(TeacherFromService.getName()).isEqualTo("An");
        assertThat(TeacherFromService.getEmail()).isEqualTo("a@c.com");
        assertThat(TeacherFromService.getSurname()).isEqualTo("Cz");

        verify(teacherRepository, times(1)).findById(123L);

    }
    @Test
    public void test_getTeacherById_shouldThrowExceptionWhenNotExistsInDatabase() throws Exception {
        when(teacherRepository.findById(12334L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            adminTeacherService.getTeacherById("12334");
        }).isInstanceOf(NoSuchElementException.class);

        verify(teacherRepository, times(1)).findById(12334L);
    }
    @Test
    public void test_getTeachers_shouldReturnTeacherListWhenExistsInDatabase() throws Exception {
        List<Teacher> mockedTeachers = new ArrayList<>();
        
        Teacher teacher1 = new Teacher();
        teacher1.setId(123L);
        teacher1.setName("An");
        teacher1.setSurname("Cz");
        teacher1.setEmail("a@c.com");
        teacher1.setPassword("pass");
        mockedTeachers.add(teacher1);
        
        Teacher teacher2 = new Teacher();
        teacher2.setId(133L);
        teacher2.setName("Bn");
        teacher2.setSurname("Dz");
        teacher2.setEmail("b@d.com");
        teacher2.setPassword("pass1");
        mockedTeachers.add(teacher2);
        
        Teacher teacher3 = new Teacher();
        teacher3.setId(143L);
        teacher3.setName("Cn");
        teacher3.setSurname("Ez");
        teacher3.setEmail("c@e.com");
        teacher3.setPassword("pass2");
        mockedTeachers.add(teacher3);
        
        Teacher teacher4 = new Teacher();
        teacher4.setId(153L);
        teacher4.setName("Dn");
        teacher4.setSurname("Fz");
        teacher4.setEmail("d@f.com");
        teacher4.setPassword("pass3");
        mockedTeachers.add(teacher4);
        
        Teacher teacher5 = new Teacher();
        teacher5.setId(163L);
        teacher5.setName("En");
        teacher5.setSurname("Gz");
        teacher5.setEmail("e@g.com");
        teacher5.setPassword("pass4");
        mockedTeachers.add(teacher5);

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
        newTeacher.setId(1233L);
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
        Teacher mockedTeacher = new Teacher();
        mockedTeacher.setId(123L);
        mockedTeacher.setName("An");
        mockedTeacher.setSurname("Cz");
        mockedTeacher.setEmail("a@c.com");
        mockedTeacher.setPassword("pass");
        
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
        existingTeacher.setId(123L);
        existingTeacher.setEmail("a@c.com");
        existingTeacher.setName("An");
        existingTeacher.setSurname("Cz");
        existingTeacher.setPassword("pass");

        Teacher editedTeacher = new Teacher();
        editedTeacher.setEmail("new@a.com");
        editedTeacher.setName("NewName");
        editedTeacher.setSurname("NewSurname");
        editedTeacher.setPassword("newPassword");
        when(teacherRepository.findById(123L)).thenReturn(Optional.of(existingTeacher));
        when(passwordEncoder.encode("newPassword")).thenReturn("enNewPass");

        Teacher edited = adminTeacherService.editTeacher(teacherId, editedTeacher);

        assertThat(edited).isNotNull();
        assertThat(edited.getName()).isEqualTo("NewName");
        assertThat(edited.getSurname()).isEqualTo("NewSurname");
        assertThat(edited.getEmail()).isEqualTo("new@a.com");
        assertThat(edited.getPassword()).isEqualTo("enNewPass");

        verify(teacherRepository).findById(123L);
        verify(teacherRepository).findByEmail("new@a.com");
        verify(passwordEncoder).encode("newPassword");
        verify(teacherRepository).save(existingTeacher);

    }

    @Test
    public void test_deleteTeacher_shouldDeleteTeacherWhenExistsInDatabase() throws Exception {
        String TeacherId = "123";
        Teacher mockedTeacher = new Teacher();
        mockedTeacher.setId(123L);
        mockedTeacher.setName("An");
        mockedTeacher.setSurname("Cz");
        mockedTeacher.setEmail("a@c.com");
        mockedTeacher.setPassword("pass");
        
        when(teacherRepository.findById(123L)).thenReturn(Optional.of(mockedTeacher));

        Teacher deletedTeacher = adminTeacherService.deleteTeacher(TeacherId);

        // then
        assertThat(deletedTeacher).isNotNull();
        assertThat(deletedTeacher.getId()).isEqualTo(123L);

        verify(teacherRepository).findById(123L);
        verify(teacherRepository).delete(mockedTeacher);

    }

}
