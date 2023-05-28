package com.bka.gpstracker.service;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.Authority;
import com.bka.gpstracker.entity.UserInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.repository.AuthorityRepository;
import com.bka.gpstracker.repository.UserInfoRepository;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public List<UserResponse> getUserAndPaging(int pageIndex, int pageSize) {
        Pageable paging = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<UserInfo> userPage = userInfoRepository.findAll(paging);
        return userPage.getContent().stream().map(UserResponse::from).collect(Collectors.toList());
    }

    public UserResponse addAuthorityWithUsername(String username, Authority.Role role) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername.equals(username)) throw new TrackerAppException(ErrorCode.BAD_REQUEST);
        UserInfo userToAdd = userInfoRepository.findById(username).orElseThrow(() ->
                new TrackerAppException(ErrorCode.USER_NOT_FOUND));
        if (role.equals(Authority.Role.ROLE_ENTERPRISE_ADMIN) && userToAdd.getEnterpriseCode() == null)
            throw new TrackerAppException(ErrorCode.ROLE_INVALID);

        if (SecurityUtil.isAuthor(AuthoritiesConstants.ROLE_ENTERPRISE_ADMIN) && !SecurityUtil.isAuthor(AuthoritiesConstants.ROLE_ADMIN)) {
            if (role.equals(Authority.Role.ROLE_ADMIN)) throw new TrackerAppException(ErrorCode.ADD_ROLE_ADMIN_PERMISSION_DENIED);

            UserInfo currentUserInfo = userInfoRepository.getById(currentUsername);
            if (!currentUserInfo.getEnterpriseCode().equals(userToAdd.getEnterpriseCode()))
                throw new TrackerAppException(ErrorCode.ADD_ROLE_PERMISSION_DENIED);
        }

        validateCanAddRole(userToAdd, role);
        Authority authority = new Authority();
        authority.setRole(role);
        authority.setUserInfo(userToAdd);
        Authority authorityResult = authorityRepository.save(authority);

        userToAdd.getAuthorities().add(authorityResult);
        return UserResponse.from(userToAdd);
    }

    public void registerRoleAdmin(String username) {
        UserInfo userToAdd = userInfoRepository.findById(username).orElseThrow(() ->
                new TrackerAppException(ErrorCode.USER_NOT_FOUND));

        Authority authority = new Authority();
        authority.setRole(Authority.Role.ROLE_ADMIN);
        authority.setUserInfo(userToAdd);
        authorityRepository.save(authority);
    }

    private void validateCanAddRole(UserInfo userToAdd, Authority.Role roleToAdd) {
        List<Authority> authorities = authorityRepository.getAllByUserInfo(userToAdd);
        for (Authority authority : authorities) {
            if (authority.getRole().equals(roleToAdd))
                throw new TrackerAppException(ErrorCode.ROLE_IS_EXIST);
        }
    }

}
