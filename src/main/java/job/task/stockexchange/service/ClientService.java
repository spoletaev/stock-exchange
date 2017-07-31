package job.task.stockexchange.service;

import job.task.stockexchange.entity.Account;
import job.task.stockexchange.entity.SecuritiesType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
@Component
public class ClientService {

    private final String clientsFile;

    private final Map<String, Account> clients = new ConcurrentHashMap<>();


    public ClientService(@Value("${clients-file}") String clientsFile) {
        this.clientsFile = clientsFile;
    }

    public Collection<Account> getClientAccounts() {
        return clients.values();
    }

    @PostConstruct
    protected void loadClients() throws IOException {
        log.info("Загрузка данных о клиентстких счетах из файла: {}", clientsFile);
        try (Stream<String> stream = Files.lines(Paths.get(clientsFile))) {
            stream.forEach(s -> {
                try {
                    Account account = parseAccount(s);
                    clients.put(account.getName(), account);
                } catch (AccountParseExceprion e) {
                    log.warn("Ошибка парсинга счета клиента из файла {}. Строка {} ", clientsFile, s, e);
                }
            });
            log.info("Счета клиентов загружены. Количество счетов {}", clients.size());

        } catch (IOException e) {
            log.error("Oшибка загрузки файлов со счетами клиентов", e.getMessage());
            throw e;
        }
    }

    private Account parseAccount(String row) throws AccountParseExceprion {
        Account account = new Account();
        if (StringUtils.isEmpty(row)) {
            throw new AccountParseExceprion("Строка не должна быть пустой");
        }
        String[] fields = row.trim().split("\t");
        if (fields.length != 6) {
            throw new AccountParseExceprion("Количество полей должно быть == 6");
        }
        if (StringUtils.isEmpty(fields[0])) {
            throw new AccountParseExceprion("Имя клиента не должно быть пустым");
        }
        account.setName(fields[0]);

        try {
            account.setUsd(Float.parseFloat(fields[1]));
            account.setSecuritiesAmount(SecuritiesType.TYPE_A, Integer.valueOf(fields[2]));
            account.setSecuritiesAmount(SecuritiesType.TYPE_B, Integer.valueOf(fields[3]));
            account.setSecuritiesAmount(SecuritiesType.TYPE_C, Integer.valueOf(fields[4]));
            account.setSecuritiesAmount(SecuritiesType.TYPE_D, Integer.valueOf(fields[5]));
        } catch (Exception e) {
            throw new AccountParseExceprion("Ошибка парсинга счетов клиента");
        }
        return account;
    }

}
