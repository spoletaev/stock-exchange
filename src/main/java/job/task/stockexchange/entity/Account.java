package job.task.stockexchange.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Account {

    private String name;
    private float usd;
    private final Map<SecuritiesType, Integer> securities =  new HashMap<>(4);

    public void setSecuritiesAmount(SecuritiesType type, Integer amount) {
        securities.put(type, amount);
    }
}
