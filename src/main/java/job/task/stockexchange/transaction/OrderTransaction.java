package job.task.stockexchange.transaction;

import job.task.stockexchange.entity.Order;

public interface OrderTransaction {

    public void process(Order order);
}
