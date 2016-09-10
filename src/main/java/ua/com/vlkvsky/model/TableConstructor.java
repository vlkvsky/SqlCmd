package ua.com.vlkvsky.model;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableConstructor {

    private final Table table;
    private final Set<String> columns;
    private final List<Map<String, Object>> tableData;

    public TableConstructor(Set<String> columns, List<Map<String, Object>> tableData) {
        this.columns = columns;
        this.tableData = tableData;
        table = new Table(columns.size(), BorderStyle.CLASSIC, ShownBorders.ALL);
    }

    public String getTableString() {
        build();
        return table.render();
    }

    private void build() {
        buildHeader();
        buildRows();
    }

    private void buildHeader() {
        columns.forEach(table::addCell);
    }

    private void buildRows() {
        for (Map<String, Object> row : tableData) {
            for (Object value : row.values()) {
                if (value != null) {
                    table.addCell(value.toString());
                } else {
                    table.addCell("");
                }
            }
        }
    }
}