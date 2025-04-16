package com.ocrv.skimrv.backend.dictionaries.entities.asfp;

import jakarta.persistence.Column;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dict_rate_asfp")
public class DictRateAsfp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(unique = true, length = 6)
    private String code;

    @Column(length = 100)
    private String name;

    @Column(length = 200, name = "full_name")
    private String fullName;
}
