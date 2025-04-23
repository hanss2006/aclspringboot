package com.ocrv.skimrv.backend.api;

import com.ocrv.skimrv.backend.service.MyACLService;
import com.ocrv.skimrv.backend.service.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.Permission;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Назначение прав", description = "Назначение прав")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')") // Защита endpoint'а
    public ResponseEntity<Void> addPermissionForUser(
            @RequestParam String entityType,
            @RequestParam Integer entityId,
            @RequestParam String permission,
            @RequestParam String username) {

        // Преобразуем строку permission в объект Permission
        Permission perm = MyACLService.getPermissionMap().get((permission).toUpperCase());

        permissionService.addPermissionForUser(entityType, entityId, perm, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authority")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addPermissionForAuthority(
            @RequestParam String entityType,
            @RequestParam Integer entityId,
            @RequestParam String permission,
            @RequestParam String authority) {

        // Преобразуем строку permission в объект Permission
        Permission perm = MyACLService.getPermissionMap().get((permission).toUpperCase());

        permissionService.addPermissionForAuthority(entityType, entityId, perm, authority);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sid")
    @PreAuthorize("hasRole('ADMIN')") // Защита endpoint'а
    public ResponseEntity<Long> createSid(@RequestParam String name, @RequestParam boolean principal) {
        Long id = permissionService.ensureSidExists(name, principal);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/class")
    @PreAuthorize("hasRole('ADMIN')") // Защита endpoint'а
    public ResponseEntity<Long> createClass(@RequestParam String className) {
        Long id = permissionService.ensureClassExists(className);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/identity")
    @PreAuthorize("hasRole('ADMIN')") // Защита endpoint'а
    public ResponseEntity<Long> createObjectIdentity(
            @RequestParam String className,
            @RequestParam Integer objectId,
            @RequestParam(required = false) String ownerSid,
            @RequestParam(defaultValue = "true") boolean principal,
            @RequestParam(required = false) Long parentId) {

        Long id = permissionService.createObjectIdentity(className, objectId, ownerSid, principal, parentId);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/identity/change-owner")
    @PreAuthorize("hasRole('ADMIN')") // Защита endpoint'а
    public ResponseEntity<Void> changeOwner(
            @RequestParam Long id,
            @RequestParam String newOwner,
            @RequestParam(defaultValue = "true") boolean principal) {

        permissionService.changeOwner(id, newOwner, principal);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/identity/change-parent")
    @PreAuthorize("hasRole('ADMIN')") // Защита endpoint'а
    public ResponseEntity<Void> changeParent(
            @RequestParam Long id,
            @RequestParam Long parentId) {

        permissionService.changeParent(id, parentId);
        return ResponseEntity.ok().build();
    }
}