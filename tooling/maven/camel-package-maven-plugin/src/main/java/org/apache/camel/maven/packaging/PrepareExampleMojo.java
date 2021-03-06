begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Arrays
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
name|Comparator
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
name|TreeSet
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
name|maven
operator|.
name|packaging
operator|.
name|model
operator|.
name|ExampleModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|plugins
operator|.
name|annotations
operator|.
name|Component
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
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
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
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
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
name|mvel2
operator|.
name|templates
operator|.
name|TemplateRuntime
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|PackageHelper
operator|.
name|loadText
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|PackageHelper
operator|.
name|writeText
import|;
end_import

begin_comment
comment|/**  * Prepares the readme.md files content up to date with all the examples that Apache Camel ships.  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"prepare-example"
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
DECL|class|PrepareExampleMojo
specifier|public
class|class
name|PrepareExampleMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"project"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|readonly
operator|=
literal|true
argument_list|)
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Maven ProjectHelper.      */
annotation|@
name|Component
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * Execute goal.      *      * @throws MojoExecutionException execution of the main class or one of the      *                                                        threads it generated failed.      * @throws MojoFailureException   something bad happened...      */
annotation|@
name|Override
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
name|executeExamplesReadme
argument_list|()
expr_stmt|;
block|}
DECL|method|executeExamplesReadme ()
specifier|protected
name|void
name|executeExamplesReadme
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|Set
argument_list|<
name|File
argument_list|>
name|examples
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|// only run in examples directory where the main readme.adoc file is located
name|String
name|currentDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"."
argument_list|)
operator|.
name|normalize
argument_list|()
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|currentDir
operator|.
name|endsWith
argument_list|(
literal|"examples"
argument_list|)
condition|)
block|{
return|return;
block|}
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
name|examples
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|files
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|List
argument_list|<
name|ExampleModel
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|examples
control|)
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"camel-example"
argument_list|)
condition|)
block|{
name|File
name|pom
init|=
operator|new
name|File
argument_list|(
name|file
argument_list|,
literal|"pom.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pom
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|existing
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|pom
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
decl_stmt|;
name|ExampleModel
name|model
init|=
operator|new
name|ExampleModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setFileName
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|StringHelper
operator|.
name|between
argument_list|(
name|existing
argument_list|,
literal|"<name>"
argument_list|,
literal|"</name>"
argument_list|)
decl_stmt|;
name|String
name|title
init|=
name|StringHelper
operator|.
name|between
argument_list|(
name|existing
argument_list|,
literal|"<title>"
argument_list|,
literal|"</title>"
argument_list|)
decl_stmt|;
name|String
name|description
init|=
name|StringHelper
operator|.
name|between
argument_list|(
name|existing
argument_list|,
literal|"<description>"
argument_list|,
literal|"</description>"
argument_list|)
decl_stmt|;
name|String
name|category
init|=
name|StringHelper
operator|.
name|between
argument_list|(
name|existing
argument_list|,
literal|"<category>"
argument_list|,
literal|"</category>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
name|model
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// fallback and use file name as title
name|model
operator|.
name|setTitle
argument_list|(
name|asTitle
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|model
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|category
operator|!=
literal|null
condition|)
block|{
name|model
operator|.
name|setCategory
argument_list|(
name|category
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|contains
argument_list|(
literal|"(deprecated)"
argument_list|)
condition|)
block|{
name|model
operator|.
name|setDeprecated
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|model
operator|.
name|setDeprecated
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
block|}
comment|// readme files is either readme.md or readme.adoc
name|String
index|[]
name|readmes
init|=
operator|new
name|File
argument_list|(
name|file
argument_list|,
literal|"."
argument_list|)
operator|.
name|list
argument_list|(
parameter_list|(
name|folder
parameter_list|,
name|fileName
parameter_list|)
lambda|->
name|fileName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"readme"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|readmes
operator|!=
literal|null
operator|&&
name|readmes
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|model
operator|.
name|setReadmeFileName
argument_list|(
name|readmes
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|models
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// sort the models
name|Collections
operator|.
name|sort
argument_list|(
name|models
argument_list|,
operator|new
name|ExampleComparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// how many deprecated
name|long
name|deprecated
init|=
name|models
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|m
lambda|->
literal|"true"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getDeprecated
argument_list|()
argument_list|)
argument_list|)
operator|.
name|count
argument_list|()
decl_stmt|;
comment|// update the big readme file in the examples dir
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|,
literal|"README.adoc"
argument_list|)
decl_stmt|;
comment|// update regular components
name|boolean
name|exists
init|=
name|file
operator|.
name|exists
argument_list|()
decl_stmt|;
name|String
name|changed
init|=
name|templateExamples
argument_list|(
name|models
argument_list|,
name|deprecated
argument_list|)
decl_stmt|;
name|boolean
name|updated
init|=
name|updateExamples
argument_list|(
name|file
argument_list|,
name|changed
argument_list|)
decl_stmt|;
if|if
condition|(
name|updated
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Updated readme.adoc file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exists
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"No changes to readme.adoc file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"No readme.adoc file: "
operator|+
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
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Error due "
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
DECL|method|templateExamples (List<ExampleModel> models, long deprecated)
specifier|private
name|String
name|templateExamples
parameter_list|(
name|List
argument_list|<
name|ExampleModel
argument_list|>
name|models
parameter_list|,
name|long
name|deprecated
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|String
name|template
init|=
name|loadText
argument_list|(
name|UpdateReadmeMojo
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"readme-examples.mvel"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"examples"
argument_list|,
name|models
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"numberOfDeprecated"
argument_list|,
name|deprecated
argument_list|)
expr_stmt|;
name|String
name|out
init|=
operator|(
name|String
operator|)
name|TemplateRuntime
operator|.
name|eval
argument_list|(
name|template
argument_list|,
name|map
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"util"
argument_list|,
name|MvelHelper
operator|.
name|INSTANCE
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|out
return|;
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
literal|"Error processing mvel template. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|updateExamples (File file, String changed)
specifier|private
name|boolean
name|updateExamples
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|changed
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|String
name|existing
init|=
name|StringHelper
operator|.
name|between
argument_list|(
name|text
argument_list|,
literal|"// examples: START"
argument_list|,
literal|"// examples: END"
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
comment|// remove leading line breaks etc
name|existing
operator|=
name|existing
operator|.
name|trim
argument_list|()
expr_stmt|;
name|changed
operator|=
name|changed
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|existing
operator|.
name|equals
argument_list|(
name|changed
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|String
name|before
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|text
argument_list|,
literal|"// examples: START"
argument_list|)
decl_stmt|;
name|String
name|after
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|text
argument_list|,
literal|"// examples: END"
argument_list|)
decl_stmt|;
name|text
operator|=
name|before
operator|+
literal|"// examples: START\n"
operator|+
name|changed
operator|+
literal|"\n// examples: END"
operator|+
name|after
expr_stmt|;
name|writeText
argument_list|(
name|file
argument_list|,
name|text
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Cannot find markers in file "
operator|+
name|file
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Add the following markers"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t// examples: START"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t// examples: END"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
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
literal|"Error reading file "
operator|+
name|file
operator|+
literal|" Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|class|ExampleComparator
specifier|private
specifier|static
class|class
name|ExampleComparator
implements|implements
name|Comparator
argument_list|<
name|ExampleModel
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compare (ExampleModel o1, ExampleModel o2)
specifier|public
name|int
name|compare
parameter_list|(
name|ExampleModel
name|o1
parameter_list|,
name|ExampleModel
name|o2
parameter_list|)
block|{
comment|// lets sort by category first and then file afterwards
name|int
name|num
init|=
name|o1
operator|.
name|getCategory
argument_list|()
operator|.
name|compareToIgnoreCase
argument_list|(
name|o2
operator|.
name|getCategory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|==
literal|0
condition|)
block|{
return|return
name|o1
operator|.
name|getFileName
argument_list|()
operator|.
name|compareToIgnoreCase
argument_list|(
name|o2
operator|.
name|getFileName
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|num
return|;
block|}
block|}
block|}
DECL|method|asTitle (String fileName)
specifier|private
specifier|static
name|String
name|asTitle
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
comment|// skip camel-example
name|String
name|answer
init|=
name|fileName
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|.
name|startsWith
argument_list|(
literal|"camel-example-"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|substring
argument_list|(
literal|14
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|StringHelper
operator|.
name|camelDashToTitle
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

