begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.manual
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|manual
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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ccil
operator|.
name|cowan
operator|.
name|tagsoup
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ccil
operator|.
name|cowan
operator|.
name|tagsoup
operator|.
name|XMLWriter
import|;
end_import

begin_class
DECL|class|ManualGenerator
specifier|public
class|class
name|ManualGenerator
block|{
DECL|field|page
name|String
name|page
decl_stmt|;
DECL|field|output
name|String
name|output
decl_stmt|;
DECL|field|head
name|String
name|head
decl_stmt|;
DECL|field|version
name|String
name|version
decl_stmt|;
DECL|field|targetDir
name|String
name|targetDir
decl_stmt|;
DECL|method|ManualGenerator (String[] args)
specifier|public
name|ManualGenerator
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|page
operator|=
name|args
index|[
literal|0
index|]
expr_stmt|;
name|output
operator|=
name|args
index|[
literal|1
index|]
expr_stmt|;
name|version
operator|=
name|args
index|[
literal|2
index|]
expr_stmt|;
name|head
operator|=
name|args
index|[
literal|3
index|]
expr_stmt|;
name|targetDir
operator|=
name|args
index|[
literal|4
index|]
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|doGenerate
argument_list|()
condition|)
block|{
name|String
name|content
init|=
name|grabBodyContent
argument_list|()
decl_stmt|;
name|storeHTMLFile
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
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
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
name|IOException
block|{
name|String
name|replaceToken
init|=
literal|"<h3 id=\"replaceme\">.*</h3>"
decl_stmt|;
name|String
name|replaceValue
init|=
literal|"<h3>Version "
operator|+
name|version
operator|+
literal|"</h3>"
decl_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|outFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
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
name|outFile
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
name|out
operator|.
name|println
argument_list|(
literal|"</head>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|replaceToken
operator|!=
literal|null
operator|&&
name|replaceValue
operator|!=
literal|null
condition|)
block|{
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
name|replaceToken
argument_list|,
name|replaceValue
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|"<body>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</body>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|doGenerate ()
specifier|private
name|boolean
name|doGenerate
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|IOException
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
literal|".manualCache-"
operator|+
name|url
operator|.
name|getFile
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|HttpURLConnection
name|con
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|con
operator|.
name|setRequestMethod
argument_list|(
literal|"HEAD"
argument_list|)
expr_stmt|;
name|long
name|date
init|=
name|con
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
name|FileReader
name|reader
init|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|char
name|chars
index|[]
init|=
operator|new
name|char
index|[
literal|1000
index|]
decl_stmt|;
name|int
name|i
init|=
name|reader
operator|.
name|read
argument_list|(
name|chars
argument_list|)
decl_stmt|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|long
name|lastDate
init|=
name|Long
operator|.
name|parseLong
argument_list|(
operator|new
name|String
argument_list|(
name|chars
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|date
operator|<=
name|lastDate
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|grabBodyContent ()
specifier|private
name|String
name|grabBodyContent
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|IOException
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
literal|".manualCache-"
operator|+
name|url
operator|.
name|getFile
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|HttpURLConnection
name|con
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|XMLReader
name|parser
init|=
operator|new
name|Parser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setFeature
argument_list|(
name|Parser
operator|.
name|namespacesFeature
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setFeature
argument_list|(
name|Parser
operator|.
name|namespacePrefixesFeature
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setProperty
argument_list|(
name|Parser
operator|.
name|schemaProperty
argument_list|,
operator|new
name|org
operator|.
name|ccil
operator|.
name|cowan
operator|.
name|tagsoup
operator|.
name|HTMLSchema
argument_list|()
block|{
block|{
comment|//problem with nested lists that the confluence {toc} macro creates
name|elementType
argument_list|(
literal|"ul"
argument_list|,
name|M_LI
argument_list|,
name|M_BLOCK
operator||
name|M_LI
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLWriter
name|xmlWriter
init|=
operator|new
name|XMLWriter
argument_list|(
name|w
argument_list|)
block|{
name|int
name|inDiv
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|int
name|count
decl_stmt|;
specifier|public
name|void
name|characters
parameter_list|(
name|char
name|ch
index|[]
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|inDiv
operator|<=
name|count
condition|)
block|{
name|super
operator|.
name|characters
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|startElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
name|count
operator|++
expr_stmt|;
if|if
condition|(
literal|"div"
operator|.
name|equalsIgnoreCase
argument_list|(
name|qName
argument_list|)
operator|&&
literal|"wiki-content maincontent"
operator|.
name|equalsIgnoreCase
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
literal|"class"
argument_list|)
argument_list|)
condition|)
block|{
name|inDiv
operator|=
name|count
expr_stmt|;
block|}
if|if
condition|(
name|inDiv
operator|<=
name|count
condition|)
block|{
name|super
operator|.
name|startElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|endElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|inDiv
operator|<=
name|count
condition|)
block|{
name|super
operator|.
name|endElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|)
expr_stmt|;
block|}
name|count
operator|--
expr_stmt|;
if|if
condition|(
name|inDiv
operator|>
name|count
condition|)
block|{
name|inDiv
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|xmlWriter
operator|.
name|setOutputProperty
argument_list|(
name|XMLWriter
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|setOutputProperty
argument_list|(
name|XMLWriter
operator|.
name|METHOD
argument_list|,
literal|"html"
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|xmlWriter
argument_list|)
expr_stmt|;
name|long
name|date
init|=
name|con
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
name|con
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|FileWriter
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|date
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|w
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * @param args      */
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
comment|/*args = new String[] {             "http://camel.apache.org/book-in-one-page.html",             "/tmp/foo.html",             "1.0",             ""         };*/
operator|new
name|ManualGenerator
argument_list|(
name|args
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

