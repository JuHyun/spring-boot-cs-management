package com.example.cs.springbootcsmanagement.domains;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tst_cs_qna_type")
@Getter
@NoArgsConstructor
@ToString(exclude = {"subQnATypes"})
public class QnAType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "expo_order", length = 10)
    private int expoOrder = 0;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY, mappedBy = "upperQnAType")
    @OrderBy(value = "expo_order asc")
    private Set<QnAType> subQnATypes = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_qna_type_id", foreignKey = @ForeignKey(name = "fk_tst_cs_qna_type_id"))
    private QnAType upperQnAType;

    @Builder
    public QnAType(String title, int expoOrder, QnAType upperQnAType) {
        this.title = title;
        this.expoOrder = expoOrder;
        this.setUpperQnAType(upperQnAType);
    }

    /**
     * 양방향 연관관계 맺기.
     *
     * @param upperQnAType
     */
    public void setUpperQnAType(QnAType upperQnAType) {
        if (Objects.isNull(upperQnAType)) return;

        // 기존 upperQnAType과의 관계 제거하기.
        if (Objects.nonNull(this.upperQnAType)) {
            this.upperQnAType.getSubQnATypes().remove(this);
        }
        this.upperQnAType = upperQnAType;
        upperQnAType.getSubQnATypes().add(this);
    }
}
