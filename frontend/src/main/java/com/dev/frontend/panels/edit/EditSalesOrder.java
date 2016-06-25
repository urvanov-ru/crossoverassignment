package com.dev.frontend.panels.edit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import ru.urvanov.crossover.salesorder.domain.Customer;
import ru.urvanov.crossover.salesorder.domain.OrderLine;
import ru.urvanov.crossover.salesorder.domain.Product;
import ru.urvanov.crossover.salesorder.domain.SaleOrder;

import com.dev.frontend.panels.ComboBoxItem;
import com.dev.frontend.services.Services;
import com.dev.frontend.services.Utils;

public class EditSalesOrder extends EditContentPanel
{
	private static final long serialVersionUID = -8971249970444644844L;
	
	private SaleOrder saleOrder;
	private JTextField txtOrderNum = new JTextField();
	private JTextField txtTotalPrice = new JTextField();
	private JComboBox<ComboBoxItem> txtCustomer = new JComboBox<ComboBoxItem>();
	private JTextField txtQuantity = new JTextField();
	private JButton btnAddLine = new JButton("Add");
	private JComboBox<ComboBoxItem> txtProduct = new JComboBox<ComboBoxItem>();
	private DefaultTableModel defaultTableModel = new DefaultTableModel(new String[] { "Product", "Qty", "Price", "Total" }, 0)
	{

		private static final long serialVersionUID = 7058518092777538239L;

		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	};
	private JTable lines = new JTable(defaultTableModel);

	public EditSalesOrder()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("Order Number"), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.weightx = 0.5;
		add(txtOrderNum, gbc);

		txtOrderNum.setColumns(10);
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 3;
		gbc.gridy = 0;
		add(new JLabel("Customer"), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		txtCustomer.setSelectedItem("Select a Customer");
		add(txtCustomer, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("Total Price"), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.weightx = 0.5;
		add(txtTotalPrice, gbc);
		txtTotalPrice.setColumns(10);
		txtTotalPrice.setEditable(false);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		add(new JLabel("Details"), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 6;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(10, 1));
		gbc.fill = GridBagConstraints.BOTH;
		add(separator, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("Product"), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		txtProduct.setSelectedItem("Select a Product");
		add(txtProduct, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 3;
		gbc.gridy = 3;
		add(new JLabel("Quantity"), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 4;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		add(txtQuantity, gbc);
		txtQuantity.setColumns(10);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 5, 2, 5);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		add(btnAddLine, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 7;
		gbc.gridheight = 8;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.ipady = 40;
		gbc.fill = GridBagConstraints.BOTH;
		JScrollPane scrollPane = new JScrollPane(lines, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		lines.setFillsViewportHeight(true);
		add(scrollPane, gbc);
		btnAddLine.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addRow();
			}
		});

	}

	public void addRow()
	{
		ComboBoxItem comboBoxItem = (ComboBoxItem) txtProduct.getSelectedItem();
		if (comboBoxItem == null)
		{
			JOptionPane.showMessageDialog(this, "You must select a product");
			return;

		}
		String productCode = comboBoxItem.getKey();
		double price = Services.getProductPrice(productCode);
		Integer qty = 0;
		try
		{
			qty = Integer.parseInt(txtQuantity.getText());
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Invalid number format in Qty field");
			return;
		}
		double totalPrice = qty * price;
		NumberFormat integerNumberFormat = (NumberFormat) NumberFormat.getIntegerInstance();
		DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
		defaultTableModel.addRow(new String[] { productCode, integerNumberFormat.format(qty), decimalFormat.format(new BigDecimal(price).setScale(2, RoundingMode.HALF_UP)), decimalFormat.format(new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP)) });
		double currentValue = Utils.parseDouble(txtTotalPrice.getText());
		currentValue += totalPrice;
		txtTotalPrice.setText("" + currentValue);
	}
	
	public boolean bindToGUI(Object o) {
		// TODO by the candidate
		/*
		 * This method use the object returned by Services.readRecordByCode and should map it to screen widgets 
		 */
		saleOrder = (SaleOrder) o;
		for (int n = 0; n < txtCustomer.getItemCount(); n++) {
		    if (((ComboBoxItem) txtCustomer.getItemAt(n)).getKey().equals(saleOrder.getCustomer().getCode())) {
		        txtCustomer.setSelectedIndex(n);
		        break;
		    }
		}
		txtOrderNum.setText(saleOrder.getNumber());
		
		NumberFormat integerNumberFormat = NumberFormat.getIntegerInstance();
		
		NumberFormat decimalFormat = DecimalFormat.getInstance();
		BigDecimal totalPrice = BigDecimal.ZERO;
		defaultTableModel.setRowCount(0);
		for (OrderLine orderLine : saleOrder.getOrderLines()) {
		    BigDecimal total = orderLine.getPrice().multiply(new BigDecimal(orderLine.getQuantity())).setScale(2, RoundingMode.HALF_UP);
		    defaultTableModel.addRow(new String[] { orderLine.getProduct().getCode(), integerNumberFormat.format(orderLine.getQuantity()),
		            decimalFormat.format(orderLine.getPrice()), decimalFormat.format(total) });
		    totalPrice = totalPrice.add(total);
		}
		txtTotalPrice.setText(decimalFormat.format(totalPrice));
		return true;
	}

	public Object guiToObject() {
		// TODO by the candidate
		/*
		 * This method collect values from screen widgets and convert them to object of your type
		 * This object will be used as a parameter of method Services.save
		 */
		if (saleOrder == null) saleOrder = new SaleOrder();
		saleOrder.setCustomer((Customer) Services.readRecordByCode(((ComboBoxItem) txtCustomer.getSelectedItem()).getKey(), Services.TYPE_CUSTOMER));
		saleOrder.setNumber(txtOrderNum.getText());
		NumberFormat integerNumberFormat = NumberFormat.getInstance();
		DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
                decimalFormat.setParseBigDecimal(true);
		for (int rowIndex = 0; rowIndex < defaultTableModel.getRowCount(); rowIndex++) {
		    
		    
		    String productCode = (String) defaultTableModel.getValueAt(rowIndex, 0);
		    String qtyString = (String) defaultTableModel.getValueAt(rowIndex, 1);
		    String priceString = (String) defaultTableModel.getValueAt(rowIndex, 2);
		    Integer qty = null;
		    BigDecimal price = null;
		    try {
                qty = integerNumberFormat.parse(qtyString).intValue();
                price = (BigDecimal) decimalFormat.parse(priceString);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		    boolean found = false;
		    for (OrderLine orderLine : saleOrder.getOrderLines()) {
		        if (orderLine.getProduct().getCode().equals(productCode)
		                && orderLine.getQuantity().compareTo(qty) == 0
		                && orderLine.getPrice().compareTo(price) == 0) {
		            found = true;
		            break;
		        }
		    }
		    if (!found) {
		        OrderLine orderLine = new OrderLine();
		        orderLine.setProduct((Product) Services.readRecordByCode(productCode, Services.TYPE_PRODUCT));
		        orderLine.setQuantity(qty);
		        orderLine.setPrice(price);
		        orderLine.setSaleOrder(saleOrder);
		        saleOrder.getOrderLines().add(orderLine);
		    }
		}
		return saleOrder;
	}

	public int getObjectType()
	{
		return Services.TYPE_SALESORDER;
	}

	public String getCurrentCode()
	{
		return txtOrderNum.getText();
	}

	public void clear()
	{
		txtOrderNum.setText("");
		txtCustomer.removeAllItems();
		txtProduct.removeAllItems();
		txtQuantity.setText("");
		txtTotalPrice.setText("");
		defaultTableModel.setRowCount(0);
		saleOrder = null;
	}

	public void onInit()
	{
		List<ComboBoxItem> customers = Services.listCurrentRecordRefernces(Services.TYPE_CUSTOMER);
		for (ComboBoxItem item : customers)
			txtCustomer.addItem(item);

		List<ComboBoxItem> products = Services.listCurrentRecordRefernces(Services.TYPE_PRODUCT);
		for (ComboBoxItem item : products)
			txtProduct.addItem(item);
	}
}
