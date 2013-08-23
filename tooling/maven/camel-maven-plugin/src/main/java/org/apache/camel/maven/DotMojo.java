begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|artifact
operator|.
name|DependencyResolutionRequiredException
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
name|plugin
operator|.
name|MojoExecutionException
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
name|plugin
operator|.
name|logging
operator|.
name|Log
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
name|cli
operator|.
name|CommandLineException
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
name|cli
operator|.
name|CommandLineUtils
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
name|cli
operator|.
name|Commandline
import|;
end_import

begin_comment
comment|/**  * Runs Camel embedded with META-INF/services/*.xml spring files to try create  * DOT files for the routing rules, then converts the DOT files into another  * format such as PNG  *  * @version   * @goal dot  * @requiresDependencyResolution compile+runtime  * @phase prepare-package  * @execute phase="test-compile"  * @see<a href="http://www.graphviz.org/">GraphViz</a>  */
end_comment

begin_class
DECL|class|DotMojo
specifier|public
class|class
name|DotMojo
extends|extends
name|AbstractMavenReport
block|{
DECL|field|DEFAULT_GRAPHVIZ_OUTPUT_TYPES
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|DEFAULT_GRAPHVIZ_OUTPUT_TYPES
init|=
block|{
literal|"png"
block|,
literal|"svg"
block|,
literal|"cmapx"
block|}
decl_stmt|;
comment|/**      * Subdirectory for report.      */
DECL|field|SUBDIRECTORY
specifier|protected
specifier|static
specifier|final
name|String
name|SUBDIRECTORY
init|=
literal|"cameldoc"
decl_stmt|;
comment|//
comment|// For running Camel embedded
comment|// -------------------------------------------------------------------------
comment|//
comment|/**      * The duration to run the application for which by default is in      * milliseconds. A value<= 0 will run forever.      * Adding a s indicates seconds - eg "5s" means 5 seconds.      *      * @parameter property="2s"      */
DECL|field|duration
specifier|protected
name|String
name|duration
decl_stmt|;
comment|/**      * Whether we should boot up camel with the META-INF/services/*.xml to      * generate the DOT file      *      * @parameter property="true"      */
DECL|field|runCamel
specifier|protected
name|boolean
name|runCamel
decl_stmt|;
comment|/**      * Should we try run the DOT executable on the generated .DOT file to      * generate images      *      * @parameter property="true"      */
DECL|field|useDot
specifier|protected
name|boolean
name|useDot
decl_stmt|;
comment|/**      * The classpath based application context uri that spring wants to get.      *      * @parameter property="camel.applicationContextUri"      */
DECL|field|applicationContextUri
specifier|protected
name|String
name|applicationContextUri
decl_stmt|;
comment|/**      * The filesystem based application context uri that spring wants to get.      *      * @parameter property="camel.fileApplicationContextUri"      */
DECL|field|fileApplicationContextUri
specifier|protected
name|String
name|fileApplicationContextUri
decl_stmt|;
comment|/**      * The main class to execute.      *      * @parameter property="camel.mainClass"      *            default-value="org.apache.camel.spring.Main"      * @required      */
DECL|field|mainClass
specifier|private
name|String
name|mainClass
decl_stmt|;
comment|/**      * Reference to Maven 2 Project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|private
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Base output directory.      *      * @parameter property="project.build.directory"      * @required      */
DECL|field|buildDirectory
specifier|private
name|File
name|buildDirectory
decl_stmt|;
comment|/**      * Base output directory for reports.      *      * @parameter default-value="${project.build.directory}/site/cameldoc"      * @required      */
DECL|field|outputDirectory
specifier|private
name|File
name|outputDirectory
decl_stmt|;
comment|/**      * In the case of multiple camel contexts, setting aggregate == true will      * aggregate all into a monolithic context, otherwise they will be processed      * independently.      *      * @parameter      */
DECL|field|aggregate
specifier|private
name|String
name|aggregate
decl_stmt|;
comment|/**      * GraphViz executable location; visualization (images) will be generated      * only if you install this program and set this property to the executable      * dot (dot.exe on Win).      *      * @parameter property="dot"      */
DECL|field|executable
specifier|private
name|String
name|executable
decl_stmt|;
comment|/**      * Graphviz output types. Default is png. Possible values: png, jpg, gif,      * svg.      *      * @required      */
DECL|field|graphvizOutputType
specifier|private
name|String
name|graphvizOutputType
decl_stmt|;
comment|/**      * Graphviz output types. Possible values: png, jpg, gif, svg.      *      * @parameter      */
DECL|field|graphvizOutputTypes
specifier|private
name|String
index|[]
name|graphvizOutputTypes
decl_stmt|;
comment|/**      * Doxia SiteRender.      *      * @component      */
DECL|field|renderer
specifier|private
name|Renderer
name|renderer
decl_stmt|;
DECL|field|indexHtmlContent
specifier|private
name|String
name|indexHtmlContent
decl_stmt|;
comment|/**      * @param locale report locale.      * @return report description.      * @see org.apache.maven.reporting.MavenReport#getDescription(Locale)      */
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
literal|"report.dot.description"
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.maven.reporting.MavenReport#getName(Locale)      */
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
literal|"report.dot.name"
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
name|SUBDIRECTORY
operator|+
literal|"/index"
return|;
block|}
DECL|method|getAggregate ()
specifier|public
name|String
name|getAggregate
parameter_list|()
block|{
return|return
name|aggregate
return|;
block|}
DECL|method|setAggregate (String aggregate)
specifier|public
name|void
name|setAggregate
parameter_list|(
name|String
name|aggregate
parameter_list|)
block|{
name|this
operator|.
name|aggregate
operator|=
name|aggregate
expr_stmt|;
block|}
DECL|method|isUseDot ()
specifier|public
name|boolean
name|isUseDot
parameter_list|()
block|{
return|return
name|useDot
return|;
block|}
DECL|method|setUseDot (boolean useDot)
specifier|public
name|void
name|setUseDot
parameter_list|(
name|boolean
name|useDot
parameter_list|)
block|{
name|this
operator|.
name|useDot
operator|=
name|useDot
expr_stmt|;
block|}
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|this
operator|.
name|execute
argument_list|(
name|this
operator|.
name|buildDirectory
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|writeIndexHtmlFile
argument_list|(
name|outputDirectory
argument_list|,
literal|"index.html"
argument_list|,
name|indexHtmlContent
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
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
name|this
operator|.
name|execute
argument_list|(
name|this
operator|.
name|outputDirectory
argument_list|,
name|locale
argument_list|)
expr_stmt|;
name|Sink
name|kitchenSink
init|=
name|getSink
argument_list|()
decl_stmt|;
if|if
condition|(
name|kitchenSink
operator|!=
literal|null
condition|)
block|{
name|kitchenSink
operator|.
name|rawText
argument_list|(
name|indexHtmlContent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writeIndexHtmlFile
argument_list|(
name|outputDirectory
argument_list|,
literal|"index.html"
argument_list|,
name|indexHtmlContent
argument_list|)
expr_stmt|;
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
comment|/**      * Executes DOT generator.      *      * @param outputDir report output directory.      * @param locale report locale.      * @throws MojoExecutionException if there were any execution errors.      */
DECL|method|execute (final File outputDir, final Locale locale)
specifier|protected
name|void
name|execute
parameter_list|(
specifier|final
name|File
name|outputDir
parameter_list|,
specifier|final
name|Locale
name|locale
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|runCamelEmbedded
argument_list|(
name|outputDir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DependencyResolutionRequiredException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|outputDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|appendFiles
argument_list|(
name|files
argument_list|,
name|outputDirectory
argument_list|)
expr_stmt|;
if|if
condition|(
name|graphvizOutputTypes
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|graphvizOutputType
operator|==
literal|null
condition|)
block|{
name|graphvizOutputTypes
operator|=
name|DEFAULT_GRAPHVIZ_OUTPUT_TYPES
expr_stmt|;
block|}
else|else
block|{
name|graphvizOutputTypes
operator|=
operator|new
name|String
index|[]
block|{
name|graphvizOutputType
block|}
expr_stmt|;
block|}
block|}
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|contextNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|String
name|contextName
init|=
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|contextNames
operator|.
name|add
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
block|}
name|boolean
name|multipleCamelContexts
init|=
name|contextNames
operator|.
name|size
argument_list|()
operator|>
literal|1
decl_stmt|;
name|int
name|size
init|=
name|files
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|File
name|file
init|=
name|files
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|contextName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|multipleCamelContexts
condition|)
block|{
name|contextName
operator|=
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Generating contextName: "
operator|+
name|contextName
operator|+
literal|" file: "
operator|+
name|file
operator|+
literal|""
argument_list|)
expr_stmt|;
name|generate
argument_list|(
name|i
argument_list|,
name|file
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|multipleCamelContexts
condition|)
block|{
comment|// lets generate an index page which lists each indiviual
comment|// CamelContext file
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<h1>Camel Contexts</h1>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<ul>"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|contextName
range|:
name|contextNames
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"<li><a href='"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"/routes.html'>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</a></li>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</ul>"
argument_list|)
expr_stmt|;
name|indexHtmlContent
operator|=
name|buffer
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CommandLineException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|generate (int index, File file, String contextName)
specifier|private
name|void
name|generate
parameter_list|(
name|int
name|index
parameter_list|,
name|File
name|file
parameter_list|,
name|String
name|contextName
parameter_list|)
throws|throws
name|CommandLineException
throws|,
name|MojoExecutionException
throws|,
name|IOException
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|printHtmlHeader
argument_list|(
name|out
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|printHtmlFileHeader
argument_list|(
name|out
argument_list|,
name|file
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|format
range|:
name|graphvizOutputTypes
control|)
block|{
name|String
name|generated
init|=
name|convertFile
argument_list|(
name|file
argument_list|,
name|format
argument_list|)
decl_stmt|;
if|if
condition|(
name|format
operator|.
name|equals
argument_list|(
literal|"cmapx"
argument_list|)
operator|&&
name|generated
operator|!=
literal|null
condition|)
block|{
comment|// lets include the generated file inside the html
name|addFileToBuffer
argument_list|(
name|out
argument_list|,
operator|new
name|File
argument_list|(
name|generated
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|printHtmlFileFooter
argument_list|(
name|out
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|printHtmlFooter
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|String
name|content
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"routes.dot"
argument_list|)
operator|||
name|index
operator|==
literal|0
condition|)
block|{
name|indexHtmlContent
operator|=
name|content
expr_stmt|;
block|}
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|name
operator|+=
literal|".html"
expr_stmt|;
block|}
name|writeIndexHtmlFile
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|name
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
DECL|method|runCamelEmbedded (File outputDir)
specifier|protected
name|void
name|runCamelEmbedded
parameter_list|(
name|File
name|outputDir
parameter_list|)
throws|throws
name|DependencyResolutionRequiredException
block|{
if|if
condition|(
name|runCamel
condition|)
block|{
comment|// default path, but can be overridden by configuration
if|if
condition|(
name|applicationContextUri
operator|!=
literal|null
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Running Camel embedded to load Spring XML files from classpath: "
operator|+
name|applicationContextUri
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fileApplicationContextUri
operator|!=
literal|null
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Running Camel embedded to load Spring XML files from file path: "
operator|+
name|fileApplicationContextUri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Running Camel embedded to load Spring XML files from default path: META-INF/spring/*.xml"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|project
operator|.
name|getTestClasspathElements
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Using classpath: "
operator|+
name|list
argument_list|)
expr_stmt|;
name|EmbeddedMojo
name|mojo
init|=
operator|new
name|EmbeddedMojo
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|setClasspathElements
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|setDotEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|setMainClass
argument_list|(
name|mainClass
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|getAggregate
argument_list|()
argument_list|)
condition|)
block|{
name|mojo
operator|.
name|setDotAggregationEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|mojo
operator|.
name|setOutputDirectory
argument_list|(
name|outputDirectory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|setDuration
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|setLog
argument_list|(
name|getLog
argument_list|()
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|setPluginContext
argument_list|(
name|getPluginContext
argument_list|()
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|setApplicationContextUri
argument_list|(
name|applicationContextUri
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|setFileApplicationContextUri
argument_list|(
name|fileApplicationContextUri
argument_list|)
expr_stmt|;
try|try
block|{
name|mojo
operator|.
name|executeWithoutWrapping
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Failed to run Camel embedded: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|writeIndexHtmlFile (File dir, String fileName, String content)
specifier|protected
name|void
name|writeIndexHtmlFile
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|fileName
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
comment|// File dir = outputDirectory;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|html
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|fileName
argument_list|)
decl_stmt|;
name|PrintWriter
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|out
operator|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|html
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<html>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<head>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</head>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<body>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|"<p>No EIP diagrams available</p>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|write
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</body>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</html>"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|String
name|description
init|=
literal|"Failed to close html output file"
decl_stmt|;
name|close
argument_list|(
name|out
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|printHtmlHeader (PrintWriter out, String contextName)
specifier|protected
name|void
name|printHtmlHeader
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|String
name|contextName
parameter_list|)
block|{
if|if
condition|(
name|contextName
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<h1>EIP Patterns for CamelContext: "
operator|+
name|contextName
operator|+
literal|"</h1>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<h1>Camel EIP Patterns</h1>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
DECL|method|printHtmlFileHeader (PrintWriter out, File file)
specifier|protected
name|void
name|printHtmlFileHeader
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<p>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<img src='"
operator|+
name|removeFileExtension
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
operator|+
literal|".png' usemap='#CamelRoutes'>"
argument_list|)
expr_stmt|;
block|}
DECL|method|printHtmlFileFooter (PrintWriter out, File file)
specifier|protected
name|void
name|printHtmlFileFooter
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"</img>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</p>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
DECL|method|printHtmlFooter (PrintWriter out)
specifier|protected
name|void
name|printHtmlFooter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
DECL|method|close (Closeable closeable, String description)
specifier|protected
name|void
name|close
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
if|if
condition|(
name|closeable
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|closeable
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
name|description
operator|+
literal|": "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|convertFile (File file, String format)
specifier|protected
name|String
name|convertFile
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|format
parameter_list|)
throws|throws
name|CommandLineException
block|{
name|Log
name|log
init|=
name|getLog
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|useDot
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"DOT generation disabled."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
if|if
condition|(
name|dotHelpExitCode
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"'dot -?' execution failed so DOT generation disabled."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|this
operator|.
name|executable
operator|==
literal|null
operator|||
name|this
operator|.
name|executable
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Parameter<executable/> was not set in the pom.xml.  Skipping conversion."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|generatedFileName
init|=
name|removeFileExtension
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
operator|+
literal|"."
operator|+
name|format
decl_stmt|;
name|Commandline
name|cl
init|=
operator|new
name|Commandline
argument_list|()
decl_stmt|;
name|cl
operator|.
name|setExecutable
argument_list|(
name|executable
argument_list|)
expr_stmt|;
name|cl
operator|.
name|createArg
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"-T"
operator|+
name|format
argument_list|)
expr_stmt|;
name|cl
operator|.
name|createArg
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"-o"
argument_list|)
expr_stmt|;
name|cl
operator|.
name|createArg
argument_list|()
operator|.
name|setValue
argument_list|(
name|generatedFileName
argument_list|)
expr_stmt|;
name|cl
operator|.
name|createArg
argument_list|()
operator|.
name|setValue
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"executing: "
operator|+
name|cl
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|CommandLineUtils
operator|.
name|StringStreamConsumer
name|stdout
init|=
operator|new
name|CommandLineUtils
operator|.
name|StringStreamConsumer
argument_list|()
decl_stmt|;
name|CommandLineUtils
operator|.
name|StringStreamConsumer
name|stderr
init|=
operator|new
name|CommandLineUtils
operator|.
name|StringStreamConsumer
argument_list|()
decl_stmt|;
name|CommandLineUtils
operator|.
name|executeCommandLine
argument_list|(
name|cl
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|stdout
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|output
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
name|String
name|errOutput
init|=
name|stderr
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|errOutput
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|errOutput
argument_list|)
expr_stmt|;
block|}
return|return
name|generatedFileName
return|;
block|}
DECL|method|dotHelpExitCode ()
specifier|private
name|int
name|dotHelpExitCode
parameter_list|()
throws|throws
name|CommandLineException
block|{
name|Commandline
name|cl
init|=
operator|new
name|Commandline
argument_list|()
decl_stmt|;
name|cl
operator|.
name|setExecutable
argument_list|(
name|executable
argument_list|)
expr_stmt|;
name|cl
operator|.
name|createArg
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"-?"
argument_list|)
expr_stmt|;
name|CommandLineUtils
operator|.
name|StringStreamConsumer
name|stdout
init|=
operator|new
name|CommandLineUtils
operator|.
name|StringStreamConsumer
argument_list|()
decl_stmt|;
name|CommandLineUtils
operator|.
name|StringStreamConsumer
name|stderr
init|=
operator|new
name|CommandLineUtils
operator|.
name|StringStreamConsumer
argument_list|()
decl_stmt|;
return|return
name|CommandLineUtils
operator|.
name|executeCommandLine
argument_list|(
name|cl
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|)
return|;
block|}
DECL|method|removeFileExtension (String name)
specifier|protected
name|String
name|removeFileExtension
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|name
return|;
block|}
block|}
DECL|method|appendFiles (List<File> output, File file)
specifier|private
name|void
name|appendFiles
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|output
parameter_list|,
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|appendDirectory
argument_list|(
name|output
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|isValid
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|output
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|appendDirectory (List<File> output, File dir)
specifier|private
name|void
name|appendDirectory
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|output
parameter_list|,
name|File
name|dir
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|appendFiles
argument_list|(
name|output
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isValid (File file)
specifier|private
name|boolean
name|isValid
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|endsWith
argument_list|(
literal|".dot"
argument_list|)
return|;
block|}
DECL|method|addFileToBuffer (PrintWriter out, File file)
specifier|private
name|void
name|addFileToBuffer
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|BufferedReader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reader
operator|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|reader
argument_list|,
literal|"cmapx file"
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"camel-maven-plugin"
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
block|}
end_class

end_unit

