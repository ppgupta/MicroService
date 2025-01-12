package com.ecommerce.order.helper;

import com.ecommerce.order.Entity.Order;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper {

    public static boolean checkExcelFormat(MultipartFile file){
        String contentType= file.getContentType();

        boolean data= false;
        return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    }

    public static List<Order> convertExcelToList(InputStream is){
        List<Order> orders = new ArrayList<>();
        try{
          XSSFWorkbook workbook=   new XSSFWorkbook(is);

            XSSFSheet sheet= workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Iterator<Cell> cells = row.iterator();

                int cellId=0;
                Order order = new Order();

                while (cells.hasNext()){

                    Cell cell= cells.next();
                    switch (cellId){
                        case 0:
                            order.setOrderId((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            order.setOrderDate(
                                    (cell.getCellType()== CellType.STRING)?LocalDate.parse(cell.getStringCellValue()):cell.getLocalDateTimeCellValue().toLocalDate());
                            break;
                        case 2:
                            order.setProductList(cell.getStringCellValue());
                            break;
                        case 3:
                            order.setCustomerId((int) cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellId++;
                }
//                order.setOrderDate(LocalDate.parse(row.getCell(0).getStringCellValue())); // Assuming date is in the first column
//                order.setProductList(row.getCell(1).getStringCellValue());               // Product list in the second column
//                order.setCustomerId((int) row.getCell(2).getNumericCellValue());         // Customer ID in the third column

                orders.add(order);
            }
        }
//        orderRepository.saveAll(orders);
         catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
