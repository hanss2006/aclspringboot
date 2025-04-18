package com.ocrv.skimrv.backend.domain;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.*;
import com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Table(name = "form_skim_it", uniqueConstraints = {@UniqueConstraint(name = "skim_it_unique_columns", columnNames = {
        "dict_rate_id"})})
public class FormSkimIt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * Справочник показателей
     */
    @JoinColumn(name = "dict_rate_id", referencedColumnName = "id")
    @ManyToOne
    private DictRate dictRate;

    /**
     * Организация осуществляющая ручной ввод
     */
    @JoinColumn(name = "dict_org_unit_id", referencedColumnName = "id")
    @ManyToOne
    private OrgUnitDictionary orgUnitDictionary;

}
