package org.esa.beam.ui.tooladapter.utils;

import com.bc.ceres.binding.*;
import com.bc.ceres.swing.binding.BindingContext;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.esa.beam.framework.gpf.annotations.ParameterDescriptorFactory;
import org.esa.beam.framework.gpf.descriptor.ParameterDescriptor;
import org.esa.beam.framework.gpf.descriptor.ToolAdapterOperatorDescriptor;
import org.esa.beam.framework.gpf.descriptor.ToolParameterDescriptor;
import org.esa.beam.framework.gpf.operators.tooladapter.ToolAdapterConstants;
import org.esa.beam.framework.gpf.ui.OperatorParameterSupport;
import org.esa.beam.framework.ui.UIUtils;
import org.esa.beam.framework.ui.tool.ToolButtonFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ramona Manda
 */
public class OperatorParametersTable extends JTable {

    private static String[] columnNames = {"", "Name", "Description", "Label", "Data type", "Default value", ""};
    private static String[] columnsMembers = {"del", "name", "description", "alias", "dataType", "defaultValue", "edit"};
    private static int[] widths = {27, 100, 200, 80, 100, 249, 30};
    private static final BidiMap typesMap;
    private ToolAdapterOperatorDescriptor operator = null;
    private Map<ToolParameterDescriptor, PropertyMemberUIWrapper> propertiesValueUIDescriptorMap;
    private MultiRenderer tableRenderer;
    private BindingContext context;
    private DefaultCellEditor comboCellEditor;
    private TableCellRenderer comboCellRenderer;
    //private BidiMap map

    static{
        typesMap = new DualHashBidiMap();
        typesMap.put("String", String.class);
        typesMap.put("File", File.class);
        typesMap.put("Integer", Integer.class);
        typesMap.put("List", List.class);
        typesMap.put("Boolean", Boolean.class);
    }

    public OperatorParametersTable(ToolAdapterOperatorDescriptor operator) {
        this.operator = operator;
        propertiesValueUIDescriptorMap = new HashMap<>();
        JComboBox typesComboBox = new JComboBox(typesMap.keySet().toArray());
        comboCellEditor = new DefaultCellEditor(typesComboBox);
        comboCellRenderer = new DefaultTableCellRenderer();

        //List<S2tbxParameterDescriptor> data = operator.getS2tbxParameterDescriptors();
        List<ToolParameterDescriptor> data = operator.getToolParameterDescriptors();
            PropertySet propertySet = new OperatorParameterSupport(operator).getPropertySet();
            //if there is an exception in teh line above, can be because the default value does not match the type
            //TODO which param is wrong????
        context = new BindingContext(propertySet);
        for (ToolParameterDescriptor paramDescriptor : data) {
            if(paramDescriptor.getName().equals(ToolAdapterConstants.TOOL_SOURCE_PRODUCT_ID)){
                propertiesValueUIDescriptorMap.put(paramDescriptor, PropertyMemberUIWrapperFactory.buildEmptyPropertyWrapper());
            } else {
                propertiesValueUIDescriptorMap.put(paramDescriptor, PropertyMemberUIWrapperFactory.buildPropertyWrapper("defaultValue", paramDescriptor, operator, context, null));
            }
            //context.getBinding(paramDescriptor.getName()).setPropertyValue(paramDescriptor.getDefaultValue());
        }
        tableRenderer = new MultiRenderer();
        setModel(new OperatorParametersTableNewTableModel());
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i=0; i < widths.length; i++) {
            getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        this.putClientProperty("JComboBox.isTableCellEditor", Boolean.FALSE);
        this.setRowHeight(20);
    }

    public void addParameterToTable(ToolParameterDescriptor param){
        try {
            PropertyDescriptor property =  ParameterDescriptorFactory.convert(param, new ParameterDescriptorFactory().getSourceProductMap());
            operator.getToolParameterDescriptors().add(param);
            DefaultPropertySetDescriptor propertySetDescriptor = new DefaultPropertySetDescriptor();
            try {
                property.setDefaultValue(param.getDefaultValue());
            }catch (Exception ex){
                ex.printStackTrace();
                //TODO if the previous value cannot be cast, this shoudl be ok???
            }
        propertySetDescriptor.addPropertyDescriptor(property);
        PropertyContainer container = PropertyContainer.createMapBacked(new HashMap<>(), propertySetDescriptor);
        context.getPropertySet().addProperties(container.getProperties());
        propertiesValueUIDescriptorMap.put(param, PropertyMemberUIWrapperFactory.buildPropertyWrapper("defaultValue", param, operator, context, null));
        revalidate();
        }catch (Exception ex){
            ex.printStackTrace();
            //TODO if the previous value cannot be cast, this shoudl be ok???
        }
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        switch (column){
            case 0:
            case 5:
            case 6:
                return tableRenderer;
            case 4:
                return comboCellRenderer;
            default:
                return super.getCellRenderer(row, column);
        }
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        switch (column){
            case 0:
            case 5:
            case 6:
                return tableRenderer;
            case 4:
                return comboCellEditor;
            default:
                return getDefaultEditor(String.class);
        }
    }

    public BindingContext getBindingContext(){
        return context;
    }

    public boolean editCellAt(int row, int column){
        return super.editCellAt(row, column);
    }

    class OperatorParametersTableNewTableModel extends AbstractTableModel {

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return operator.getToolParameterDescriptors().size();
        }

        @Override
        public Object getValueAt(int row, int column) {
            ToolParameterDescriptor descriptor = operator.getToolParameterDescriptors().get(row);
            switch (column) {
                case 0:
                    return false;
                case 4:
                    return typesMap.getKey(descriptor.getDataType());
                case 6:
                    return false;
                default:
                    try {
                        return descriptor.getAttribute(columnsMembers[column]);
                    } catch (PropertyAttributeException e) {
                        e.printStackTrace();
                        //TODO
                        return "ERROR!!!";
                    }
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            ToolParameterDescriptor descriptor = operator.getToolParameterDescriptors().get(rowIndex);
            if(descriptor.getName().equals(ToolAdapterConstants.TOOL_SOURCE_PRODUCT_ID)){
                return false;
            }
            if(descriptor.getName().equals(ToolAdapterConstants.TOOL_TARGET_PRODUCT_FILE) && (columnIndex == 0 || columnIndex == 1 || columnIndex == 4)){
                return false;
            }
            if(descriptor.getName().equals(ToolAdapterConstants.TOOL_TARGET_PRODUCT_ID) && columnIndex <= 1){
                //if it is the target product parameter, it cannot be deleted and the name cannot be changed
                return false;
            }
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ToolParameterDescriptor descriptor = operator.getToolParameterDescriptors().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    operator.removeParamDescriptor(descriptor);
                    revalidate();
                    break;
                case 1:
                    String oldName = descriptor.getName();
                    descriptor.setName(aValue.toString());
                    //since the name is changed, the context must be changed also
                    context.getPropertySet().removeProperty(context.getPropertySet().getProperty(oldName));
                    try {
                        PropertyDescriptor property =  ParameterDescriptorFactory.convert(descriptor, new ParameterDescriptorFactory().getSourceProductMap());
                        try {
                            property.setDefaultValue(descriptor.getDefaultValue());
                        }catch (Exception ex){
                            ex.printStackTrace();
                            //TODO if the previous value cannot be cast, this shoudl be ok???
                        }
                        DefaultPropertySetDescriptor propertySetDescriptor = new DefaultPropertySetDescriptor();
                        propertySetDescriptor.addPropertyDescriptor(property);
                        PropertyContainer container = PropertyContainer.createMapBacked(new HashMap<>(), propertySetDescriptor);
                        context.getPropertySet().addProperties(container.getProperties());
                        propertiesValueUIDescriptorMap.put(descriptor, PropertyMemberUIWrapperFactory.buildPropertyWrapper("defaultValue", descriptor, operator, context, null));
                        revalidate();
                        repaint();
                    } catch (ConversionException e) {
                        e.printStackTrace();
                        //TODO show error
                    }
                    break;
                case 4:
                    //type editing
                    if(descriptor.getDataType() != typesMap.get(aValue)) {
                        descriptor.setDataType((Class<?>) typesMap.get(aValue));
                        descriptor.setDefaultValue(descriptor.getDefaultValue());
                        context.getPropertySet().removeProperty(context.getPropertySet().getProperty(descriptor.getName()));
                        try {
                            PropertyDescriptor property =  ParameterDescriptorFactory.convert(descriptor, new ParameterDescriptorFactory().getSourceProductMap());
                            try {
                                property.setDefaultValue(descriptor.getDefaultValue());
                            }catch (Exception ex){
                                ex.printStackTrace();
                                //TODO if the previous value cannot be cast, this shoudl be ok???
                            }
                            DefaultPropertySetDescriptor propertySetDescriptor = new DefaultPropertySetDescriptor();
                            propertySetDescriptor.addPropertyDescriptor(property);
                            PropertyContainer container = PropertyContainer.createMapBacked(new HashMap<>(), propertySetDescriptor);
                            context.getPropertySet().addProperties(container.getProperties());
                            propertiesValueUIDescriptorMap.put(descriptor, PropertyMemberUIWrapperFactory.buildPropertyWrapper("defaultValue", descriptor, operator, context, null));
                            revalidate();
                            repaint();
                        } catch (ConversionException e) {
                            e.printStackTrace();
                            //TODO show error
                        }
                    }
                    break;
                case 5:
                    //the custom editor should handle this
                    break;
                case 6:
                    //TODO edit
                    break;
                default:
                    try {
                        descriptor.setAttribute(columnsMembers[columnIndex], aValue == null ? null : aValue.toString());
                    } catch (PropertyAttributeException e) {
                        e.printStackTrace();
                        //TODO
                    }
            }
        }
    }

    class MultiRenderer extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
        private TableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        private AbstractButton delButton = ToolButtonFactory.createButton(UIUtils.loadImageIcon("/org/esa/beam/resources/images/icons/DeleteShapeTool16.gif"),
                false);
        private AbstractButton editButton = new JButton("...");

        public MultiRenderer() {
            delButton.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            ParameterDescriptor descriptor = operator.getToolParameterDescriptors().get(row);
            switch (column) {
                case 0:
                    return delButton;
                case 5:
                    try {
                        return propertiesValueUIDescriptorMap.get(descriptor).getUIComponent();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                case 6:
                    return editButton;
                default:
                    return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ParameterDescriptor descriptor = operator.getToolParameterDescriptors().get(row);
            switch (column) {
                case 0:
                    return delButton;
                case 5:
                    try {
                        return propertiesValueUIDescriptorMap.get(descriptor).getUIComponent();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                case 6:
                    return editButton;
                default:
                    return getDefaultEditor(String.class).getTableCellEditorComponent(table, value, isSelected, row, column);
            }
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}