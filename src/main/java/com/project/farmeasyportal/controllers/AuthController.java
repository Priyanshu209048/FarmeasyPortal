package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.components.JwtTokenHelper;
import com.project.farmeasyportal.entities.User;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import com.project.farmeasyportal.dao.UserDao;
import com.project.farmeasyportal.services.BankService;
import com.project.farmeasyportal.services.FarmerService;
import com.project.farmeasyportal.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserDao userDao;
    private final FarmerService farmerService;
    private final BankService bankService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request){
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenHelper.generateToken(userDetails);

        User user = this.userDao.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setUser(this.modelMapper.map(user, UserDTO.class));
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            this.authenticationManager.authenticate(authenticationToken);
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled");
        }
    }

    @PostMapping("/register/farmer")
    public ResponseEntity<?> registerFarmer(@RequestBody @Valid FarmerDTO farmerDTO) {
        if (userService.isUserExistByEmail(farmerDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("User already exists"), HttpStatus.CONFLICT);
        }

        FarmerDTO farmerRegistered = farmerService.saveFarmer(farmerDTO);
        return new ResponseEntity<>(farmerRegistered, HttpStatus.CREATED);
    }

    @PostMapping("/register/bank")
    public ResponseEntity<?> registerBank(@RequestBody @Valid BankDTO bankDTO) {
        if (this.bankService.isBankExistByEmail(bankDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("Bank already exists !!"), HttpStatus.CONFLICT);
        }

        BankDTO bank = this.bankService.addBank(bankDTO);
        return new ResponseEntity<>(new ApiResponse("Bank registered successfully"), HttpStatus.CREATED);
    }

    /*@PostMapping("/register/government")
    public ResponseEntity<?> registerGov(@RequestBody @Valid GovernmentDTO governmentDTO) {
        if (userService.isUserExistByEmail(governmentDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("User already exists"), HttpStatus.CONFLICT);
        }

        governmentService.createGovDetails(user, governmentDTO);
        return new ResponseEntity<>(new ApiResponse("Government officer registered successfully"), HttpStatus.CREATED);
    }*/


    /*@PostMapping("/register")
    public ResponseEntity<?> createUserWithRole(@RequestBody @Valid UserDTO userDTO, String role) {
        if (this.userService.isUserExistByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("User already exists"), HttpStatus.CONFLICT);
        }

        userDTO.setRole(role);

        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }*/

}
