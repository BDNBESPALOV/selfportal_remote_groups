package exel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GeneratingExcelFile {


    private void rowGEF(String value, Cell cell, CellStyle style) {
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void columnGEF(String name, Cell headerCell, CellStyle headerStyle) {
        headerCell.setCellValue(name);
        headerCell.setCellStyle(headerStyle);
    }

    public String gef(ResultSet rs) throws IOException, SQLException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("СТП ИС АСУ ГФ");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 12000);
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook)workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short)12);
        font.setBold(true);
        headerStyle.setFont(font);
        Row header = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int columns = rs.getMetaData().getColumnCount();
        int headerCreateCell = 0;

        int createCell;
        Cell cell;
        for(createCell = 1; createCell <= columns; ++createCell) {
            cell = header.createCell(headerCreateCell);
            this.columnGEF(rs.getMetaData().getColumnName(createCell), cell, headerStyle);
            ++headerCreateCell;
        }

        List<ArrayList<String>> arr = new ArrayList();

        int sheetCreateRow;
        while(rs.next()) {
            createCell = 0;
            ArrayList<String> arr1 = new ArrayList();

            for(sheetCreateRow = 1; sheetCreateRow <= columns; ++sheetCreateRow) {
                arr1.add(rs.getString(sheetCreateRow));
                System.out.print(rs.getString(sheetCreateRow) + " ");
                ++createCell;
            }

            arr.add(arr1);
            System.out.println();
        }

        System.out.println("===== Добавление строк в exel ===== ");
        sheetCreateRow = 1;

        for(int i = 0; i < arr.size(); ++i) {
            Row row = sheet.createRow(sheetCreateRow);

            for(int j = 0; j < ((ArrayList)arr.get(i)).size(); ++j) {
                cell = row.createCell(j);
                cell.setCellValue((String)((ArrayList)arr.get(i)).get(j));
                cell.setCellStyle(style);
                System.out.print((String)((ArrayList)arr.get(i)).get(j) + " ");
            }

            ++sheetCreateRow;
            System.out.println();
        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        LocalDate currentDate = LocalDate.now();
        LocalDate dateMinus7day = LocalDate.now().minusDays(7L);
        String fileLocation = path.substring(0, path.length() - 1) + "СТП ИС АСУ ГФ"+".xlsx";

        try {
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            Throwable var21 = null;

            try {
                workbook.write(outputStream);

            } catch (Throwable var37) {
                var21 = var37;
                throw var37;
            } finally {
                if (outputStream != null) {
                    if (var21 != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable var36) {
                            var21.addSuppressed(var36);
                        }
                    } else {
                        outputStream.close();
                    }
                }

            }
        } finally {

        }

        return dateMinus7day + "-" + currentDate;
    }
}
