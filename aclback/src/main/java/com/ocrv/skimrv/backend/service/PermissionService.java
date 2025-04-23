package com.ocrv.skimrv.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;

import static java.lang.Class.forName;

@Service
@Transactional
@Slf4j
public class PermissionService {
    @Autowired
    private MutableAclService aclService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    public void addPermissionForUser(String targetObjClassName, Integer id, Permission permission, String username) {
        final Sid sid = new PrincipalSid(username);
        addPermissionForSid(targetObjClassName, id, permission, sid);
    }
    public void addPermissionForAuthority(String targetObjClassName, Integer id, Permission permission, String authority) {
        final Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(targetObjClassName, id, permission, sid);
    }
    private void addPermissionForSid(String targetObjClassName, Integer id, Permission permission, Sid sid) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final ObjectIdentity oi;
                try {
                    oi = new ObjectIdentityImpl(forName(targetObjClassName), id);
                } catch (ClassNotFoundException e) {
                    log.debug("Class does not found {}", targetObjClassName);
                    throw new RuntimeException(e);
                }
                MutableAcl acl;
                try {
                    acl = (MutableAcl) aclService.readAclById(oi);
                } catch (final NotFoundException nfe) {
                    acl = aclService.createAcl(oi);
                }
                acl.insertAce(acl.getEntries()
                        .size(), permission, sid, true);
                aclService.updateAcl(acl);
            }
        });
    }
}
