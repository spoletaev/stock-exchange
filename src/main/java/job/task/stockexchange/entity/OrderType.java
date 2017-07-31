package job.task.stockexchange.entity;

import org.springframework.util.StringUtils;

public final class OrderType {

    public static final String SALE = "s";
    public static final String BUY = "b";


    public static boolean isValid(String orderTypeStr) {
        return !StringUtils.isEmpty(orderTypeStr) &&
                (SALE.equals(orderTypeStr) || BUY.equals(orderTypeStr));
    }


}
