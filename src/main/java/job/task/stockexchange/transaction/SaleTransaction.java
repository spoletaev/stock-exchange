package job.task.stockexchange.transaction;

import job.task.stockexchange.entity.OrderType;
import job.task.stockexchange.entity.Order;
import org.springframework.stereotype.Component;

@Component(value = OrderType.SALE)
class SaleTransaction implements OrderTransaction {

    @Override
    public void process(Order order) {

    }
}
