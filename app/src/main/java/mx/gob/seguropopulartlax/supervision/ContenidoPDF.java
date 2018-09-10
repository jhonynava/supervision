package mx.gob.seguropopulartlax.supervision;

import android.content.Context;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ContenidoPDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitulos = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
    private Font ftext = new Font(Font.FontFamily.HELVETICA,11,Font.NORMAL,BaseColor.BLACK);

    public ContenidoPDF(Context context) {
        this.context = context;
    }

    public void openDocument(){
        crearArghivo();
        try {
            document = new Document(PageSize.LETTER);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e){
            Log.e("openDocument", e.toString());
        }
    }

    private void crearArghivo(){
        File folder = new File(Environment.getExternalStorageDirectory().toString(),"PDF");
        if(!folder.exists());
        folder.mkdir();

        pdfFile = new File(folder,"CEDULA_SUPERVISION.pdf");
    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String tittle, String subject, String author){
        document.addTitle(tittle);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void crearTabla(String[]header, ArrayList<String[]>clients){
        paragraph = new Paragraph();
        paragraph.setFont(ftext);
        PdfPTable pdfPTable = new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        PdfPCell pdfPCell;
        int indexC=0;
        while (indexC<header.length){
            pdfPCell = new PdfPCell(new Phrase(header[indexC++],fTitulos));
        }
    }

}
