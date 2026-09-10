package com.ufape.libraryhub.communication.converter;

import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.communication.dto.response.ReservationDTOResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter {

    private final MemberConverter memberConverter;

    private final ItemConverter itemConverter;

    public ReservationConverter(MemberConverter memberConverter, ItemConverter itemConverter) {
        this.memberConverter = memberConverter;
        this.itemConverter = itemConverter;
    }

    public ReservationDTOResponse entityToResponse(Reservation reservation) {
        return new ReservationDTOResponse(
                reservation.getId(),
                memberConverter.entityToResponse(reservation.getMember()),
                itemConverter.entityToResponse(reservation.getItem()),
                reservation.getCreatedAt(),
                reservation.getStatus().name(),
                reservation.isActive());
    }

    public List<ReservationDTOResponse> entityToResponseList(List<Reservation> reservations) {
        return reservations.stream().map(this::entityToResponse).toList();
    }
}
