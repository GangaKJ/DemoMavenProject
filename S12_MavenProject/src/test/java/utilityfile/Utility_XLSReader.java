package utilityfile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class Utility_XLSReader {

	// instance variables
    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;
    private XSSFCell cell = null;
    
    
// constructor : Loads Excel file and first sheet
    public Utility_XLSReader(String path) {
        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returns the number of rows in a sheet (including header).
    public int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return 0;
        else {
            sheet = workbook.getSheetAt(index);
            return sheet.getLastRowNum() + 1;
        }
    }

    // Read data from a specific cell by: 1. Finding the sheet by name.
    // 2.Finding the column number using the header row (first row).
    // 3.Accessing the target row and cell.
    // 4.Returning the cell's value as a string.
    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            if (rowNum <= 0) return "";
           
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            int colNum = -1;

            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName.trim())) {
                    colNum = i;
                    break;
                }
            }

            if (colNum == -1) return "";

            row = sheet.getRow(rowNum - 1);
            if (row == null) return "";
            cell = row.getCell(colNum);
            if (cell == null) return "";

            return getCellValueAsString(cell);

        } catch (Exception e) {
            e.printStackTrace();
            return "row " + rowNum + " or column " + colName + " does not exist in xls";
        }
    }

    // Converts the cell value into a string regardless of the type:
    public String getCellValueAsString(Cell cell) {
        try {
            CellType type = cell.getCellType();

            switch (type) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(cell.getDateCellValue());
                        return cal.get(Calendar.DAY_OF_MONTH) + "/" +
                               (cal.get(Calendar.MONTH) + 1) + "/" +
                               cal.get(Calendar.YEAR);
                    }
                    return String.valueOf(cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                case BLANK:
                    return "";
                default:
                    return "Unsupported cell type";
            }
        } catch (Exception e) {
            return "Error reading cell";
        }
    }

    //setCellData: 1. Finds sheet and column.2.Creates the row and cell if they don’t exist.
    // 3.Writes the value.   4.Saves changes using FileOutputStream.
    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if (rowNum <= 0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            if (index == -1)
                return false;

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            int colNum = -1;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
                    colNum = i;
                    break;
                }
            }

            if (colNum == -1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}