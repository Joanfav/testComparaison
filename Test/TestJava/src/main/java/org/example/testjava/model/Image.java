package org.example.testjava.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "filepath", nullable = false)
    private String filepath;

    @Lob
    @Column(name = "file_content", nullable = false)
    private byte[] fileContent;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
