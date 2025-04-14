package com.example.paneli.Repositories.UserPanel;

import com.example.paneli.DataObjects.Admin.UserProjection;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT email FROM User")
    List<String> findAllEmails();
    @Query("SELECT username FROM User")
    List<String> findAllUsernames();

    Page<User> findById(Long id, Pageable pageable);
    @Query("select u from User u where u.username = :username")
    public User getUserByUsername(@Param("username") String username);

    @Query("select u from User u where u.full_name = ?1 and u.username = ?2")
    public User findByFull_nameAndUsername(String fullname, String username);

    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    //query per te bere kerkimet per UsernameOrEmail
    User findByUsernameOrEmail(String username, String email);


    @Query("select u from User u where u.username = ?1")
    List<User> findAllByUsername(String username);


    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query("select u from User u where u.email = ?1")
    List<User> findAllByEmail(String email);


    @Query("select u from User u where u.id = ?1")
    Optional<User> findById(Long id);

    @Query("SELECT u.email FROM User u WHERE u.id = 1")
    String findAdminEmail();


    @Query("SELECT u.id AS id, u.username AS username, u.full_name AS full_name, u.email AS email, u.password_expired AS password_expired, u.account_locked AS account_locked FROM User u")
    List<UserProjection> findAllUserProjections();


    @Query("SELECT u.id as id, u.username as username, u.full_name as full_name, u.email as email, u.password_expired as password_expired, u.account_locked as account_locked FROM User u WHERE u.id = :id")
    List<UserProjection> findProjectionById(@Param("id") Long id);

    @Query("SELECT u.id as id, u.username as username, u.full_name as full_name, u.email as email, u.password_expired as password_expired, u.account_locked as account_locked FROM User u WHERE u.username LIKE %:username%")
    List<UserProjection> findProjectionByUsernameContaining(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM User u " +
            "JOIN u.role userRole " +
            "JOIN Role propertyRole ON propertyRole.id = :roleId " +
            "JOIN propertyRole.users propertyUser " +
            "WHERE userRole.id = 3 AND propertyRole IN :roles")
    boolean existsGroupAccountUser(@Param("roleId") Long roleId, @Param("roles") List<Role> roles);
}
