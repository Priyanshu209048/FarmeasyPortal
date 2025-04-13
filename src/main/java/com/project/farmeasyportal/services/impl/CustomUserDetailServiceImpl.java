package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.config.CustomUserDetails;
import com.project.farmeasyportal.dao.UserDao;
import com.project.farmeasyportal.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userDao.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Farmer not found with username: " + username));
        if (user == null)
            throw new UsernameNotFoundException("Farmer not found with username: " + username);
        return new CustomUserDetails(user);
    }
}
