package helper;

import java.io.File;
import java.io.FilenameFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal compile-reports
 * 
 * @phase compile
 */
public class JasperMojo extends AbstractMojo {

    /**
     * The greeting to display.
     *
     * @parameter expression="${jasper.source.directory}" default-value="${basedir}/src/main/jasperreports"
     */
    private File sourceDirectory;

    /**
     * Location of the file.
     * 
     * @parameter expression="${jasper.output.directory}" default-value="${project.build.directory}/generated-jasper"
     */
    private File outputDirectory;

    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists())
        {
          System.out.println("creating directory: " + outputDirectory);
          boolean result = outputDirectory.mkdir();  
          if(result){    
             System.out.println("DIR created");  
           }

        }
        System.out.println("compiling file...");
        File[] jrxmlList = finder(sourceDirectory);
        for (File jrxml : jrxmlList) {
            try {
                String sourceFileName = jrxml.getPath();
                System.out.println(sourceFileName);
                String destFileName = outputDirectory.getPath() +"/"+ jrxml.getName().replace(".jrxml", ".jasper");

                JasperCompileManager.compileReportToFile(sourceFileName, destFileName);
            } catch (JRException e) {
                throw new MojoExecutionException("Error compiling file " + jrxml.getName(), e);
            }
        }
        System.out.println("compiling done!");
    }

    private File[] finder(File dir) {
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".jrxml");
            }
        });
    }

}
