package com.ocrv.skimrv.backend.dictionaries.entities.simple;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
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
