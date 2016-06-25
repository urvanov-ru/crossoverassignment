package com.dev.frontend.panels.list;

import java.text.NumberFormat;
import java.util.List;

import ru.urvanov.crossover.salesorder.domain.Product;

import com.dev.frontend.services.Services;

public class ProductDataModel extends ListDataModel {
    private static final long serialVersionUID = 7526529951747614655L;

    public ProductDataModel() {
        super(new String[] { "Code", "Description", "Price", "Quantity" }, 0);
    }

    @Override
    public int getObjectType() {
        return Services.TYPE_PRODUCT;
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
        // [][]{{"01","Product 1","12.5","25"},{"02","Product 2","10","8"}};
        // return sampleData;
        return list
                .stream()
                .map(o -> {
                    Product product = (Product) o;
                    String[] item = new String[4];
                    item[0] = product.getCode();
                    item[1] = product.getDescription();
                    item[2] = NumberFormat.getInstance().format(
                            product.getPrice());
                    item[3] = NumberFormat.getIntegerInstance().format(
                            product.getQuantity());
                    return item;
                }).toArray(size -> new String[size][]);
    }
}
