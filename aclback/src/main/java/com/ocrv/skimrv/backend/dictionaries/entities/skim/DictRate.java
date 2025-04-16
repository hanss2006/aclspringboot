package com.ocrv.skimrv.backend.dictionaries.entities.skim;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "dict_rate")
public class DictRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false)
    private String code;

    @Column(name = "full_name")
    private String fullName;
}
