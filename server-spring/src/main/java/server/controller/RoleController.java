package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import server.dto.RoleDTO;
import server.model.Role;
import server.service.RoleService;

@Controller
@RequestMapping("/api/role")

public class RoleController extends BaseController<Role, RoleDTO, Long> {

    @Autowired
    private RoleService RoleService;

    @Override
    protected RoleService getService() {
        return RoleService;
    }

}