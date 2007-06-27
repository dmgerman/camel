begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FileWriter
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
name|InputStream
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
name|Iterator
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
name|StringTokenizer
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
name|java
operator|.
name|net
operator|.
name|URLConnection
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|Element
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
name|NamedNodeMap
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
name|DOMElementImpl
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

begin_comment
comment|/**  * Goal which extracts the content of a wiki page and converts it to docbook  * format  *   * @goal htmlToDocbook  * @phase process-sources  */
end_comment

begin_class
DECL|class|GenerateDocBookMojo
specifier|public
class|class
name|GenerateDocBookMojo
extends|extends
name|AbstractMojo
block|{
comment|/** 	 * Base URL. 	 *  	 * @parameter expression="${baseURL}" 	 *            default-value="http://activemq.apache.org/camel/" 	 * @required 	 */
DECL|field|baseURL
specifier|private
name|String
name|baseURL
decl_stmt|;
comment|/** 	 * List of resources 	 *  	 * @parameter 	 */
DECL|field|resources
specifier|private
name|String
index|[]
name|resources
decl_stmt|;
comment|/** 	 * List of author's fullname 	 *  	 * @parameter 	 */
DECL|field|authors
specifier|private
name|String
index|[]
name|authors
decl_stmt|;
comment|/** 	 * Location of the xsl file. 	 *  	 * @parameter expression="${configDirectory}" 	 *            default-value="${basedir}/src/styles/docbook.xsl" 	 * @required 	 */
DECL|field|xslFile
specifier|private
name|String
name|xslFile
decl_stmt|;
comment|/** 	 * Location of the output directory. 	 *  	 * @parameter expression="${project.build.directory}/docbkx/docbkx-source" 	 */
DECL|field|outputPath
specifier|private
name|String
name|outputPath
decl_stmt|;
comment|/** 	 * Location of the output directory for wiki source. 	 *  	 * @parameter expression="${project.build.directory}/docbkx/wiki-source" 	 */
DECL|field|wikiOutputPath
specifier|private
name|String
name|wikiOutputPath
decl_stmt|;
comment|/** 	 * @parameter expression="${title}" 	 * @required 	 */
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
comment|/** 	 * @parameter expression="${subtitle}" 	 */
DECL|field|subtitle
specifier|private
name|String
name|subtitle
decl_stmt|;
comment|/** 	 * @parameter expression="${mainFilename}" default-value="manual" 	 * @required 	 */
DECL|field|mainFilename
specifier|private
name|String
name|mainFilename
decl_stmt|;
comment|/** 	 * @parameter expression="${version}" default-value="${project.version}" 	 */
DECL|field|version
specifier|private
name|String
name|version
decl_stmt|;
comment|/** 	 * @parameter expression="${legalNotice}" 	 */
DECL|field|legalNotice
specifier|private
name|String
name|legalNotice
decl_stmt|;
comment|/** 	 * Location of image files. 	 *  	 * @parameter expression="${project.build.directory}/site/book/images" 	 *             	 */
DECL|field|imageLocation
specifier|private
name|String
name|imageLocation
decl_stmt|;
DECL|field|chapterId
specifier|private
name|String
name|chapterId
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
name|outputPath
argument_list|)
decl_stmt|;
name|File
name|wikiOutputDir
init|=
operator|new
name|File
argument_list|(
name|wikiOutputPath
argument_list|)
decl_stmt|;
name|File
name|imageDir
init|=
operator|new
name|File
argument_list|(
name|imageLocation
argument_list|)
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
name|imageDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|wikiOutputDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|createMainXML
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|resources
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|this
operator|.
name|setChapterId
argument_list|(
name|removeExtension
argument_list|(
name|resources
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|process
argument_list|(
name|resources
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Extract the wiki content and tranform it into docbook format 	 *  	 * @param resource 	 */
DECL|method|process (String resource)
specifier|public
name|void
name|process
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|Tidy
name|tidy
init|=
operator|new
name|Tidy
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
literal|null
decl_stmt|;
name|BufferedOutputStream
name|output
init|=
literal|null
decl_stmt|;
name|BufferedOutputStream
name|wikiOutput
init|=
literal|null
decl_stmt|;
name|tidy
operator|.
name|setXmlOut
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|URL
name|u
init|=
operator|new
name|URL
argument_list|(
name|baseURL
operator|+
name|resource
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|tidy
operator|.
name|parseDOM
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
name|u
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// let's extract the div element with class="wiki-content
comment|// maincontent"
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
literal|"wiki-content maincontent"
argument_list|)
condition|)
block|{
name|downloadImages
argument_list|(
name|node
argument_list|)
expr_stmt|;
comment|// These attributes will be used by xsl to
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
name|element
operator|.
name|setAttribute
argument_list|(
literal|"chapterId"
argument_list|,
name|chapterId
argument_list|)
expr_stmt|;
name|element
operator|.
name|setAttribute
argument_list|(
literal|"baseURL"
argument_list|,
name|baseURL
argument_list|)
expr_stmt|;
name|element
operator|.
name|setAttribute
argument_list|(
literal|"imageLocation"
argument_list|,
literal|"../images/"
argument_list|)
expr_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|output
operator|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outputPath
operator|+
name|File
operator|.
name|separator
operator|+
name|removeExtension
argument_list|(
name|resource
argument_list|)
operator|+
literal|".xml"
argument_list|)
argument_list|)
expr_stmt|;
name|StreamResult
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|output
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
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|xslFile
argument_list|)
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// generate the wiki source for debugging
name|wikiOutput
operator|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|wikiOutputPath
operator|+
name|File
operator|.
name|separator
operator|+
name|removeExtension
argument_list|(
name|resource
argument_list|)
operator|+
literal|".html"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
operator|new
name|StreamResult
argument_list|(
name|wikiOutput
argument_list|)
expr_stmt|;
name|transformer
operator|=
name|tFactory
operator|.
name|newTransformer
argument_list|()
expr_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
name|output
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
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/* 	 *  create the main docbook xml file  	 */
DECL|method|createMainXML ()
specifier|public
name|void
name|createMainXML
parameter_list|()
block|{
try|try
block|{
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|outputPath
operator|+
name|File
operator|.
name|separator
operator|+
name|mainFilename
operator|+
literal|".xml"
argument_list|)
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<!DOCTYPE book PUBLIC \"-//OASIS//DTD DocBook XML V4.4//EN\" \"http://www.oasis-open.org/docbook/xml/4.4/docbookx.dtd\" "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"["
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|resources
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<!ENTITY "
operator|+
name|removeExtension
argument_list|(
name|resources
index|[
name|i
index|]
argument_list|)
operator|+
literal|" SYSTEM \""
operator|+
name|removeExtension
argument_list|(
name|resources
index|[
name|i
index|]
argument_list|)
operator|+
literal|".xml\">"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"]>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<book>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<bookinfo>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<title>"
operator|+
name|title
operator|+
literal|"</title>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<subtitle>"
operator|+
name|subtitle
operator|+
literal|"</subtitle>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<releaseinfo>"
operator|+
name|version
operator|+
literal|"</releaseinfo>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<authorgroup>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|authors
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
name|authors
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|StringTokenizer
name|name
init|=
operator|new
name|StringTokenizer
argument_list|(
name|authors
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|String
name|fname
init|=
name|name
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|String
name|lname
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|lname
operator|=
name|name
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"<author>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<firstname>"
operator|+
name|fname
operator|+
literal|"</firstname>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<surname>"
operator|+
name|lname
operator|+
literal|"</surname>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</author>"
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</authorgroup>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<legalnotice>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|legalNotice
operator|!=
literal|null
operator|&&
name|legalNotice
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<para>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|legalNotice
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</para>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<para>Licensed to the Apache Software Foundation (ASF) under one or more"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"contributor license agreements. See the NOTICE file distributed with"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"this work for additional information regarding copyright ownership. The"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"ASF licenses this file to You under the Apache License, Version 2.0 (the"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\"License\"); you may not use this file except in compliance with the"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"License. You may obtain a copy of the License at</para>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<para>http://www.apache.org/licenses/LICENSE-2.0</para>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<para>Unless required by applicable law or agreed to in writing,"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|" software distributed under the License is distributed on an \"AS IS\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"implied. See the License for the specific language governing permissions"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"and limitations under the License.</para>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</legalnotice>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</bookinfo>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<toc></toc>"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|resources
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"&"
operator|+
name|removeExtension
argument_list|(
name|resources
index|[
name|i
index|]
argument_list|)
operator|+
literal|";"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</book>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
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
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|downloadImages (Node node)
specifier|public
name|void
name|downloadImages
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|List
name|imageList
init|=
name|getImageUrls
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|Iterator
name|iter
init|=
name|imageList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|imageUrl
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|imageFile
init|=
literal|"imageFile"
decl_stmt|;
comment|//check if url path is relative
if|if
condition|(
name|imageUrl
operator|.
name|indexOf
argument_list|(
literal|"http://"
argument_list|)
operator|<
literal|0
condition|)
block|{
name|imageUrl
operator|=
name|baseURL
operator|+
name|imageUrl
expr_stmt|;
block|}
try|try
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|imageUrl
argument_list|)
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|url
operator|.
name|getFile
argument_list|()
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|imageFile
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
name|URLConnection
name|connection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|connection
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|BufferedInputStream
name|in
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|FileOutputStream
name|file
init|=
operator|new
name|FileOutputStream
argument_list|(
name|imageLocation
operator|+
name|File
operator|.
name|separator
operator|+
name|imageFile
argument_list|)
decl_stmt|;
name|BufferedOutputStream
name|out
init|=
operator|new
name|BufferedOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|int
name|i
decl_stmt|;
while|while
condition|(
operator|(
name|i
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getImageUrls (Node node)
specifier|public
name|List
name|getImageUrls
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|ArrayList
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|DOMElementImpl
name|doc
init|=
operator|(
name|DOMElementImpl
operator|)
name|node
decl_stmt|;
name|NodeList
name|imageList
init|=
name|doc
operator|.
name|getElementsByTagName
argument_list|(
literal|"img"
argument_list|)
decl_stmt|;
if|if
condition|(
name|imageList
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
name|imageList
operator|.
name|getLength
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|Node
name|imageNode
init|=
name|imageList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|NamedNodeMap
name|nm
init|=
name|imageNode
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
literal|"src"
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|attr
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
DECL|method|getChapterId ()
specifier|public
name|String
name|getChapterId
parameter_list|()
block|{
return|return
name|chapterId
return|;
block|}
DECL|method|setChapterId (String chapterId)
specifier|public
name|void
name|setChapterId
parameter_list|(
name|String
name|chapterId
parameter_list|)
block|{
name|this
operator|.
name|chapterId
operator|=
name|chapterId
expr_stmt|;
block|}
DECL|method|removeExtension (String resource)
specifier|public
name|String
name|removeExtension
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|int
name|index
init|=
name|resource
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
return|return
name|resource
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
return|;
block|}
block|}
end_class

end_unit

