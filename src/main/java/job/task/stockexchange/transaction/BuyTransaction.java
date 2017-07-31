package job.task.stockexchange.transaction;

import job.task.stockexchange.entity.OrderType;
import job.task.stockexchange.entity.Order;
import job.task.stockexchange.service.ClientService;
import org.springframework.stereotype.Component;

@Component(value = OrderType.BUY)
class BuyTransaction implements OrderTransaction {

    private final ClientService clientService;

    public BuyTransaction(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void process(Order order) {
        clientService.getClientAccounts().forEach();
    }
}
