
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


@WebServlet("/decryptaes")
@SuppressWarnings("serial")
		public class decryptaes extends HttpServlet {
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
		              
		              decrypt(file);
		              
		              
		             // out.print(mess);
		             
		               
		            }
		         }response.sendRedirect("message.html");
		         out.println("</body>");
		         out.println("</html>");
		         } catch(Exception ex) {
		            System.out.println(ex);
		         }
		      }
		
		 
		   
		   
		   public void decrypt(File f1) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
		   Cipher cpr =Cipher.getInstance("AES");
			
			KeyGenerator keygen =KeyGenerator.getInstance("AES");
			byte[] key1="sonalidashcse2b4".getBytes();
			
			  SecretKeySpec key = new SecretKeySpec(key1, "AES");
			
			//Key key=keygen.generateKey();
			
			cpr.init(Cipher.DECRYPT_MODE, key);
			
			CipherInputStream cprin=new CipherInputStream(new FileInputStream(f1),cpr);
			
			FileOutputStream fin=new FileOutputStream(new File("C:/Users/USER/Desktop/emb/aes_dec.png"));
			
			int i; 
			while((i=cprin.read())!=-1){
			fin.write(i);}
		  		   
}}