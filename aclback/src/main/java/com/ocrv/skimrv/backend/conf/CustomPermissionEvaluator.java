package com.ocrv.skimrv.backend.conf;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate;
import com.ocrv.skimrv.backend.domain.FormSkimIt;
import com.ocrv.skimrv.backend.service.ACLService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Collections;

@AllArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final ACLService aclService;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        // Получить orgUnitDictionary.id и dictRate.id из сущности
        FormSkimIt formSkimIt = (FormSkimIt) targetDomainObject;
        Integer orgUnitId = formSkimIt.getOrgUnitDictionary().getId();
        Integer dictRateId = formSkimIt.getDictRate().getId();

        // Проверить права на orgUnitDictionary
        boolean hasOrgUnitPermission = checkPermission(auth, OrgUnitDictionary.class, orgUnitId, permission);

        // Проверить права на dictRate
        boolean hasDictRatePermission = checkPermission(auth, DictRate.class, dictRateId, permission);

        // Если есть права хотя бы на одну из сущностей, то доступ разрешен
        return hasOrgUnitPermission || hasDictRatePermission;
    }

    private boolean checkPermission(Authentication auth, Class<?> clazz, Serializable id, Object permission) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(clazz, id);

        try {
            Acl acl = aclService.getMutableAclService().readAclById(objectIdentity);
            Sid sid = new PrincipalSid(auth.getName());
            Permission requiredPermission = ACLService.getPermissionMap().get(((String) permission).toUpperCase());

            // Проверка прав доступа
            return acl.isGranted(Collections.singletonList(requiredPermission), Collections.singletonList(sid), false);
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        // Не реализовано в данном примере
        return false;
    }
}