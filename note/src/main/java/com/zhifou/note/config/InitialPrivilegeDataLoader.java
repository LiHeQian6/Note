package com.zhifou.note.config;

import com.zhifou.note.user.entity.Privilege;
import com.zhifou.note.user.entity.Role;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.repository.PrivilegeRepository;
import com.zhifou.note.user.repository.RoleRepository;
import com.zhifou.note.user.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Component
public class InitialPrivilegeDataLoader implements ApplicationListener<ContextRefreshedEvent> {
 
    boolean alreadySetup = false;
 
    @Resource
    private UserRepository userRepository;
  
    @Resource
    private RoleRepository roleRepository;
  
    @Resource
    private PrivilegeRepository privilegeRepository;

    @Resource
    private PasswordEncoder passwordEncoder;
  
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
  
        if (alreadySetup)
            return;
        //初始化权限
        Privilege readPrivilege
          = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
          = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
  
        List<Privilege> adminPrivileges = Arrays.asList(
          readPrivilege, writePrivilege);

        if (userRepository.findUserByUsername("SYSTEM@zhifou.com")==null) {
            //初始化角色
            Role admin = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
            createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(admin.getName()));
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(admin);
            User user = new User("SYSTEM@zhifou.com",passwordEncoder.encode("admin123."),roles,authorities);
            userRepository.save(user);
        }

        alreadySetup = true;
    }
 
    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
  
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
 
    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {
  
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name,privileges);
            roleRepository.save(role);
        }
        return role;
    }
}