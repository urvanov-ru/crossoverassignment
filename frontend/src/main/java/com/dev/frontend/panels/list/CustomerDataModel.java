package com.dev.frontend.panels.list;

import java.util.List;

import ru.urvanov.crossover.salesorder.domain.Customer;

import com.dev.frontend.services.Services;

public class CustomerDataModel extends ListDataModel {
    private static final long serialVersionUID = 7526529951747613655L;

    public CustomerDataModel() {
        super(new String[] { "Code", "Name", "Phone", "Current Credit" }, 0);
    }

    @Override
    public int getObjectType() {
        return Services.TYPE_CUSTOMER;
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
        // [][]{{"01","Customer 1","+201011121314","23.4"},{"02","Customer 2","+201112131415","1.4"}};
        // String[][] result = new String[list.size()][];
        // int n = 0;
        // for (Object obj : list) {
        // Customer customer = (Customer) obj;
        // result[n] = new String[4];
        // result[n][0] = customer.getCode();
        // result
        // n++;
        // }
        //
        String[][] result = list.stream().map(o -> {
            Customer customer = (Customer) o;
            String[] item = new String[4];
            item[0] = customer.getCode();
            item[1] = customer.getName();
            item[2] = customer.getPhone1();
            item[3] = customer.getCurrentCredit().toString();
            return item;
        }).toArray(size -> new String[size][]);
        return result;
    }
}
