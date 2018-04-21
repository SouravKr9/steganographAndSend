
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;


@WebServlet("/decrypt")
@SuppressWarnings("serial")
		public class decrypt extends HttpServlet {
		   private BufferedImage embeddedImage;
		   private String mess;
		   private boolean isMultipart;
		   private String filePath;
		   private int maxFileSize = 50 * 100000;
		   private int maxMemSize = 4 * 1024;
		   private File file ;

		   public void init( ){
		      // Get the file location where it would be stored.
		      filePath = getServletContext().getInitParameter("file-upload"); 
		   }
		   
		   public void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, java.io.IOException {
		   
		      // Check that we have a file upload request
		      isMultipart = ServletFileUpload.isMultipartContent(request);
		      response.setContentType("text/html");
		      java.io.PrintWriter out = response.getWriter( );
		   
		      if( !isMultipart ) {
		         out.println("<html>");
		         out.println("<head>");
		         out.println("<title>Servlet upload</title>");  
		         out.println("</head>");
		         out.println("<body>");
		         out.println("<p>No file uploaded</p>"); 
		         out.println("</body>");
		         out.println("</html>");
		         return;
		      }
		  
		      DiskFileItemFactory factory = new DiskFileItemFactory();
		   
		      // maximum size that will be stored in memory
		      factory.setSizeThreshold(maxMemSize);
		   
		      // Location to save data that is larger than maxMemSize.
		      factory.setRepository(new File("C:/Users/USER/Desktop/upload"));

		      // Create a new file upload handler
		      ServletFileUpload upload = new ServletFileUpload(factory);
		   
		      // maximum file size to be uploaded.
		      upload.setSizeMax( maxFileSize );

		      try { 
		         // Parse the request to get file items.
		         List fileItems = upload.parseRequest(request);
			
		         // Process the uploaded file items
		         Iterator i = fileItems.iterator();

		         out.println("<html>");
		         out.println("<head>");
		         out.println("<title>Servlet upload</title>");  
		         out.println("</head>");
		         out.println("<body>");
		   
		         while ( i.hasNext () ) {
		            FileItem fi = (FileItem)i.next();
		            if ( !fi.isFormField () ) {
		               // Get the uploaded file parameters
		               String fieldName = fi.getFieldName();
		               String fileName = fi.getName();
		               String contentType = fi.getContentType();
		               boolean isInMemory = fi.isInMemory();
		               long sizeInBytes = fi.getSize();
		            
		               // Write the file
		               if( fileName.lastIndexOf("\\") >= 0 ) {
		                  file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
		               } else {
		                  file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
		               }
		     
		              
		              
		              try { 
		           	    embeddedImage = ImageIO.read( file ); 
		           	} 
		           	catch (IOException e) { }
		              
		            //  decrypt(file);
		              
		              decodeMessage();
		              out.print("<h3>");
		              out.print("The message is "+mess);
		              out.print("</h3>");
		               
		            }
		         }
		         out.println("</body>");
		         out.println("</html>");
		         } catch(Exception ex) {
		            System.out.println(ex);
		         }
		      }
		
		   
		   private void decodeMessage() {
		     int len = extractInteger(embeddedImage, 0, 0);
		     byte b[] = new byte[len];
		  for(int i=0; i<len; i++)
		         b[i] = extractByte(embeddedImage, i*8+32, 0);
		      mess=new String(b);
		      System.out.println(mess);
		      }
		   
		   private int extractInteger(BufferedImage img, int start, int storageBit) {
		      int maxX = img.getWidth(), maxY = img.getHeight(), 
		         startX = start/maxY, startY = start - startX*maxY, count=0;
		      int length = 0;
		      for(int i=startX; i<maxX && count<32; i++) {
		         for(int j=startY; j<maxY && count<32; j++) {
		            int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
		            length = setBitValue(length, count, bit);
		            count++;
		            }
		         }
		      return length;
		    }
		   
		   private byte extractByte(BufferedImage img, int start, int storageBit) {
		      int maxX = img.getWidth(), maxY = img.getHeight(), 
		         startX = start/maxY, startY = start - startX*maxY, count=0;
		      byte b = 0;
		      for(int i=startX; i<maxX && count<8; i++) {
		         for(int j=startY; j<maxY && count<8; j++) {
		            int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
		            b = (byte)setBitValue(b, count, bit);
		            count++;
		            }
		         }
		    return b;
		      }
		   
		   
		   
		   private int getBitValue(int n, int location) {
		      int v = n & (int) Math.round(Math.pow(2, location));
		      return v==0?0:1;
		      }
		   
		   private int setBitValue(int n, int location, int bit) {
		      int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
		      if(bv == bit)
		         return n;
		      if(bv == 0 && bit == 1)
		         n |= toggle;
		      else if(bv == 1 && bit == 0)
		         n ^= toggle;
		      return n;
		      }
		   
		   
		  }