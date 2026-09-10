package com.ufape.libraryhub.business.model;

import com.ufape.libraryhub.exception.ReservationNotActiveException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    protected Reservation() {
    }

    public Reservation(Member member, Item item, LocalDate createdAt) {
        this.member = member;
        this.item = item;
        this.createdAt = createdAt;
        this.status = ReservationStatus.ACTIVE;
    }

    public void fulfill() throws ReservationNotActiveException {
        requireActive();
        status = ReservationStatus.FULFILLED;
    }

    public void cancel() throws ReservationNotActiveException {
        requireActive();
        status = ReservationStatus.CANCELLED;
    }

    public boolean isActive() {
        return status == ReservationStatus.ACTIVE;
    }

    public boolean belongsTo(Long memberId) {
        return member.getId() != null && member.getId().equals(memberId);
    }

    private void requireActive() throws ReservationNotActiveException {
        if (!isActive()) {
            throw new ReservationNotActiveException(id, status);
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Item getItem() {
        return item;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
