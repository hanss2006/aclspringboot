package com.ocrv.skimrv.backend.api;

import com.ocrv.skimrv.backend.domain.FormSkimIt;
import com.ocrv.skimrv.backend.domain.IEntity;
import com.ocrv.skimrv.backend.service.MyACLService;
import com.ocrv.skimrv.backend.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.Permission;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

/*    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Защита endpoint'а
    public ResponseEntity<Void> addPermissionForUser(
            @RequestParam String entityType,
            @RequestParam Integer entityId,
            @RequestParam String permission,
            @RequestParam String username) {

        // Создаем объект IEntity (вам нужно реализовать этот класс)
        IEntity targetObj = new IEntity();

        // Преобразуем строку permission в объект Permission
        Permission perm = MyACLService.getPermissionMap().get((permission).toUpperCase());

        permissionService.addPermissionForUser(targetObj, perm, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authority")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public ResponseEntity<Void> addPermissionForAuthority(
            @RequestParam String entityType,
            @RequestParam Integer entityId,
            @RequestParam String permission,
            @RequestParam String authority) {

        IEntity targetObj = new SimpleEntity(entityId, entityType);
        // Преобразуем строку permission в объект Permission
        Permission perm = MyACLService.getPermissionMap().get((permission).toUpperCase());

        permissionService.addPermissionForAuthority(targetObj, perm, authority);
        return ResponseEntity.ok().build();
    }*/
}