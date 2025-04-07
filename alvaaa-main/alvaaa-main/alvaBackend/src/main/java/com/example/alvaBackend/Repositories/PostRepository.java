package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Post;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    void deleteById(Integer id);

    @Query("SELECT p FROM Post p WHERE p.publication_date <= :date AND p.status = 'archived' ")
    List<Post> findPostsToArchive(Date date);

    // nbadlo etat
    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.status = 'archived' WHERE p.id = :id")
    void archivePostById(Integer id);

    @Query("SELECT p FROM Post p WHERE p.status != 'archived' ORDER BY p.publication_date DESC")
    List<Post> findAllNonArchivedPosts();

    @Query("SELECT p FROM Post p WHERE p.user.department.department_id = :departmentId AND p.status <> 'archived' ORDER BY p.publication_date DESC")
    List<Post> findPostsByDepartmentId(int departmentId);


    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.publication_date DESC")
    List<Post> findPostsByUserId(int userId);


    @Query("SELECT p FROM Post p WHERE p.user.email = :email")
    List<Post> findByEmailUser(@Param("email") String email);}
