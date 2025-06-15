package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.ItemDao;
import com.project.farmeasyportal.dao.MerchantDao;
import com.project.farmeasyportal.dao.UserDao;
import com.project.farmeasyportal.entities.Item;
import com.project.farmeasyportal.entities.Merchant;
import com.project.farmeasyportal.entities.User;
import com.project.farmeasyportal.enums.ItemCategory;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.ItemDTO;
import com.project.farmeasyportal.payloads.MerchantDTO;
import com.project.farmeasyportal.services.MerchantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images";

    private final UserDao userDao;
    private final MerchantDao merchantDao;
    private final ItemDao itemDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public MerchantDTO saveMerchant(MerchantDTO merchantDTO) {
        if (merchantDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(merchantDTO.getPassword());

        merchantDTO.setPassword(encodedPassword);
        merchantDTO.setId(String.valueOf(UUID.randomUUID()));
        Merchant merchant = this.modelMapper.map(merchantDTO, Merchant.class);

        User user = new User();
        user.setEmail(merchantDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(UsersConstants.ROLE_MERCHANT);
        this.userDao.save(user);
        Merchant save = this.merchantDao.save(merchant);
        return this.modelMapper.map(save, MerchantDTO.class);
    }

    @Override
    public MerchantDTO updateMerchant(String id, MerchantDTO merchantDTO) {
        Merchant merchant = this.merchantDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, id));

        merchant.setName(merchantDTO.getName());
        merchant.setPhone(merchantDTO.getPhone());
        merchant.setAddress(merchant.getAddress());
        Merchant update = this.merchantDao.save(merchant);

        return this.modelMapper.map(update, MerchantDTO.class);
    }

    @Override
    public MerchantDTO getMerchantByEmail(String email) {
        Merchant merchant = this.merchantDao.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.EMAIL, email));
        return this.modelMapper.map(merchant, MerchantDTO.class);
    }

    @Override
    public MerchantDTO getMerchantById(String id) {
        Merchant merchant = this.merchantDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, id));
        return this.modelMapper.map(merchant, MerchantDTO.class);
    }

    @Override
    public List<MerchantDTO> getAllMerchants() {
        List<Merchant> merchantList = this.merchantDao.findAll();
        return merchantList.stream().map(merchant -> this.modelMapper.map(merchant, MerchantDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteMerchant(String id) {
        Merchant merchant = this.merchantDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, id));
        User user = this.userDao.findByEmail(merchant.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.USER, UsersConstants.EMAIL, merchant.getEmail()));
        this.merchantDao.delete(merchant);
        this.userDao.delete(user);
    }

    @Override
    public Boolean isMerchantExistByEmail(String email) {
        return this.merchantDao.existsByEmail(email);
    }

    @Override
    public Boolean isMerchantExistById(String id) {
        return this.merchantDao.existsById(id);
    }

    public String saveImage(MultipartFile imageName, String imageOriginalFileName, String pdfName, String userId) throws IOException {
        String storedImageName = "";
        if (!imageName.isEmpty()) {
            String safeOriginalName = Paths.get(imageOriginalFileName).getFileName().toString();
            String extension = safeOriginalName.contains(".")
                    ? safeOriginalName.substring(safeOriginalName.lastIndexOf('.'))
                    : "";

            if (!imageName.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }

            String timestamp = String.valueOf(System.currentTimeMillis());
            storedImageName = pdfName + UsersConstants.DASH + userId + UsersConstants.DASH + timestamp + extension;

            Path directory = Paths.get(uploadDir);
            Files.createDirectories(directory);

            Path filePath = directory.resolve(storedImageName);
            try (InputStream inputStream = imageName.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            storedImageName = imageOriginalFileName;
        }

        return storedImageName;
    }

    @Override
    public ItemDTO addItem(ItemDTO itemDTO, MultipartFile imageName, String merchantId) throws IOException {
        Item item = this.modelMapper.map(itemDTO, Item.class);
        Merchant merchant = this.merchantDao.findById(merchantId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, merchantId));

        String imageOriginalFileName = imageName.getOriginalFilename();
        String saveImage = saveImage(imageName, imageOriginalFileName, UsersConstants.ITEM, merchantId);

        item.setMerchantId(merchantId);
        item.setCategory(ItemCategory.valueOf(itemDTO.getCategory().toUpperCase()));
        item.setImageName(saveImage);

        Item save = itemDao.save(item);

        ItemDTO map = this.modelMapper.map(save, ItemDTO.class);
        map.setMerchantDTO(this.modelMapper.map(merchant, MerchantDTO.class));
        map.setCategory(save.getCategory().name());

        return map;
    }

    @Override
    public ItemDTO updateItem(ItemDTO itemDTO, Integer itemId) {
        Item item = this.itemDao.findById(itemId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.ITEM, UsersConstants.ID, String.valueOf(itemId)));
        Merchant merchant = this.merchantDao.findById(item.getMerchantId()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, item.getMerchantId()));

        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());
        item.setTotalQuantity(itemDTO.getTotalQuantity());
        item.setPricePerDay(itemDTO.getPricePerDay());
        item.setCategory(ItemCategory.valueOf(itemDTO.getCategory().toUpperCase()));

        Item update = itemDao.save(item);

        ItemDTO map = this.modelMapper.map(update, ItemDTO.class);
        map.setMerchantDTO(this.modelMapper.map(merchant, MerchantDTO.class));
        map.setCategory(update.getCategory().name());

        return map;
    }

    @Override
    public ItemDTO getItemById(Integer id) {
        Item item = this.itemDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.ITEM, UsersConstants.ID, String.valueOf(id)));
        Merchant merchant = this.merchantDao.findById(item.getMerchantId()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, item.getMerchantId()));
        ItemDTO itemDTO = this.modelMapper.map(item, ItemDTO.class);
        itemDTO.setCategory(item.getCategory().name());
        itemDTO.setMerchantDTO(this.modelMapper.map(merchant, MerchantDTO.class));
        return itemDTO;
    }

    @Override
    public List<ItemDTO> getItemsByMerchant(String username) {
        Merchant merchant = this.merchantDao.findByEmail(username).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.EMAIL, username));
        return this.itemDao.findAllByMerchantId(merchant.getId()).stream().map(item -> {
            ItemDTO itemDTO = this.modelMapper.map(item, ItemDTO.class);
            itemDTO.setCategory(item.getCategory().name());
            itemDTO.setMerchantDTO(this.modelMapper.map(merchant, MerchantDTO.class));
            return itemDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> getItems() {
        return this.itemDao.findAll().stream().map(item -> {
            ItemDTO itemDTO = this.modelMapper.map(item, ItemDTO.class);
            Merchant merchant = this.merchantDao.findById(item.getMerchantId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, item.getMerchantId()));
            itemDTO.setCategory(item.getCategory().name());
            itemDTO.setMerchantDTO(this.modelMapper.map(merchant, MerchantDTO.class));
            return itemDTO;
        }).collect(Collectors.toList());
    }

}
