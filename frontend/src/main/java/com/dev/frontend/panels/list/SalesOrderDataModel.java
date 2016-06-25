package com.dev.frontend.panels.list;

import java.text.DecimalFormat;
import java.util.List;

import ru.urvanov.crossover.salesorder.domain.SaleOrderListItem;

import com.dev.frontend.services.Services;

public class SalesOrderDataModel extends ListDataModel {
    private static final long serialVersionUID = 7526529951747614655L;

    public SalesOrderDataModel() {
        super(new String[] { "Order Number", "Customer", "Total Price" }, 0);
    }

    @Override
    public int getObjectType() {
        return Services.TYPE_SALESORDER;
    }

    @Override
    public String[][] convertRecordsListToTableModel(List<Object> list) {
        // TODO by the candidate
        /*
         * This method use list returned by Services.listCurrentRecords and
         * should convert it to array of rows each row is another array of
         * columns of the row
         */
        // String[][] sampleData = new String
        // [][]{{"22423","(01)Customer 1","122.5"},{"22424","(02)Customer 2","3242.5"}};
        // return sampleData;
        final DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
        return list
                .stream()
                .map(o -> {
                    SaleOrderListItem saleOrder = (SaleOrderListItem) o;
                    String[] item = new String[3];
                    item[0] = saleOrder.getNumber();
                    item[1] = "(" + saleOrder.getCustomerCode() + ")"
                            + saleOrder.getCustomerName();
                    item[2] = decimalFormat.format(saleOrder.getTotalPrice());
                    return item;
                }).toArray(size -> new String[size][]);
    }
}
