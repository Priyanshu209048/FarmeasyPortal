package com.project.farmeasyportal.services;

public interface UserService {

    Boolean isUserExistById(Integer id);
    Boolean isUserExistByEmail(String email);

}
