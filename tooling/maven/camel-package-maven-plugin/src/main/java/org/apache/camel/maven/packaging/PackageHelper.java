begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|BufferedReader
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
name|FileFilter
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|LineNumberReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|model
operator|.
name|Resource
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
name|sonatype
operator|.
name|plexus
operator|.
name|build
operator|.
name|incremental
operator|.
name|BuildContext
import|;
end_import

begin_class
DECL|class|PackageHelper
specifier|public
specifier|final
class|class
name|PackageHelper
block|{
DECL|method|PackageHelper ()
specifier|private
name|PackageHelper
parameter_list|()
block|{     }
DECL|method|haveResourcesChanged (Log log, MavenProject project, BuildContext buildContext, String suffix)
specifier|public
specifier|static
name|boolean
name|haveResourcesChanged
parameter_list|(
name|Log
name|log
parameter_list|,
name|MavenProject
name|project
parameter_list|,
name|BuildContext
name|buildContext
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
name|String
name|baseDir
init|=
name|project
operator|.
name|getBasedir
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
for|for
control|(
name|Resource
name|r
range|:
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getResources
argument_list|()
control|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|r
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|r
operator|.
name|getDirectory
argument_list|()
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|path
init|=
name|file
operator|.
name|getPath
argument_list|()
operator|+
literal|"/"
operator|+
name|suffix
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Checking  if "
operator|+
name|path
operator|+
literal|" ("
operator|+
name|r
operator|.
name|getDirectory
argument_list|()
operator|+
literal|"/"
operator|+
name|suffix
operator|+
literal|") has changed."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|buildContext
operator|.
name|hasDelta
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Indeed "
operator|+
name|suffix
operator|+
literal|" has changed."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Loads the entire stream into memory as a String and returns it.      *<p/>      *<b>Notice:</b> This implementation appends a<tt>\n</tt> as line      * terminator at the of the text.      *<p/>      * Warning, don't use for crazy big streams :)      */
DECL|method|loadText (InputStream in)
specifier|public
specifier|static
name|String
name|loadText
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
decl_stmt|;
try|try
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|LineNumberReader
argument_list|(
name|isr
argument_list|)
decl_stmt|;
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
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
finally|finally
block|{
name|isr
operator|.
name|close
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|writeText (File file, String text)
specifier|public
specifier|static
name|void
name|writeText
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|text
parameter_list|)
throws|throws
name|IOException
block|{
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
name|fos
operator|.
name|write
argument_list|(
name|text
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|after (String text, String after)
specifier|public
specifier|static
name|String
name|after
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|after
parameter_list|)
block|{
if|if
condition|(
operator|!
name|text
operator|.
name|contains
argument_list|(
name|after
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|text
operator|.
name|substring
argument_list|(
name|text
operator|.
name|indexOf
argument_list|(
name|after
argument_list|)
operator|+
name|after
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Parses the text as a map (eg key=value)      * @param data the data      * @return the map      */
DECL|method|parseAsMap (String data)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseAsMap
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|lines
init|=
name|data
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|int
name|idx
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|key
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|line
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
decl_stmt|;
comment|// remove ending line break for the values
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|key
operator|.
name|trim
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|findJsonFiles (File dir, FileFilter filter)
specifier|public
specifier|static
name|Set
argument_list|<
name|File
argument_list|>
name|findJsonFiles
parameter_list|(
name|File
name|dir
parameter_list|,
name|FileFilter
name|filter
parameter_list|)
block|{
name|Set
argument_list|<
name|File
argument_list|>
name|files
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|findJsonFiles
argument_list|(
name|dir
argument_list|,
name|files
argument_list|,
name|filter
argument_list|)
expr_stmt|;
return|return
name|files
return|;
block|}
DECL|method|findJsonFiles (File dir, Set<File> found, FileFilter filter)
specifier|public
specifier|static
name|void
name|findJsonFiles
parameter_list|(
name|File
name|dir
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|found
parameter_list|,
name|FileFilter
name|filter
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|(
name|filter
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
comment|// skip files in root dirs as Camel does not store information there but others may do
name|boolean
name|jsonFile
init|=
name|file
operator|.
name|isFile
argument_list|()
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".json"
argument_list|)
decl_stmt|;
if|if
condition|(
name|jsonFile
condition|)
block|{
name|found
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|findJsonFiles
argument_list|(
name|file
argument_list|,
name|found
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|class|CamelComponentsModelFilter
specifier|public
specifier|static
class|class
name|CamelComponentsModelFilter
implements|implements
name|FileFilter
block|{
annotation|@
name|Override
DECL|method|accept (File pathname)
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
comment|// skip camel-jetty9 as its a duplicate of camel-jetty
if|if
condition|(
literal|"camel-jetty9"
operator|.
name|equals
argument_list|(
name|pathname
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|pathname
operator|.
name|isDirectory
argument_list|()
operator|||
name|pathname
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".json"
argument_list|)
return|;
block|}
block|}
DECL|class|CamelOthersModelFilter
specifier|public
specifier|static
class|class
name|CamelOthersModelFilter
implements|implements
name|FileFilter
block|{
annotation|@
name|Override
DECL|method|accept (File pathname)
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
name|String
name|name
init|=
name|pathname
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|special
init|=
literal|"camel-core-osgi"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-core-xml"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-http-common"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-jetty"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-jetty-common"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|boolean
name|special2
init|=
literal|"camel-as2"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-box"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-linkedin"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-olingo2"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-olingo4"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"camel-salesforce"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|special
operator|||
name|special2
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|pathname
operator|.
name|isDirectory
argument_list|()
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".json"
argument_list|)
return|;
block|}
block|}
DECL|method|findCamelCoreDirectory (MavenProject project, File dir)
specifier|public
specifier|static
name|File
name|findCamelCoreDirectory
parameter_list|(
name|MavenProject
name|project
parameter_list|,
name|File
name|dir
parameter_list|)
block|{
if|if
condition|(
name|dir
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|(
name|f
lambda|->
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"camel-core"
argument_list|)
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
operator|==
literal|1
condition|)
block|{
comment|// okay the file are in the target folder
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|,
literal|"target"
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|project
operator|.
name|getVersion
argument_list|()
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|target
argument_list|,
literal|"camel-core-"
operator|+
name|version
operator|+
literal|".jar"
argument_list|)
return|;
block|}
else|else
block|{
comment|// okay walk up the parent dir
return|return
name|findCamelCoreDirectory
argument_list|(
name|project
argument_list|,
name|dir
operator|.
name|getParentFile
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

