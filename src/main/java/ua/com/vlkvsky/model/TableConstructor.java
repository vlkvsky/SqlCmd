package ua.com.vlkvsky.model;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.*;

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

    public static class Columns {

        private List<List<String>> row = new ArrayList<>();
        private List<Integer> maxLengths = new ArrayList<>();
        private int numColumns = -1;

        public Columns addLine(String... line) {
            if (numColumns == -1) {
                numColumns = line.length;
                for (int i = 0; i < numColumns; i++) {
                    maxLengths.add(0);
                }
            }
            if (numColumns != line.length) {
                throw new IllegalArgumentException();
            }
            for (int i = 0; i < numColumns; i++) {
                maxLengths.set(i, Math.max(maxLengths.get(i), line[i].length()));
            }
            row.add(Arrays.asList(line));
            return this;
        }

        public String print() {
            String result = "";
            for (List<String> line : row) {
                for (int i = 0; i < numColumns; i++) {
                    result += pad(line.get(i), maxLengths.get(i));
                }
                result += System.lineSeparator();
            }
            return result;
        }

        private String pad(String word, int newLength) {
            while (word.length() < newLength) {
                word += " ";
            }
            return word;
        }
    }
}