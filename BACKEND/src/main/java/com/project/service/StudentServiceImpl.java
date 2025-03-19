package com.project.service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import com.project.repository.ProjektRepository;
import com.project.model.Projekt;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ProjektRepository projektRepository; // Dodano repozytorium projektów

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ProjektRepository projektRepository) {
        this.studentRepository = studentRepository;
        this.projektRepository = projektRepository;
    }

    @Override
    public Optional<Student> getStudent(String studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public Student setStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudent(String studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            
            // Usuń studenta z projektów, do których jest przypisany
            for (String projektId : student.getProjektyIds()) {
                Optional<Projekt> projektOpt = projektRepository.findById(projektId);
                if (projektOpt.isPresent()) {
                    Projekt projekt = projektOpt.get();
                    projekt.getStudenciIds().remove(studentId);
                    projektRepository.save(projekt);
                }
            }

            // Usuń studenta z bazy
            studentRepository.deleteById(studentId);
        }
    }

    @Override
    public Page<Student> getStudenci(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Optional<Student> findByNrIndeksu(String nrIndeksu) {
        return studentRepository.findByNrIndeksu(nrIndeksu);
    }
}
