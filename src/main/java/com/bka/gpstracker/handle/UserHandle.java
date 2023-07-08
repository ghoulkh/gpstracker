package com.bka.gpstracker.handle;

import com.bka.gpstracker.repository.CarInfoRepository;
import com.bka.gpstracker.solr.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHandle {
    @Autowired
    private CarInfoRepository carInfoRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;


}
