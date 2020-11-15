package com.study.springboot.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("M")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends Item {
    private String director;
    private String actor;
}
