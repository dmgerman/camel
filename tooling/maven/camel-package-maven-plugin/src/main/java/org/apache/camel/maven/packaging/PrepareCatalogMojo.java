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
name|FileInputStream
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
name|nio
operator|.
name|channels
operator|.
name|FileChannel
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
name|Collections
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
name|TreeMap
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|plugin
operator|.
name|MojoFailureException
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

begin_comment
comment|/**  * Prepares the camel catalog to include component descriptors, and generates a report with components which have not been migrated  * to include component json descriptors.  *  * @goal prepare-catalog  * @execute phase="process-resources"  */
end_comment

begin_class
DECL|class|PrepareCatalogMojo
specifier|public
class|class
name|PrepareCatalogMojo
extends|extends
name|AbstractMojo
block|{
DECL|field|BUFFER_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|128
operator|*
literal|1024
decl_stmt|;
DECL|field|LABEL_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|LABEL_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\\"label\\\":\\s\\\"([\\w,]+)\\\""
argument_list|)
decl_stmt|;
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The output directory for components catalog      *      * @parameter default-value="${project.build.directory}/classes/org/apache/camel/catalog/components"      */
DECL|field|outDir
specifier|protected
name|File
name|outDir
decl_stmt|;
comment|/**      * The components directory where all the Apache Camel components are      *      * @parameter default-value="${project.build.directory}/../../..//components"      */
DECL|field|componentsDir
specifier|protected
name|File
name|componentsDir
decl_stmt|;
comment|/**      * The camel-core directory where camel-core components are      *      * @parameter default-value="${project.build.directory}/../../..//camel-core"      */
DECL|field|coreDir
specifier|protected
name|File
name|coreDir
decl_stmt|;
comment|/**      * Maven ProjectHelper.      *      * @component      * @readonly      */
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * Execute goal.      *      * @throws org.apache.maven.plugin.MojoExecutionException execution of the main class or one of the      *                                                        threads it generated failed.      * @throws org.apache.maven.plugin.MojoFailureException   something bad happened...      */
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Copying all Camel component json descriptors"
argument_list|)
expr_stmt|;
comment|// lets use sorted set/maps
name|Set
argument_list|<
name|File
argument_list|>
name|jsonFiles
init|=
operator|new
name|TreeSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|duplicateJsonFiles
init|=
operator|new
name|TreeSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|componentFiles
init|=
operator|new
name|TreeSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|missingComponents
init|=
operator|new
name|TreeSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|missingLabels
init|=
operator|new
name|TreeSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|missingUriPaths
init|=
operator|new
name|TreeSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|usedLabels
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// find all json files in components and camel-core
if|if
condition|(
name|componentsDir
operator|!=
literal|null
operator|&&
name|componentsDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|components
init|=
name|componentsDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|components
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|dir
range|:
name|components
control|)
block|{
if|if
condition|(
name|dir
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
literal|"target"
operator|.
name|equals
argument_list|(
name|dir
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"target/classes"
argument_list|)
decl_stmt|;
name|int
name|before
init|=
name|componentFiles
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|before2
init|=
name|jsonFiles
operator|.
name|size
argument_list|()
decl_stmt|;
name|findFilesRecursive
argument_list|(
name|target
argument_list|,
name|jsonFiles
argument_list|,
name|componentFiles
argument_list|,
operator|new
name|CamelComponentsFileFilter
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|after
init|=
name|componentFiles
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|after2
init|=
name|jsonFiles
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|before
operator|!=
name|after
operator|&&
name|before2
operator|==
name|after2
condition|)
block|{
name|missingComponents
operator|.
name|add
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
if|if
condition|(
name|coreDir
operator|!=
literal|null
operator|&&
name|coreDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|coreDir
argument_list|,
literal|"target/classes"
argument_list|)
decl_stmt|;
name|int
name|before
init|=
name|componentFiles
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|before2
init|=
name|jsonFiles
operator|.
name|size
argument_list|()
decl_stmt|;
name|findFilesRecursive
argument_list|(
name|target
argument_list|,
name|jsonFiles
argument_list|,
name|componentFiles
argument_list|,
operator|new
name|CamelComponentsFileFilter
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|after
init|=
name|componentFiles
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|after2
init|=
name|jsonFiles
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|before
operator|!=
name|after
operator|&&
name|before2
operator|==
name|after2
condition|)
block|{
name|missingComponents
operator|.
name|add
argument_list|(
name|coreDir
argument_list|)
expr_stmt|;
block|}
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Found "
operator|+
name|jsonFiles
operator|.
name|size
argument_list|()
operator|+
literal|" component json files"
argument_list|)
expr_stmt|;
comment|// make sure to create out dir
name|outDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
for|for
control|(
name|File
name|file
range|:
name|jsonFiles
control|)
block|{
name|File
name|to
init|=
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|to
operator|.
name|exists
argument_list|()
condition|)
block|{
name|duplicateJsonFiles
operator|.
name|add
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Duplicate component name detected: "
operator|+
name|to
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|copyFile
argument_list|(
name|file
argument_list|,
name|to
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
name|MojoFailureException
argument_list|(
literal|"Cannot copy file from "
operator|+
name|file
operator|+
literal|" -> "
operator|+
name|to
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// check if we have a label as we want the components to include labels
try|try
block|{
name|String
name|text
init|=
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
comment|// just do a basic label check
if|if
condition|(
name|text
operator|.
name|contains
argument_list|(
literal|"\"label\": \"\""
argument_list|)
condition|)
block|{
name|missingLabels
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|name
init|=
name|asComponentName
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|LABEL_PATTERN
operator|.
name|matcher
argument_list|(
name|text
argument_list|)
decl_stmt|;
comment|// grab the label, and remember it in the used labels
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|label
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
index|[]
name|labels
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|labels
control|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|components
init|=
name|usedLabels
operator|.
name|get
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|components
operator|==
literal|null
condition|)
block|{
name|components
operator|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|usedLabels
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|components
argument_list|)
expr_stmt|;
block|}
name|components
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|text
operator|.
name|contains
argument_list|(
literal|"\"kind\": \"path\""
argument_list|)
condition|)
block|{
name|missingUriPaths
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|File
name|all
init|=
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
literal|"../components.properties"
argument_list|)
decl_stmt|;
try|try
block|{
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|all
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
index|[]
name|names
init|=
name|outDir
operator|.
name|list
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|components
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// sort the names
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".json"
argument_list|)
condition|)
block|{
comment|// strip out .json from the name
name|String
name|componentName
init|=
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
literal|5
argument_list|)
decl_stmt|;
name|components
operator|.
name|add
argument_list|(
name|componentName
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|components
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|components
control|)
block|{
name|fos
operator|.
name|write
argument_list|(
name|name
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fos
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
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Error writing to file "
operator|+
name|all
argument_list|)
throw|;
block|}
name|printReport
argument_list|(
name|jsonFiles
argument_list|,
name|duplicateJsonFiles
argument_list|,
name|missingComponents
argument_list|,
name|missingUriPaths
argument_list|,
name|missingLabels
argument_list|,
name|usedLabels
argument_list|)
expr_stmt|;
block|}
DECL|method|printReport (Set<File> json, Set<File> duplicate, Set<File> missing, Set<File> missingUriPaths, Set<File> missingLabels, Map<String, Set<String>> usedLabels)
specifier|private
name|void
name|printReport
parameter_list|(
name|Set
argument_list|<
name|File
argument_list|>
name|json
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|duplicate
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|missing
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|missingUriPaths
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|missingLabels
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|usedLabels
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"================================================================================"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Camel component catalog report"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"\tComponents found: "
operator|+
name|json
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|file
range|:
name|json
control|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"\t\t"
operator|+
name|asComponentName
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|duplicate
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\tDuplicate components detected: "
operator|+
name|duplicate
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|file
range|:
name|duplicate
control|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t\t"
operator|+
name|asComponentName
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|missingLabels
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\tMissing labels detected: "
operator|+
name|missingLabels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|file
range|:
name|missingLabels
control|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t\t"
operator|+
name|asComponentName
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|usedLabels
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"\tUsed labels: "
operator|+
name|usedLabels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|usedLabels
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"\t\t"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"\t\t\t"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|missingUriPaths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\tMissing @UriPath detected: "
operator|+
name|missingUriPaths
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|file
range|:
name|missingUriPaths
control|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t\t"
operator|+
name|asComponentName
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\tMissing components detected: "
operator|+
name|missing
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|name
range|:
name|missing
control|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t\t"
operator|+
name|name
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"================================================================================"
argument_list|)
expr_stmt|;
block|}
DECL|method|asComponentName (File file)
specifier|private
specifier|static
name|String
name|asComponentName
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
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".json"
argument_list|)
condition|)
block|{
return|return
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
literal|5
argument_list|)
return|;
block|}
return|return
name|name
return|;
block|}
DECL|method|findFilesRecursive (File dir, Set<File> found, Set<File> components, FileFilter filter)
specifier|private
name|void
name|findFilesRecursive
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
name|Set
argument_list|<
name|File
argument_list|>
name|components
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
name|rootDir
init|=
literal|"classes"
operator|.
name|equals
argument_list|(
name|dir
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
literal|"META-INF"
operator|.
name|equals
argument_list|(
name|dir
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|jsonFile
init|=
operator|!
name|rootDir
operator|&&
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
name|boolean
name|componentFile
init|=
operator|!
name|rootDir
operator|&&
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
name|equals
argument_list|(
literal|"component.properties"
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
name|componentFile
condition|)
block|{
name|components
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
name|findFilesRecursive
argument_list|(
name|file
argument_list|,
name|found
argument_list|,
name|components
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|class|CamelComponentsFileFilter
specifier|private
class|class
name|CamelComponentsFileFilter
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
operator|||
operator|(
name|pathname
operator|.
name|isFile
argument_list|()
operator|&&
name|pathname
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"component.properties"
argument_list|)
operator|)
return|;
block|}
block|}
DECL|method|copyFile (File from, File to)
specifier|public
specifier|static
name|void
name|copyFile
parameter_list|(
name|File
name|from
parameter_list|,
name|File
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|FileChannel
name|in
init|=
literal|null
decl_stmt|;
name|FileChannel
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|FileInputStream
argument_list|(
name|from
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|to
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|in
operator|.
name|size
argument_list|()
decl_stmt|;
name|long
name|position
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|position
operator|<
name|size
condition|)
block|{
name|position
operator|+=
name|in
operator|.
name|transferTo
argument_list|(
name|position
argument_list|,
name|BUFFER_SIZE
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
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
block|}
end_class

end_unit

