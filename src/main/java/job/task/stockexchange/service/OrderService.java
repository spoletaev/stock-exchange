package job.task.stockexchange.service;

import job.task.stockexchange.entity.Order;
import job.task.stockexchange.entity.OrderType;
import job.task.stockexchange.entity.SecuritiesType;
import job.task.stockexchange.transaction.OrderTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
public class OrderService {

    private final Map<String, OrderTransaction> orderHandlers;
    private final String ordersFile;

    public OrderService(Map<String, OrderTransaction> orderHandlers, ClientService clientService,
                        @Value("${orders-file}") String ordersFile,
                        @Value("${result-file}") String resultFile) {
        this.orderHandlers = orderHandlers;
        this.ordersFile = ordersFile;
    }

    @PostConstruct
    protected void processOrders() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(ordersFile))) {
            stream.forEach(str -> {
                try {
                    Order order = parseOrder(str);
                    processOrder(order);
                } catch (OrderParseException e) {
                    log.warn("Ошибка парсинга счета клиента из файла {}. Строка {} ", ordersFile, str, e);
                }
            });

        } catch (IOException e) {
            log.error("Oшибка загрузки файлов с заявками клиентов", e.getMessage());
            throw e;
        }
    }

    private void processOrder(Order order) {
        OrderTransaction transaction = orderHandlers.get(order.getOrderType());
        transaction.process(order);
    }

    private Order parseOrder(String row) throws OrderParseException {

        if (StringUtils.isEmpty(row)) {
            throw new OrderParseException("Строка не должна быть пустой");
        }
        String[] fields = row.trim().split("\t");
        if (fields.length != 5) {
            throw new OrderParseException("Количество полей должно быть == 5");
        }
        if (StringUtils.isEmpty(fields[0])) {
            throw new OrderParseException("Имя клиента в заявке не должно быть пустым");
        }
        if (StringUtils.isEmpty(fields[1]) || !OrderType.isValid(fields[1])) {
            throw new OrderParseException("Неверный формат типа заявки");
        }
        try {
            return Order.builder()
                    .clientName(fields[0])
                    .orderType(fields[1])
                    .securitiesType(SecuritiesType.valueOf(fields[2]))
                    .price(Integer.parseInt(fields[3]))
                    .amount(Integer.parseInt(fields[4]))
                    .build();
        } catch (Exception e) {
            throw new OrderParseException("Ошибка парсинга заявки клиента");
        }
    }


}
