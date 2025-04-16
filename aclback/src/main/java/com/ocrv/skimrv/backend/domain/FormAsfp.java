package com.ocrv.skimrv.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocrv.skimrv.backend.dictionaries.entities.asfp.DictRateAsfp;
import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormAsfp {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dict_org_unit_id")
    @JsonProperty("orgUnitDictionary")
    private OrgUnitDictionary orgUnitDictionary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dict_rate_asfp_id")
    private DictRateAsfp dictRateAsfp;

}
