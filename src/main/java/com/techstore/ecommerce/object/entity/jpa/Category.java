package com.techstore.ecommerce.object.entity.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "category_seq", sequenceName = "categories_id_seq", allocationSize = 1)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String slug;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "categories_brands",
            joinColumns = @JoinColumn(name = "brand_id", columnDefinition = "bigint"),
            inverseJoinColumns = @JoinColumn(name = "category_id", columnDefinition = "bigint"))
    private List<Brand> brands;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @ToString.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

}
