package com.ocrv.skimrv.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class to handle ACL permissions.
 */
@Service
@Slf4j
public class ACLService {
    /**
     * Чтение.
     */
    public static final int READ = 1;
    /**
     * Запись.
     */
    public static final int WRITE = 2;
    /**
     * Создание.
     */
    public static final int CREATE = 4;
    /**
     * Удаление.
     */
    public static final int DELETE = 8;
    /**
     * Администрирование.
     */
    public static final int ADMINISTRATION = 16;
    /**
     * Чтение.
     */
    public static final String READSTR = "READ";
    /**
     * Запись.
     */
    public static final String WRITESTR = "WRITE";

    public MutableAclService getMutableAclService() {
        return mutableAclService;
    }

    public SidRetrievalStrategy getSidRetrievalStrategy() {
        return sidRetrievalStrategy;
    }

    /**
     * Mutable Acl Service.
     */
    private final MutableAclService mutableAclService;

    /**
     * Стратегия аутентификации.
     */
    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

    /**
     * Конструктор.
     *
     * @param mutableAclServicePar
     */
    @Autowired
    public ACLService(final MutableAclService mutableAclServicePar) {
        this.mutableAclService = mutableAclServicePar;
    }

    /**
     * Разрешения.
     */
    private static Map<String, Permission> permissionMap;

    static {
        setPermissionMap(new HashMap<>());
        getPermissionMap().put("READ", BasePermission.READ);
        getPermissionMap().put("WRITE", BasePermission.WRITE);
        getPermissionMap().put("CREATE", BasePermission.CREATE);
        getPermissionMap().put("DELETE", BasePermission.DELETE);
        getPermissionMap().put("ADMINISTRATION", BasePermission.ADMINISTRATION);
    }

    /**
     * Маска в строку.
     *
     * @param x
     * @return String
     */
    public static String fromMask(final int x) {
        switch (x) {
            case READ:
                return "READ";
            case WRITE:
                return "WRITE";
            case CREATE:
                return "CREATE";
            case DELETE:
                return "DELETE";
            case ADMINISTRATION:
                return "ADMINISTRATION";
            default:
        }
        return null;
    }

    /**
     * Чтение разрешения.
     *
     * @return Map<String, Permission>
     */
    public static Map<String, Permission> getPermissionMap() {
        return permissionMap;
    }

    /**
     * Установка разрешения.
     *
     * @param permissionMapPar
     */
    public static void setPermissionMap(final Map<String, Permission> permissionMapPar) {
        ACLService.permissionMap = permissionMapPar;
    }

    /**
     * Insert an ACL entry.
     *
     * @param className  represents the model object
     * @param role       represents the principal (user, group of users, etc)
     * @param permission quite explicit name...
     * @param granting
     */
    public void updatePermission(
            final String className,
            final String role,
            final String permission,
            final Boolean granting
    ) {
        log.debug("updatePermission className = " + className + " role = " + role + " permission = " + permission);
        MutableAcl acl;
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(className, 1);
        Sid sid = new GrantedAuthoritySid(role);

        try {
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(objectIdentity);
        }

        deleteAceIfExists(sid, acl, ACLService.getPermissionMap().get(permission));
        acl.insertAce(acl.getEntries().size(), ACLService.getPermissionMap().get(permission), sid, granting);

        log.debug(mutableAclService.updateAcl(acl).toString());
    }

    /**
     * Удалить разрешение.
     *
     * @param sid
     * @param acl
     * @param permission
     * @return boolean
     */
    private boolean deleteAceIfExists(final Sid sid, final MutableAcl acl, final Permission permission) {
        boolean aclUpdated = false;
        int nrEntries = acl.getEntries().size();
        for (int i = nrEntries - 1; i >= 0; i--) {
            AccessControlEntry accessControlEntry = acl.getEntries().get(i);
            if (accessControlEntry.getSid().equals(sid) && accessControlEntry.getPermission() == permission) {
                acl.deleteAce(i);
                aclUpdated = true;
            }
        }
        return aclUpdated;
    }
}
