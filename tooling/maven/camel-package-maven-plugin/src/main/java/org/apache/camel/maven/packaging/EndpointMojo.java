begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilenameFilter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|doxia
operator|.
name|sink
operator|.
name|Sink
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|doxia
operator|.
name|siterenderer
operator|.
name|Renderer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|project
operator|.
name|MavenProject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|reporting
operator|.
name|AbstractMavenReport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|reporting
operator|.
name|MavenReportException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|IOUtil
import|;
end_import

begin_comment
comment|/**  * Generates a HTML report for all the Camel endpoints and their configuration parameters  *  * @goal endpoints  * @phase site  */
end_comment

begin_class
DECL|class|EndpointMojo
specifier|public
class|class
name|EndpointMojo
extends|extends
name|AbstractMavenReport
block|{
comment|/**      * Reference to Maven Project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|private
name|MavenProject
name|project
decl_stmt|;
comment|/**      * @component      * @required      * @readonly      */
DECL|field|renderer
specifier|private
name|Renderer
name|renderer
decl_stmt|;
comment|/**      * Base output directory for reports.      *      * @parameter default-value="${project.build.directory}/site"      * @required      */
DECL|field|outputDirectory
specifier|private
name|File
name|outputDirectory
decl_stmt|;
DECL|method|executeReport (final Locale locale)
specifier|protected
name|void
name|executeReport
parameter_list|(
specifier|final
name|Locale
name|locale
parameter_list|)
throws|throws
name|MavenReportException
block|{
try|try
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getOutputDirectory
argument_list|()
argument_list|,
literal|"org/apache/camel/component"
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Looking into directory "
operator|+
name|dir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|dir
operator|.
name|exists
argument_list|()
operator|&&
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".html"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
operator|&&
name|files
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|boolean
name|showIndex
init|=
name|files
operator|.
name|length
operator|>
literal|1
decl_stmt|;
name|Sink
name|sink
init|=
name|getSink
argument_list|()
decl_stmt|;
if|if
condition|(
name|sink
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sink
operator|.
name|head
argument_list|()
expr_stmt|;
name|sink
operator|.
name|title
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
literal|"Camel Endpoints"
argument_list|)
expr_stmt|;
name|sink
operator|.
name|title_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|rawText
argument_list|(
literal|"<style>\n"
operator|+
literal|"th, td {\n"
operator|+
literal|"  text-align:left;\n\n"
operator|+
literal|"}\n"
operator|+
literal|"</style>\n"
argument_list|)
expr_stmt|;
name|sink
operator|.
name|head_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|body
argument_list|()
expr_stmt|;
name|sink
operator|.
name|section1
argument_list|()
expr_stmt|;
name|sink
operator|.
name|sectionTitle1
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
literal|"Camel Endpoints"
argument_list|)
expr_stmt|;
name|sink
operator|.
name|sectionTitle1_
argument_list|()
expr_stmt|;
if|if
condition|(
name|showIndex
condition|)
block|{
name|sink
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"found "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|linkName
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|linkName
operator|.
name|endsWith
argument_list|(
literal|".html"
argument_list|)
condition|)
block|{
name|linkName
operator|=
name|linkName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|linkName
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
expr_stmt|;
block|}
name|String
name|endpointHtml
init|=
name|IOUtil
operator|.
name|toString
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|endpointHtml
operator|=
name|extractBodyContents
argument_list|(
name|endpointHtml
argument_list|)
expr_stmt|;
if|if
condition|(
name|showIndex
condition|)
block|{
name|sink
operator|.
name|listItem
argument_list|()
expr_stmt|;
name|sink
operator|.
name|link
argument_list|(
literal|"#"
operator|+
name|linkName
argument_list|)
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|linkName
argument_list|)
expr_stmt|;
name|sink
operator|.
name|link_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|listItem_
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"<a name='"
operator|+
name|linkName
operator|+
literal|"'>\n"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|endpointHtml
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"</a>\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sink
operator|.
name|section1_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|section2
argument_list|()
expr_stmt|;
name|sink
operator|.
name|rawText
argument_list|(
name|endpointHtml
argument_list|)
expr_stmt|;
name|sink
operator|.
name|section2_
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|showIndex
condition|)
block|{
name|sink
operator|.
name|list_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|section1_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|section2
argument_list|()
expr_stmt|;
name|sink
operator|.
name|rawText
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sink
operator|.
name|section2_
argument_list|()
expr_stmt|;
block|}
name|sink
operator|.
name|body_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|flush
argument_list|()
expr_stmt|;
name|sink
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
specifier|final
name|MavenReportException
name|ex
init|=
operator|new
name|MavenReportException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
comment|/**      * Extracts the content between<body> and</body>      */
DECL|method|extractBodyContents (String html)
specifier|protected
name|String
name|extractBodyContents
parameter_list|(
name|String
name|html
parameter_list|)
block|{
name|String
name|body
init|=
literal|"<body>"
decl_stmt|;
name|int
name|idx
init|=
name|html
operator|.
name|indexOf
argument_list|(
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|html
operator|=
name|html
operator|.
name|substring
argument_list|(
name|idx
operator|+
name|body
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|idx
operator|=
name|html
operator|.
name|lastIndexOf
argument_list|(
literal|"</body>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|html
operator|=
name|html
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|html
return|;
block|}
comment|/*     public void execute() throws MojoExecutionException {         this.execute(this.buildDirectory, Locale.getDefault());         try {             writeIndexHtmlFile(outputDirectory, "index.html", indexHtmlContent);         } catch (IOException e) {             throw new MojoExecutionException("Failed: " + e, e);         }     }      */
comment|/**  * Executes DOT generator.  *  * @param outputDir report output directory.  * @param locale report locale.  * @throws org.apache.maven.plugin.MojoExecutionException if there were any execution errors.  */
comment|/*      protected void execute(final File outputDir, final Locale locale) throws MojoExecutionException {         outputDir.mkdirs();          List<File> files = new ArrayList<File>();         appendFiles(files, outputDirectory);          try {             Set<String> contextNames = new HashSet<String>();             for (File file : files) {                 String contextName = file.getParentFile().getName();                 contextNames.add(contextName);             }              boolean multipleCamelContexts = contextNames.size()> 1;             int size = files.size();             for (int i = 0; i< size; i++) {                 File file = files.get(i);                 String contextName = null;                 if (multipleCamelContexts) {                     contextName = file.getParentFile().getName();                 }                  getLog().info("Generating contextName: " + contextName + " file: " + file + "");                  generate(i, file, contextName);             }              if (multipleCamelContexts) {                 // lets generate an index page which lists each indiviual                 // CamelContext file                 StringWriter buffer = new StringWriter();                 PrintWriter out = new PrintWriter(buffer);                  out.println("<h1>Camel Contexts</h1>");                 out.println();                  out.println("<ul>");                 for (String contextName : contextNames) {                     out.print("<li><a href='");                     out.print(contextName);                     out.print("/routes.html'>");                     out.print(contextName);                     out.println("</a></li>");                 }                 out.println("</ul>");                 indexHtmlContent = buffer.toString();             }         } catch (CommandLineException e) {             throw new MojoExecutionException("Failed: " + e, e);         } catch (IOException e) {             throw new MojoExecutionException("Failed: " + e, e);         }     }      private void generate(int index, File file, String contextName) throws CommandLineException,         MojoExecutionException, IOException {          StringWriter buffer = new StringWriter();         PrintWriter out = new PrintWriter(buffer);         printHtmlHeader(out, contextName);         printHtmlFileHeader(out, file);         for (String format : graphvizOutputTypes) {             String generated = convertFile(file, format);              if (format.equals("cmapx")&& generated != null) {                 // lets include the generated file inside the html                 addFileToBuffer(out, new File(generated));             }         }         printHtmlFileFooter(out, file);         printHtmlFooter(out);          String content = buffer.toString();         String name = file.getName();         if (name.equalsIgnoreCase("routes.dot") || index == 0) {             indexHtmlContent = content;         }         int idx = name.lastIndexOf(".");         if (idx>= 0) {             name = name.substring(0, idx);             name += ".html";         }         writeIndexHtmlFile(file.getParentFile(), name, content);     }      protected void writeIndexHtmlFile(File dir, String fileName, String content) throws IOException {         // File dir = outputDirectory;         dir.mkdirs();         File html = new File(dir, fileName);         PrintWriter out = null;         try {             out = new PrintWriter(new FileWriter(html));             out.println("<html>");             out.println("<head>");             out.println("</head>");             out.println("<body>");             out.println();             if (content == null) {                 out.write("<p>No EIP diagrams available</p>");             } else {                 out.write(content);             }             out.println("</body>");             out.println("</html>");         } finally {             String description = "Failed to close html output file";             close(out, description);         }     }      protected void printHtmlHeader(PrintWriter out, String contextName) {         if (contextName != null) {             out.println("<h1>EIP Patterns for CamelContext: " + contextName + "</h1>");         } else {             out.println("<h1>Camel EIP Patterns</h1>");         }         out.println();     }      protected void printHtmlFileHeader(PrintWriter out, File file) {         out.println("<p>");         out.println("<img src='" + removeFileExtension(file.getName()) + ".png' usemap='#CamelRoutes'>");     }      protected void printHtmlFileFooter(PrintWriter out, File file) {         out.println("</img>");         out.println("</p>");         out.println();     }      protected void printHtmlFooter(PrintWriter out) {         out.println();     }      protected void close(Closeable closeable, String description) {         if (closeable != null) {             try {                 closeable.close();             } catch (IOException e) {                 getLog().warn(description + ": " + e);             }         }     }      protected String convertFile(File file, String format) throws CommandLineException {         Log log = getLog();         if (!useDot) {             log.info("DOT generation disabled.");             return null;         } else {             if (dotHelpExitCode() != 0) {                 log.info("'dot -?' execution failed so DOT generation disabled.");                 return null;             }         }         if (this.executable == null || this.executable.length() == 0) {             log.warn("Parameter<executable/> was not set in the pom.xml.  Skipping conversion.");             return null;         }          String generatedFileName = removeFileExtension(file.getAbsolutePath()) + "." + format;         Commandline cl = new Commandline();         cl.setExecutable(executable);         cl.createArg().setValue("-T" + format);         cl.createArg().setValue("-o");         cl.createArg().setValue(generatedFileName);         cl.createArg().setValue(file.getAbsolutePath());          log.debug("executing: " + cl.toString());          CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();          CommandLineUtils.executeCommandLine(cl, stdout, stderr);          String output = stdout.getOutput();         if (output.length()> 0) {             log.debug(output);         }         String errOutput = stderr.getOutput();         if (errOutput.length()> 0) {             log.warn(errOutput);         }         return generatedFileName;     }      private int dotHelpExitCode() throws CommandLineException {         Commandline cl = new Commandline();         cl.setExecutable(executable);         cl.createArg().setValue("-?");          CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();          return CommandLineUtils.executeCommandLine(cl, stdout, stderr);     }      protected String removeFileExtension(String name) {         int idx = name.lastIndexOf(".");         if (idx> 0) {             return name.substring(0, idx);         } else {             return name;         }     }      private void appendFiles(List<File> output, File file) {         if (file.isDirectory()) {             appendDirectory(output, file);         } else {             if (isValid(file)) {                 output.add(file);             }         }     }      private void appendDirectory(List<File> output, File dir) {         File[] files = dir.listFiles();         for (File file : files) {             appendFiles(output, file);         }     }      private boolean isValid(File file) {         String name = file.getName().toLowerCase();         return name.endsWith(".dot");     }      private void addFileToBuffer(PrintWriter out, File file) throws MojoExecutionException {         BufferedReader reader = null;         try {             reader = IOHelper.buffered(new FileReader(file));             while (true) {                 String line = reader.readLine();                 if (line == null) {                     break;                 } else {                     out.println(line);                 }             }         } catch (IOException e) {             throw new MojoExecutionException("Failed: " + e, e);         } finally {             close(reader, "cmapx file");         }     } */
comment|/**      * Gets resource bundle for given locale.      *      * @param locale locale      * @return resource bundle      */
DECL|method|getBundle (final Locale locale)
specifier|protected
name|ResourceBundle
name|getBundle
parameter_list|(
specifier|final
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
literal|"camel-package-maven-plugin"
argument_list|,
name|locale
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getSiteRenderer ()
specifier|protected
name|Renderer
name|getSiteRenderer
parameter_list|()
block|{
return|return
name|this
operator|.
name|renderer
return|;
block|}
DECL|method|getOutputDirectory ()
specifier|protected
name|String
name|getOutputDirectory
parameter_list|()
block|{
return|return
name|this
operator|.
name|outputDirectory
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
DECL|method|getProject ()
specifier|protected
name|MavenProject
name|getProject
parameter_list|()
block|{
return|return
name|this
operator|.
name|project
return|;
block|}
DECL|method|getDescription (final Locale locale)
specifier|public
name|String
name|getDescription
parameter_list|(
specifier|final
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.endpoint.description"
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.maven.reporting.MavenReport#getName(java.util.Locale)      */
DECL|method|getName (final Locale locale)
specifier|public
name|String
name|getName
parameter_list|(
specifier|final
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.endpoint.name"
argument_list|)
return|;
block|}
DECL|method|getOutputName ()
specifier|public
name|String
name|getOutputName
parameter_list|()
block|{
return|return
literal|"camelEndpoints"
return|;
block|}
block|}
end_class

end_unit

