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
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;
import java.io.*;
import java.util.*;
 
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
 //@WebServlet ("/UploadServlet")
public class UploadServlet extends HttpServlet {
   private BufferedImage embeddedImage;
   private boolean isMultipart;
   private String filePath;
   private int maxFileSize = 50 * 50000;
   private int maxMemSize = 4 * 1024;
   private File file ;
   private String mess;
   
   private BufferedImage buffImg = 
		    new BufferedImage(700,525, BufferedImage.TYPE_INT_ARGB);
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
         out.println("<meta http-equiv='refresh' content='10;url=http://localhost:8090/cdac_project/email.html'/>");
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
               
               
               
              while( i.hasNext()){
               FileItem fi1 = (FileItem)i.next();
               if(fi1.isFormField()){ 
            	 mess=fi1.getString();
            	// out.print(mess);}
               }}
               // Write the file
               if( fileName.lastIndexOf("\\") >= 0 ) {
                  file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
               } else {
                  file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
               }
             
               fi.write(file);
               
               
              
              
              
              try { 
           	    buffImg = ImageIO.read( file ); 
           	} 
           	catch (IOException e) { }
              
             
              
              embedMessage();
              
              if( fileName.lastIndexOf("\\") >= 0 ) {
                  file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
               } else {
                  file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
               }
       
              	
              
              File f = new File("C:/Users/USER/Desktop/emb/"+fileName.split(".")+".png");
              ImageIO.write(buffImg, "PNG", f);
              
              encrypt(f); 
               
               out.println("Uploaded Filename: " + fileName + "<br>");
              
               //RequestDispatcher rs = request.getRequestDispatcher("http://localhost:8090/cdac_project/email.html");
            }
         }
         out.println("</body>");
         out.println("</html>");
         } catch(Exception ex) {
            System.out.println("exception is"+ex);
            
         }// response.sendRedirect("email.html");
      }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException {

         throw new ServletException("GET method used with " +
            getClass( ).getName( )+": POST method required.");
      }
      
      public void encrypt(File f1) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
			Cipher cpr= Cipher.getInstance("AES");
			KeyGenerator keygen=KeyGenerator.getInstance("AES");
			byte[] key1="sonalidashcse2b4".getBytes();
			
			  SecretKeySpec key = new SecretKeySpec(key1, "AES");
			System.out.println(key);
			cpr.init(Cipher.ENCRYPT_MODE, key);
			CipherInputStream cprin=new CipherInputStream(new FileInputStream(f1),cpr);
			FileOutputStream fin=new FileOutputStream(new File("C:/Users/USER/Desktop/emb/aes_en.jpg"));
			int i; 
			while((i=cprin.read())!=-1){
			fin.write(i);
			}
	 }
      
      
      
 	 private void embedMessage() {
 	    String message =mess ;
 	    System.out.println("message is"+message);
 	    embeddedImage = buffImg.getSubimage(0,0,
 	    		buffImg.getWidth(),buffImg.getHeight());
 	    embedMessage(embeddedImage, message);
 	  
 	    }
 	 
 	 private void embedMessage(BufferedImage img, String mess) {
 	    int messageLength = mess.length();
 	   
 	   int imageWidth = img.getWidth(), imageHeight = img.getHeight(),
 	       imageSize = imageWidth * imageHeight;
 	    if(messageLength * 8 + 32 > imageSize) {
 	       System.out.println("too long");
 	       return;
 	       }
 	    embedInteger(img, messageLength, 0, 0);
 	 
 	    byte b[] = mess.getBytes();
 	    for(int i=0; i<b.length; i++)
 	       embedByte(img, b[i], i*8+32, 0);
 	    }
 	 
 	  private void embedInteger(BufferedImage img, int n, int start, int storageBit) {
 	     int maxX = img.getWidth(), maxY = img.getHeight(), 
 	        startX = start/maxY, startY = start - startX*maxY, count=0;
 	     for(int i=startX; i<maxX && count<32; i++) {
 	        for(int j=startY; j<maxY && count<32; j++) {
 	           int rgb = img.getRGB(i, j), bit = getBitValue(n, count);
 	          rgb = setBitValue(rgb, storageBit, bit);
 	           img.setRGB(i, j, rgb);
 	           count++;
 	           }
 	        }
 	     }
 	  
 	  private void embedByte(BufferedImage img, byte b, int start, int storageBit) {
 	     int maxX = img.getWidth(), maxY = img.getHeight(), 
 	        startX = start/maxY, startY = start - startX*maxY, count=0;
 	     for(int i=startX; i<maxX && count<8; i++) {
 	        for(int j=startY; j<maxY && count<8; j++) {
 	           int rgb = img.getRGB(i, j), bit = getBitValue(b, count);
 	           rgb = setBitValue(rgb, storageBit, bit);
 	          img.setRGB(i, j, rgb);
 	           count++;
 	           }
        }
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

