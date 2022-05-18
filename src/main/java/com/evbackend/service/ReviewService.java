package com.evbackend.service;

import com.evbackend.commands.CreateReviewCommand;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.chargestation.Review;
import com.evbackend.model.users.User;
import com.evbackend.repository.ChargeStationRepository;
import com.evbackend.repository.ReviewRepository;
import com.evbackend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private final ChargeStationRepository chargeStationRepository;


    public Mono<List<Review>> getReviewsForChargeStation(UUID chargeStationId) {
        return this.reviewRepository.getReviewsForChargeStation(chargeStationId).convert().with(toMono());
    }



    public Mono<String> createReview(UUID userId, CreateReviewCommand createReviewCommand) {

        Mono<User> user = userRepository.getUserByUserId(userId).convert().with(toMono());
        Mono<ChargeStation> chargeStation = chargeStationRepository.getChargeStationById(createReviewCommand.getChargeStationId()).convert().with(toMono());

        Mono<Review> review = Mono.zip(user, chargeStation).flatMap(u ->
                this.reviewRepository.create(
                        Review.builder()
                                .createdBy(u.getT1())
                                .chargeStationId(u.getT2())
                                .feedback(createReviewCommand.getFeedback())
                                .rating(createReviewCommand.getRating())
                                .build()
                ).convert().with(toMono()));
        return review.map(u -> u.getReviewId().toString());
    }

}
