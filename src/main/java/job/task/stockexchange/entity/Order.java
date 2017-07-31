package job.task.stockexchange.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Order {

    private final String clientName;
    private final String orderType;
    private final SecuritiesType securitiesType;
    private final int price;
    private final int amount;
}
