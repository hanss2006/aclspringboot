package com.ocrv.skimrv.backend.dictionaries.entities.simple;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dict_org_unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgUnitDictionary {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "full_name", length=1000)
    private String fullName;
}
