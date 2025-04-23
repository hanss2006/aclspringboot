package com.ocrv.skimrv.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AclSupportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long createOrGetSid(String sid, boolean principal) {
        String selectSql = "SELECT id FROM acl_sid WHERE sid = ? AND principal = ?";
        List<Long> ids = jdbcTemplate.query(selectSql,
                (rs, rowNum) -> rs.getLong("id"),
                sid, principal);
        if (!ids.isEmpty()) return ids.get(0);

        String insertSql = "INSERT INTO acl_sid (principal, sid) VALUES (?, ?)";
        jdbcTemplate.update(insertSql, principal, sid);

        // После вставки — получить ID
        return jdbcTemplate.queryForObject("SELECT id FROM acl_sid WHERE sid = ? AND principal = ?",
                Long.class, sid, principal);
    }

    public Long createOrGetClass(String className) {
        String selectSql = "SELECT id FROM acl_class WHERE class = ?";
        List<Long> ids = jdbcTemplate.query(selectSql,
                (rs, rowNum) -> rs.getLong("id"),
                className);
        if (!ids.isEmpty()) return ids.get(0);

        String insertSql = "INSERT INTO acl_class (class) VALUES (?)";
        jdbcTemplate.update(insertSql, className);

        return jdbcTemplate.queryForObject("SELECT id FROM acl_class WHERE class = ?",
                Long.class, className);
    }


    public Long createOrGetObjectIdentity(Long classId, Long objectId, Long ownerSidId, Long parentObjectId) {
        // Проверка существования
        String selectSql = "SELECT id FROM acl_object_identity WHERE object_id_class = ? AND object_id_identity = ?";
        List<Long> ids = jdbcTemplate.query(selectSql, (rs, rowNum) -> rs.getLong("id"), classId, objectId);
        if (!ids.isEmpty()) return ids.get(0);

        // Вставка новой записи
        String insertSql = "INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, classId, objectId,
                parentObjectId,
                ownerSidId,
                true);

        return jdbcTemplate.queryForObject(
                "SELECT id FROM acl_object_identity WHERE object_id_class = ? AND object_id_identity = ?",
                Long.class, classId, objectId);
    }

    public void updateObjectIdentityOwner(Long id, Long newOwnerSidId) {
        String updateSql = "UPDATE acl_object_identity SET owner_sid = ? WHERE id = ?";
        jdbcTemplate.update(updateSql, newOwnerSidId, id);
    }

    public void updateObjectIdentityParent(Long id, Long newParentId) {
        String updateSql = "UPDATE acl_object_identity SET parent_object = ? WHERE id = ?";
        jdbcTemplate.update(updateSql, newParentId, id);
    }
}

