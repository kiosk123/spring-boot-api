package com.study.springboot.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends Item {
    private String artist;
    private String etc;
}
