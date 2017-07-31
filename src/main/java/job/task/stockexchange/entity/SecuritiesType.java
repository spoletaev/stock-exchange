package job.task.stockexchange.entity;


public enum SecuritiesType {

    TYPE_A("A"),
    TYPE_B("B"),
    TYPE_C("C"),
    TYPE_D("D");

    private final String type;

    SecuritiesType(String type) {
        this.type = type;
    }

    public SecuritiesType getByString(String type) throws UnknownSecuritiesTypeException {
        for (SecuritiesType t : SecuritiesType.values()) {
            if (t.type.equals(type)) {
                return t;
            }
        }
        throw new UnknownSecuritiesTypeException("Документ заявленного типа не найден");
    }


}
