package ua.vspelykh.aggregator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.vspelykh.aggregator.client.ProductClient;
import ua.vspelykh.aggregator.client.PromotionClient;
import ua.vspelykh.aggregator.client.ReviewClient;
import ua.vspelykh.aggregator.dto.*;

import java.util.List;

/**
 * Сервис для агрегации информации о продукте из нескольких микросервисов.
 * <p>
 * Этот класс реализует паттерн Агрегатор, который объединяет данные из разных
 * микросервисов (продукт, промоакции, отзывы) в один объект {@code ProductAggregate}.
 * Агрегатор отвечает за взаимодействие с несколькими источниками данных и их объединение
 * для конечного пользователя или системы.
 */
@Service
@RequiredArgsConstructor
public class ProductAggregateService {

    private final ProductClient productClient;
    private final PromotionClient promotionClient;
    private final ReviewClient reviewClient;

    /**
     * Агрегирует данные о продукте, промоакции и отзывах.
     * <p>
     * Метод реализует паттерн Агрегатор, запрашивая данные о продукте, промоакциях и
     * отзывах из разных микросервисов, и объединяет их в единый объект {@code ProductAggregate}.
     * </p>
     *
     * @param id идентификатор продукта для агрегации данных
     * @return {@code Mono<ProductAggregate>} с агрегированной информацией о продукте,
     * включая цену, описание, категорию и отзывы
     */
    public Mono<ProductAggregate> aggregateProduct(Integer id) {
        return Mono.zip(
                productClient.getProduct(id),
                promotionClient.getPromotion(id),
                reviewClient.getReviews(id)
        ).map(t -> toDto(t.getT1(), t.getT2(), t.getT3()));
    }

    private ProductAggregate toDto(ProductResponse product, PromotionResponse promotion, List<Review> reviews) {

        return ProductAggregate.builder()
                .price(getPrice(product, promotion))
                .id(product.getId())
                .category(product.getCategory())
                .description(product.getDescription())
                .reviews(reviews)
                .build();

    }

    private static Price getPrice(ProductResponse product, PromotionResponse promotion) {
        Price price = new Price();

        double amountSaved = product.getPrice() * promotion.getDiscount() / 100;
        double discountedPrice = product.getPrice() - amountSaved;

        price.setListPrice(product.getPrice());
        price.setDiscount(promotion.getDiscount());
        price.setAmountSaved(amountSaved);
        price.setDiscountedPrice(discountedPrice);

        return price;
    }
}
