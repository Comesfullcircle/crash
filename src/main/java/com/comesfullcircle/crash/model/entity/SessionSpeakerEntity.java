package com.comesfullcircle.crash.model.entity;

import com.comesfullcircle.crash.model.user.Role;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name= "sessionspeaker")
public class SessionSpeakerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speakerId;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String name;

    @Column( columnDefinition = "TEXT" ,nullable = false)
    private String description;

    @Column(nullable = false)
    private String profile;

    public Long getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(Long speakerId) {
        this.speakerId = speakerId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SessionSpeakerEntity that = (SessionSpeakerEntity) object;
        return Objects.equals(speakerId, that.speakerId) && Objects.equals(company, that.company) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speakerId, company, name, description, profile);
    }

    public static SessionSpeakerEntity of(String company, String name, String description){
        var sessionSpeakerEntity = new SessionSpeakerEntity();
        sessionSpeakerEntity.setCompany(company);
        sessionSpeakerEntity.setName(name);
        sessionSpeakerEntity.setDescription(description);
        sessionSpeakerEntity.setProfile(
                "https://dev-jayce.github.io/public/profile/"+(new Random().nextInt(100)+1)+".png"
        );

        return sessionSpeakerEntity;
    }
}
