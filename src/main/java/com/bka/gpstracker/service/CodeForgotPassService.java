package com.bka.gpstracker.service;

import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.solr.entity.CodeForgotPass;
import com.bka.gpstracker.solr.repository.CodeForgotPassRepository;
import com.bka.gpstracker.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeForgotPassService {

    @Autowired
    private CodeForgotPassRepository codeForgotPassRepository;

    public CodeForgotPass generateCode(String username) {
        Long currentTime = System.currentTimeMillis();
        Long expireTime = currentTime + 300000L;
        String code = Utils.getAlphaNumericString(10);
        CodeForgotPass codeForgotPass = new CodeForgotPass();
        codeForgotPass.setCode(code);
        codeForgotPass.setExpireDate(expireTime);
        codeForgotPass.setUsername(username);
        return codeForgotPassRepository.save(codeForgotPass);
    }

    public void validateCode(String username, String code) {
        CodeForgotPass codeForgotPass = codeForgotPassRepository.findById(username).orElseThrow(() ->
                new TrackerAppException(ErrorCode.CODE_INVALID));
        if (codeForgotPass.getExpireDate() < System.currentTimeMillis())
            throw new TrackerAppException(ErrorCode.CODE_IS_EXPIRE);
        if (!code.equals(codeForgotPass.getCode()))
            throw new TrackerAppException(ErrorCode.CODE_INVALID);
        codeForgotPassRepository.delete(codeForgotPass);
    }
}
