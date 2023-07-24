package com.bka.gpstracker.service;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.solr.entity.Authority;
import com.bka.gpstracker.solr.entity.UserInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.solr.repository.AuthorityRepository;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CarInfoService carInfoService;

    public CarInfo setCarAuthor(String username, String rfid) {
        UserInfo userToSet = userService.getByUsername(username);

        if (carInfoService.getAllByUsername(username).size() == 1) {
            throw new TrackerAppException(ErrorCode.ADD_CAR_AUTHOR_FAILED);
        }
        CarInfo carInfoToSet = carInfoService.getByRfid(rfid);
        carInfoToSet.setUsername(userToSet.getUsername());
        carInfoToSet.setDriver(userToSet.getFullName());
        setRoleDriver(username);
        return carInfoService.save(carInfoToSet);
    }

    private void setRoleDriver(String username) {
        List<String> authorities = authorityRepository.getAllByUsername(username).stream()
                .map(Authority::getRole)
                .map(Authority.Role::getValue)
                .collect(Collectors.toList());
        if (authorities.contains(AuthoritiesConstants.ROLE_DRIVER)) return;
        Authority authority = new Authority();
        authority.setRole(Authority.Role.ROLE_DRIVER);
        authority.setUsername(username);
        authorityRepository.save(authority);
    }
}
