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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactoryConfigurationError
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NamedNodeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|tidy
operator|.
name|Tidy
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
name|AbstractMojo
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
name|project
operator|.
name|MavenProjectHelper
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
name|StreamConsumer
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
operator|.
name|Argument
import|;
end_import

begin_comment
comment|/**  * Goal which extracts the content div from the html page and converts to PDF  * using Prince  *   * @goal compile  * @phase compile  */
end_comment

begin_class
DECL|class|ConfluenceToPDFMojo
specifier|public
class|class
name|ConfluenceToPDFMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The URL to the confluence page to convert.      *       * @parameter expression="${page}"      *            default-value="http://cwiki.apache.org/confluence/display/CAMEL/Index"      * @required      */
DECL|field|page
specifier|private
name|String
name|page
decl_stmt|;
comment|/**      * The output file name for the pdf.      *       * @parameter expression="${pdf}"      *            default-value="${project.build.directory}/site/manual/${project.artifactId}-${project.version}.pdf"      */
DECL|field|pdf
specifier|private
name|String
name|pdf
decl_stmt|;
comment|/**      * The css style sheets that should be linked.      *       * @parameter      */
DECL|field|styleSheets
specifier|private
name|String
index|[]
name|styleSheets
decl_stmt|;
comment|/**      * Content that should be added in the head element of the html file.      *       * @parameter      */
DECL|field|head
specifier|private
name|String
name|head
decl_stmt|;
comment|/**      * The first div with who's class matches the contentDivClass will be      * assumed to be the content section of the HTML and is what will be used as      * the content in the PDF.      *       * @parameter default-value="wiki-content"      */
DECL|field|contentDivClass
specifier|private
name|String
name|contentDivClass
init|=
literal|"wiki-content"
decl_stmt|;
comment|/**      * Arguments that should be passed to the prince html to pdf processor.      *       * @parameter      */
DECL|field|princeArgs
specifier|private
name|String
index|[]
name|princeArgs
decl_stmt|;
comment|/**      * If there is an error converting the HTML to PDF should the build fail?      * default to false since this requires the prince tool to be installed and      * on the PATH of the system.      *       * @parameter default-value="false"      */
DECL|field|errorOnConverionFailure
specifier|private
name|boolean
name|errorOnConverionFailure
decl_stmt|;
comment|/**      * If there is an error downloading the HTML should the build fail? default      * to false since this usually requires the user to be online.      *       * @parameter default-value="false"      */
DECL|field|errorOnDownloadFailure
specifier|private
name|boolean
name|errorOnDownloadFailure
decl_stmt|;
comment|/**      * The maven project.      *       * @parameter expression="${project}"      * @required      * @readonly      */
DECL|field|project
specifier|private
name|MavenProject
name|project
decl_stmt|;
comment|/**      * @component      */
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * The type used when attaching the artifact to the deployment.      *       * @parameter default-value="pdf"      */
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
comment|/**      * Classifier to add to the artifact generated.      *       * @parameter      */
DECL|field|classifier
specifier|private
name|String
name|classifier
decl_stmt|;
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|File
name|outputDir
init|=
operator|new
name|File
argument_list|(
name|pdf
argument_list|)
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|outputDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|outputDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
try|try
block|{
comment|// Download
name|String
name|content
init|=
name|downloadContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// Store
name|storeHTMLFile
argument_list|(
name|content
argument_list|)
expr_stmt|;
comment|// Run Prince
if|if
condition|(
name|convert
argument_list|()
operator|==
literal|0
condition|)
block|{
name|File
name|pdfFile
init|=
operator|new
name|File
argument_list|(
name|getPDFFileName
argument_list|()
argument_list|)
decl_stmt|;
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|project
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|,
name|pdfFile
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Download of '"
operator|+
name|page
operator|+
literal|"' failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|convert ()
specifier|private
name|int
name|convert
parameter_list|()
throws|throws
name|CommandLineException
throws|,
name|MojoExecutionException
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Converting to PDF with prince..."
argument_list|)
expr_stmt|;
name|Commandline
name|cl
init|=
operator|new
name|Commandline
argument_list|(
literal|"prince"
argument_list|)
decl_stmt|;
name|Argument
name|arg
decl_stmt|;
if|if
condition|(
name|princeArgs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|princeArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|arg
operator|=
operator|new
name|Argument
argument_list|()
expr_stmt|;
name|arg
operator|.
name|setValue
argument_list|(
name|princeArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|cl
operator|.
name|addArg
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
block|}
name|arg
operator|=
operator|new
name|Argument
argument_list|()
expr_stmt|;
name|arg
operator|.
name|setValue
argument_list|(
name|getHTMLFileName
argument_list|()
argument_list|)
expr_stmt|;
name|cl
operator|.
name|addArg
argument_list|(
name|arg
argument_list|)
expr_stmt|;
name|arg
operator|=
operator|new
name|Argument
argument_list|()
expr_stmt|;
name|arg
operator|.
name|setValue
argument_list|(
name|getPDFFileName
argument_list|()
argument_list|)
expr_stmt|;
name|cl
operator|.
name|addArg
argument_list|(
name|arg
argument_list|)
expr_stmt|;
name|StreamConsumer
name|out
init|=
operator|new
name|StreamConsumer
argument_list|()
block|{
specifier|public
name|void
name|consumeLine
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"[prince] "
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|int
name|rc
init|=
name|CommandLineUtils
operator|.
name|executeCommandLine
argument_list|(
name|cl
argument_list|,
name|out
argument_list|,
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|==
literal|0
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Stored: "
operator|+
name|getPDFFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|errorOnConverionFailure
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"PDF Conversion failed rc="
operator|+
name|rc
argument_list|)
throw|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"PDF Conversion failed due to return code: "
operator|+
name|rc
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|rc
return|;
block|}
DECL|method|getPDFFileName ()
specifier|private
name|String
name|getPDFFileName
parameter_list|()
block|{
return|return
name|pdf
return|;
block|}
DECL|method|storeHTMLFile (String content)
specifier|private
name|void
name|storeHTMLFile
parameter_list|(
name|String
name|content
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|getHTMLFileName
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
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
literal|"<base href=\""
operator|+
name|page
operator|+
literal|"\"/>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|head
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|head
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|styleSheets
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|styleSheets
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<link href=\""
operator|+
name|styleSheets
index|[
name|i
index|]
operator|+
literal|"\" rel=\"stylesheet\" type=\"text/css\"/>"
argument_list|)
expr_stmt|;
block|}
block|}
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
operator|+
name|content
operator|+
literal|"</body>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Stored: "
operator|+
name|getHTMLFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getHTMLFileName ()
specifier|private
name|String
name|getHTMLFileName
parameter_list|()
block|{
name|String
name|name
init|=
name|getPDFFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".pdf"
argument_list|)
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
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
return|return
name|name
operator|+
literal|".html"
return|;
block|}
DECL|method|downloadContent ()
specifier|private
name|String
name|downloadContent
parameter_list|()
throws|throws
name|IOException
throws|,
name|TransformerFactoryConfigurationError
throws|,
name|TransformerException
throws|,
name|MojoExecutionException
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Downloading: "
operator|+
name|page
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|Tidy
name|tidy
init|=
operator|new
name|Tidy
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|result
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|tidy
operator|.
name|setErrout
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|doc
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|tidy
operator|.
name|parseDOM
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|errorOnDownloadFailure
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
operator|new
name|String
argument_list|(
name|result
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Download or validation of '"
operator|+
name|page
operator|+
literal|"' failed: "
operator|+
name|e
argument_list|)
throw|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
operator|new
name|String
argument_list|(
name|result
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Download or validation of '"
operator|+
name|page
operator|+
literal|"' failed: "
operator|+
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
name|NodeList
name|nodeList
init|=
name|doc
operator|.
name|getElementsByTagName
argument_list|(
literal|"div"
argument_list|)
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
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|Node
name|node
init|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|NamedNodeMap
name|nm
init|=
name|node
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Node
name|attr
init|=
name|nm
operator|.
name|getNamedItem
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|!=
literal|null
operator|&&
name|attr
operator|.
name|getNodeValue
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|contentDivClass
argument_list|)
condition|)
block|{
comment|// Write the wiki-content div to the content variable.
name|ByteArrayOutputStream
name|contentData
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|1024
operator|*
literal|100
argument_list|)
decl_stmt|;
name|TransformerFactory
name|tFactory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Transformer
name|transformer
init|=
name|tFactory
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|contentData
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|contentData
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|content
operator|=
name|content
operator|.
name|substring
argument_list|(
name|content
operator|.
name|indexOf
argument_list|(
literal|"<div"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|content
return|;
block|}
block|}
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"The '"
operator|+
name|page
operator|+
literal|"' page did not have a<div class=\""
operator|+
name|contentDivClass
operator|+
literal|"\"> element."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

