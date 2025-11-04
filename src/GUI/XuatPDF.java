package GUI;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class XuatPDF {
    private static final NumberFormat VND = NumberFormat.getInstance(new Locale("vi", "VN"));
    private static final SimpleDateFormat DF = new SimpleDateFormat("H:mm dd-MM-yyyy");

    public static void xuatHoaDonPDF(
            String outPath,
            String fontPath,
            String maHD,
            Timestamp gioVao,
            Timestamp gioRa,
            String tenKH,
            String thuNgan,
            DefaultTableModel modelBang,
            long tongSL,
            long thanhTien,
            int diemTru,
            int giamGiaPT,
            long tongCong,
            long khachTra,
            long traLai
    ) throws Exception {

        // ===== Fonts =====
    	BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fTitle = new Font(bf, 17, Font.BOLD);
        Font fBody = new Font(bf, 10);
        Font fBold = new Font(bf, 10, Font.BOLD);
        Font fSmall = new Font(bf, 10);

        Document doc = new Document(PageSize.A5, 22, 22, 18, 22);
        PdfWriter.getInstance(doc, new FileOutputStream(outPath));
        doc.open();

        // ===== Header =====
        Paragraph title = new Paragraph("Hóa đơn thanh toán", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        Paragraph pMa = new Paragraph("Mã hóa đơn: " + maHD + "\nNgày: " + DF.format(gioRa), fBody);
        pMa.setAlignment(Element.ALIGN_CENTER);
        pMa.setSpacingAfter(8f);
        doc.add(pMa);

        // Thông tin khách
        PdfPTable info = new PdfPTable(new float[]{35, 65});
        info.setWidthPercentage(100);
        info.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addInfo(info, "Giờ vào:", DF.format(gioVao), fBody);
        addInfo(info, "Giờ ra:", DF.format(gioRa), fBody);
        addInfo(info, "Tên khách hàng:", tenKH == null ? "Khách lẻ" : tenKH, fBody);
        addInfo(info, "Thu ngân:", thuNgan == null ? "" : thuNgan, fBody);
        doc.add(info);

        doc.add(new Paragraph(" ")); // khoảng trắng

        // ===== BẢNG MÓN =====
        PdfPTable tbl = new PdfPTable(new float[]{6, 38, 12, 20, 24});
        tbl.setWidthPercentage(100);
        addHeader(tbl, "TT", fBold);
        addHeader(tbl, "Tên món", fBold);
        addHeader(tbl, "SL", fBold);
        addHeader(tbl, "Giá", fBold);
        addHeader(tbl, "Thành tiền", fBold);

        int stt = 1;
        for (int i = 0; i < modelBang.getRowCount(); i++) {
            String tenMon = String.valueOf(modelBang.getValueAt(i, 1));
            int sl = ((Number) modelBang.getValueAt(i, 2)).intValue();
            long gia = ((Number) modelBang.getValueAt(i, 3)).longValue();
            long tt = ((Number) modelBang.getValueAt(i, 4)).longValue();

            addCell(tbl, String.valueOf(stt++), fBody, Element.ALIGN_CENTER);
            addCell(tbl, tenMon, fBody, Element.ALIGN_LEFT);
            addCell(tbl, String.valueOf(sl), fBody, Element.ALIGN_CENTER);
            addCell(tbl, VND.format(gia), fBody, Element.ALIGN_RIGHT);
            addCell(tbl, VND.format(tt), fBody, Element.ALIGN_RIGHT);
        }

        // Tổng cộng phần bảng
        PdfPCell line = new PdfPCell(new Phrase(""));
        line.setColspan(5);
        line.setBorder(Rectangle.TOP);
        line.setPaddingTop(4f);
        tbl.addCell(line);

        addFooterRow(tbl, "Tổng SL:", String.valueOf(tongSL), fBody, fBody);
        addFooterRow(tbl, "Thành tiền:", VND.format(thanhTien), fBody, fBody);
        addFooterRow(tbl, "Điểm TL:", String.valueOf(diemTru), fBody, fBody);
        addFooterRow(tbl, "Giảm giá:", giamGiaPT + " %", fBody, fBody);
        addFooterRow(tbl, "Tổng cộng:", VND.format(tongCong), fBold, fBold);
        addFooterRow(tbl, "Khách trả:", VND.format(khachTra), fBody, fBody);
        addFooterRow(tbl, "Trả lại khách:", VND.format(traLai), fBody, fBody);

        doc.add(tbl);

        // Footer cảm ơn
        Paragraph thank = new Paragraph("\nXin cảm ơn quý khách!\nHẹn gặp lại quý khách lần sau.", fSmall);
        thank.setAlignment(Element.ALIGN_CENTER);
        doc.add(thank);

        doc.close();
    }

    private static void addInfo(PdfPTable t, String key, String val, Font f) {
        PdfPCell c1 = new PdfPCell(new Phrase(key, f));
        c1.setBorder(Rectangle.NO_BORDER);
        PdfPCell c2 = new PdfPCell(new Phrase(val, f));
        c2.setBorder(Rectangle.NO_BORDER);
        t.addCell(c1);
        t.addCell(c2);
    }

    private static void addHeader(PdfPTable t, String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setBackgroundColor(BaseColor.WHITE);
        c.setPadding(5f);
        t.addCell(c);
    }

    private static void addCell(PdfPTable t, String text, Font f, int align) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setHorizontalAlignment(align);
        c.setPadding(4f);
        c.setBorderWidth(0.5f);
        t.addCell(c);
    }

    private static void addFooterRow(PdfPTable t, String label, String value, Font valFont, Font labelFont) {
        PdfPCell l = new PdfPCell(new Phrase(label, labelFont));
        l.setColspan(4);
        l.setHorizontalAlignment(Element.ALIGN_LEFT); //  căn sang trái
        l.setBorder(Rectangle.NO_BORDER);
        l.setPaddingLeft(10f); // thêm 1 chút khoảng cách so với mép
        l.setPaddingTop(2f);
        l.setPaddingBottom(2f);

        PdfPCell v = new PdfPCell(new Phrase(value, valFont));
        v.setHorizontalAlignment(Element.ALIGN_RIGHT); // giữ giá trị bên phải
        v.setBorder(Rectangle.NO_BORDER);
        v.setPaddingRight(10f);
        t.addCell(l);
        t.addCell(v);
    }
}
