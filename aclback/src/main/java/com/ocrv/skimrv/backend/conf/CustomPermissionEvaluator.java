package com.ocrv.skimrv.backend.conf;

import com.ocrv.skimrv.backend.dictionaries.entities.asfp.DictRateAsfp;
import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate;
import com.ocrv.skimrv.backend.domain.FormAsfp;
import com.ocrv.skimrv.backend.domain.FormSkimIt;
import com.ocrv.skimrv.backend.service.MyACLService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.*;

@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {
    public CustomPermissionEvaluator(JdbcMutableAclService aclService) {
        myAclService = new MyACLService(aclService);
    }

    private final MyACLService myAclService;

    // Таблица обработчиков разрешений
    private final Map<Class<?>, TriFunction<Authentication, Object, String, Boolean>> permissionHandlers = Map.of(
            FormSkimIt.class, (auth, obj, perm) -> handleFormSkimIt(auth, (FormSkimIt) obj, perm),
            FormAsfp.class, (auth, obj, perm) -> handleFormAsfpClass(auth, (FormAsfp) obj, perm)
    );

    // Обработчик разрешений для сущности FormSkimIt
    private boolean handleFormSkimIt(Authentication auth, FormSkimIt formSkimIt, String permission) {
        Integer orgUnitId = formSkimIt.getOrgUnitDictionary().getId();
        Integer dictRateId = formSkimIt.getDictRate().getId();

        boolean hasOrgUnitPermission = checkPermission(auth, OrgUnitDictionary.class, orgUnitId, permission);
        boolean hasDictRatePermission = checkPermission(auth, DictRate.class, dictRateId, permission);

        return hasOrgUnitPermission && hasDictRatePermission;
    }

    // Обработчик разрешений для сущности FormAsfp
    private boolean handleFormAsfpClass(Authentication auth, FormAsfp formAsfp, String permission) {
        return checkPermission(auth, DictRateAsfp.class, formAsfp.getId(), permission);
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        Object domainObject = targetDomainObject;
        if (targetDomainObject instanceof Optional<?> optional) {
            domainObject = optional.get();
        }
        Class<?> domainClass = domainObject.getClass();
        TriFunction<Authentication, Object, String, Boolean> handler = permissionHandlers.get(domainClass);
        if (handler == null) {
            return false; // нет обработчика для этого типа
        }
        return handler.apply(auth, domainObject, (String) permission);
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

        } catch (NotFoundException e) {
            log.debug("ACL not found for {} with id {}", targetType, targetId);
            return false;
        } catch (Exception e) {
            log.error("Error checking permission", e);
            return false;
        }
    }
}