package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.Enterprise;
import com.bka.gpstracker.model.request.RegisterEnterpriseRequest;
import com.bka.gpstracker.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api")
public class EnterpriseController {
    @Autowired
    private EnterpriseService enterpriseService;

    @PostMapping("/enterprise")
    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    public ResponseEntity<Enterprise> registerEnterprise(@RequestBody @Valid RegisterEnterpriseRequest request) {
        return ResponseEntity.ok(enterpriseService.registerEnterprise(request));
    }

    @GetMapping("/enterprises")
    public ResponseEntity<List<Enterprise>> getAllEnterprise(@RequestParam(name = "page_index", required = false, defaultValue = "1") int pageIndex,
                                                             @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(enterpriseService.getAll(pageIndex, pageSize));
    }

    @GetMapping("/enterprise/{enterpriseCode}")
    public ResponseEntity<Enterprise> getByEnterpriseCode(@PathVariable String enterpriseCode) {
        return ResponseEntity.ok(enterpriseService.getByEnterpriseCode(enterpriseCode));
    }
}
