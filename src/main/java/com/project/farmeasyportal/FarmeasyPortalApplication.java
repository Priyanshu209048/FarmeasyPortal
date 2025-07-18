package com.project.farmeasyportal;

import com.project.farmeasyportal.dao.BankDao;
import com.project.farmeasyportal.dao.UserDao;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class FarmeasyPortalApplication implements CommandLineRunner {

    @Autowired
    private BankDao bankDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    public static void main(String[] args) {
        SpringApplication.run(FarmeasyPortalApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        addStaticBanks();
        addGov();
    }

    public void addStaticBanks() {
        if (bankDao.count() == 0) {
            Bank bank1 = new Bank();
            bank1.setId(UUID.randomUUID().toString());
            bank1.setBankName("State Bank of India");
            bank1.setBankAddress("Connaught Place");
            bank1.setBankCity("Delhi");
            bank1.setBankState("Delhi");
            bank1.setBankZip("110001");
            bank1.setEmail("sbi@bank.com");
            bank1.setBankPhone("9876543210");

            Bank bank2 = new Bank();
            bank2.setId(UUID.randomUUID().toString());
            bank2.setBankName("Bank of Baroda");
            bank2.setBankAddress("Khajuri Khas");
            bank2.setBankCity("Delhi");
            bank2.setBankState("Delhi");
            bank2.setBankZip("110095");
            bank2.setEmail("bob@bank.com");
            bank2.setBankPhone("6359874589");

            bankDao.saveAll(List.of(bank1, bank2));
            List<Bank> savedBanks = bankDao.findAll();
            System.out.println("Banks found: " + savedBanks.size());

            for (Bank bank : savedBanks) {
                User bankUser = new User();
                bankUser.setEmail(bank.getEmail());
                bankUser.setPassword(passwordEncoder.encode("12345"));
                bankUser.setRole("ROLE_BANK");

                System.out.println("Saving user: " + bankUser.getEmail());
                userDao.save(bankUser);
            }
        }
    }

    public void addGov() {
        User govUser = new User();
        govUser.setEmail("central@gov.com");
        govUser.setPassword(passwordEncoder.encode("12345"));
        govUser.setRole("ROLE_GOV");
        userDao.save(govUser);
    }
}
