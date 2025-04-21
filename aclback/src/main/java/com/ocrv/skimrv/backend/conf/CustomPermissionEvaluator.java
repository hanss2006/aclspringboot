package com.ocrv.skimrv.backend.conf;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate;
import com.ocrv.skimrv.backend.domain.FormSkimIt;
import com.ocrv.skimrv.backend.service.MyACLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {
    public CustomPermissionEvaluator(JdbcMutableAclService aclService) {
        myAclService = new MyACLService(aclService);
    }

    private final MyACLService myAclService;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        if (!(targetDomainObject instanceof Optional)) {
            return false;
        }
        Optional<FormSkimIt> optionalFormSkimIt = (Optional<FormSkimIt>) targetDomainObject;

        if (optionalFormSkimIt.isEmpty()) {
            return false;
        }

        FormSkimIt formSkimIt = optionalFormSkimIt.get();

        Integer orgUnitId = formSkimIt.getOrgUnitDictionary().getId();
        Integer dictRateId = formSkimIt.getDictRate().getId();

        // Проверяем права на FormSkimIt
        //boolean hasFormSkimItPermission = checkPermission(auth, FormSkimIt.class, formSkimIt.getId(), permission);
        // Проверить права на orgUnitDictionary
        boolean hasOrgUnitPermission = checkPermission(auth, OrgUnitDictionary.class, orgUnitId, permission);

        // Проверить права на dictRate
        boolean hasDictRatePermission = checkPermission(auth, DictRate.class, dictRateId, permission);

        // Если есть права хотя бы на одну из сущностей, то доступ разрешен
        return hasOrgUnitPermission && hasDictRatePermission;
    }

    private boolean checkPermission(Authentication auth, Class<?> clazz, Serializable id, Object permission) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(clazz, id);

        try {
            Acl acl = myAclService.getMutableAclService().readAclById(objectIdentity);
            // Получаем все роли пользователя
            List<Sid> sids = new ArrayList<>();
            for (GrantedAuthority authority : auth.getAuthorities()) {
                sids.add(new GrantedAuthoritySid(authority.getAuthority()));
            }

            // Добавляем PrincipalSid для пользователя
            sids.add(new PrincipalSid(auth.getName()));

            Permission requiredPermission = MyACLService.getPermissionMap().get(((String) permission).toUpperCase());

            // Проверка прав доступа
            return acl.isGranted(Collections.singletonList(requiredPermission), sids, false);
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication auth,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        if (auth == null || targetId == null || targetType == null || !(permission instanceof String)) {
            return false;
        }

        try {
            // Получаем класс сущности по имени
            Class<?> targetClass = Class.forName(targetType);

            // Проверяем, что класс является одной из наших сущностей
            if (!FormSkimIt.class.isAssignableFrom(targetClass) &&
                    !OrgUnitDictionary.class.isAssignableFrom(targetClass) &&
                    !DictRate.class.isAssignableFrom(targetClass)) {
                return false;
            }

            // Создаем ObjectIdentity для проверки прав
            ObjectIdentity objectIdentity = new ObjectIdentityImpl(targetType, targetId);

            // Получаем ACL для этой сущности
            Acl acl = myAclService.getMutableAclService().readAclById(objectIdentity);

            // Получаем все роли пользователя
            List<Sid> sids = new ArrayList<>();
            for (GrantedAuthority authority : auth.getAuthorities()) {
                sids.add(new GrantedAuthoritySid(authority.getAuthority()));
            }

            // Добавляем PrincipalSid для пользователя
            sids.add(new PrincipalSid(auth.getName()));

            // Получаем требуемое право из мапы
            Permission requiredPermission = MyACLService.getPermissionMap()
                    .get(((String) permission).toUpperCase());

            // Проверяем права
            return acl.isGranted(Collections.singletonList(requiredPermission),
                    sids,
                    false);

        } catch (ClassNotFoundException e) {
            log.error("Class not found: {}", targetType, e);
            return false;
        } catch (NotFoundException e) {
            log.debug("ACL not found for {} with id {}", targetType, targetId);
            return false;
        } catch (Exception e) {
            log.error("Error checking permission", e);
            return false;
        }
    }
}