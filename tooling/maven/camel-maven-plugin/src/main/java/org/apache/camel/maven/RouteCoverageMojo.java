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
name|FileInputStream
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
name|PrintStream
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
name|LinkedHashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|helper
operator|.
name|EndpointHelper
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
name|model
operator|.
name|RouteCoverageNode
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
name|parser
operator|.
name|RouteBuilderParser
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
name|parser
operator|.
name|XmlRouteParser
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
name|parser
operator|.
name|helper
operator|.
name|RouteCoverageHelper
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
name|parser
operator|.
name|model
operator|.
name|CamelNodeDetails
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
name|parser
operator|.
name|model
operator|.
name|CoverageData
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
name|codehaus
operator|.
name|mojo
operator|.
name|exec
operator|.
name|AbstractExecMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|Roaster
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|JavaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|JavaClassSource
import|;
end_import

begin_comment
comment|/**  * Performs route coverage reports after running Camel unit tests with camel-test modules  *  * @goal route-coverage  * @threadSafe  */
end_comment

begin_class
DECL|class|RouteCoverageMojo
specifier|public
class|class
name|RouteCoverageMojo
extends|extends
name|AbstractExecMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Whether to fail if a route was not fully covered      *      * @parameter property="camel.failOnError"      *            default-value="false"      */
DECL|field|failOnError
specifier|private
name|boolean
name|failOnError
decl_stmt|;
comment|/**      * Whether to include test source code      *      * @parameter property="camel.includeTest"      *            default-value="false"      */
DECL|field|includeTest
specifier|private
name|boolean
name|includeTest
decl_stmt|;
comment|/**      * To filter the names of java and xml files to only include files matching any of the given list of patterns (wildcard and regular expression).      * Multiple values can be separated by comma.      *      * @parameter property="camel.includes"      */
DECL|field|includes
specifier|private
name|String
name|includes
decl_stmt|;
comment|/**      * To filter the names of java and xml files to exclude files matching any of the given list of patterns (wildcard and regular expression).      * Multiple values can be separated by comma.      *      * @parameter property="camel.excludes"      */
DECL|field|excludes
specifier|private
name|String
name|excludes
decl_stmt|;
comment|// CHECKSTYLE:OFF
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
name|Set
argument_list|<
name|File
argument_list|>
name|javaFiles
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|xmlFiles
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
comment|// find all java route builder classes
name|List
name|list
init|=
name|project
operator|.
name|getCompileSourceRoots
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|list
control|)
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
name|findJavaFiles
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|)
argument_list|,
name|javaFiles
argument_list|)
expr_stmt|;
block|}
comment|// find all xml routes
name|list
operator|=
name|project
operator|.
name|getResources
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|list
control|)
block|{
name|Resource
name|dir
init|=
operator|(
name|Resource
operator|)
name|obj
decl_stmt|;
name|findXmlFiles
argument_list|(
operator|new
name|File
argument_list|(
name|dir
operator|.
name|getDirectory
argument_list|()
argument_list|)
argument_list|,
name|xmlFiles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeTest
condition|)
block|{
name|list
operator|=
name|project
operator|.
name|getTestCompileSourceRoots
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|list
control|)
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
name|findJavaFiles
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|)
argument_list|,
name|javaFiles
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|project
operator|.
name|getTestResources
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|list
control|)
block|{
name|Resource
name|dir
init|=
operator|(
name|Resource
operator|)
name|obj
decl_stmt|;
name|findXmlFiles
argument_list|(
operator|new
name|File
argument_list|(
name|dir
operator|.
name|getDirectory
argument_list|()
argument_list|)
argument_list|,
name|xmlFiles
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|routeTrees
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
name|javaFiles
control|)
block|{
if|if
condition|(
name|matchFile
argument_list|(
name|file
argument_list|)
condition|)
block|{
try|try
block|{
comment|// parse the java source code and find Camel RouteBuilder classes
name|String
name|fqn
init|=
name|file
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|baseDir
init|=
literal|"."
decl_stmt|;
name|JavaType
name|out
init|=
name|Roaster
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
comment|// we should only parse java classes (not interfaces and enums etc)
if|if
condition|(
name|out
operator|!=
literal|null
operator|&&
name|out
operator|instanceof
name|JavaClassSource
condition|)
block|{
name|JavaClassSource
name|clazz
init|=
operator|(
name|JavaClassSource
operator|)
name|out
decl_stmt|;
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|result
init|=
name|RouteBuilderParser
operator|.
name|parseRouteBuilderTree
argument_list|(
name|clazz
argument_list|,
name|baseDir
argument_list|,
name|fqn
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|routeTrees
operator|.
name|addAll
argument_list|(
name|result
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
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Error parsing java file "
operator|+
name|file
operator|+
literal|" code due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|File
name|file
range|:
name|xmlFiles
control|)
block|{
if|if
condition|(
name|matchFile
argument_list|(
name|file
argument_list|)
condition|)
block|{
try|try
block|{
comment|// parse the xml files code and find Camel routes
name|String
name|fqn
init|=
name|file
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|baseDir
init|=
literal|"."
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|result
init|=
name|XmlRouteParser
operator|.
name|parseXmlRouteTree
argument_list|(
name|is
argument_list|,
name|baseDir
argument_list|,
name|fqn
argument_list|)
decl_stmt|;
name|routeTrees
operator|.
name|addAll
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
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
name|warn
argument_list|(
literal|"Error parsing xml file "
operator|+
name|file
operator|+
literal|" code due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Discovered "
operator|+
name|routeTrees
operator|.
name|size
argument_list|()
operator|+
literal|" routes"
argument_list|)
expr_stmt|;
comment|// skip any routes which has no route id assigned
name|long
name|anonymous
init|=
name|routeTrees
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|t
lambda|->
name|t
operator|.
name|getRouteId
argument_list|()
operator|==
literal|null
argument_list|)
operator|.
name|count
argument_list|()
decl_stmt|;
if|if
condition|(
name|anonymous
operator|>
literal|0
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Discovered "
operator|+
name|anonymous
operator|+
literal|" anonymous routes. Add route ids to these routes for route coverage support"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AtomicInteger
name|notCovered
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|routeTrees
operator|=
name|routeTrees
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|t
lambda|->
name|t
operator|.
name|getRouteId
argument_list|()
operator|!=
literal|null
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|CamelNodeDetails
name|t
range|:
name|routeTrees
control|)
block|{
name|String
name|routeId
init|=
name|t
operator|.
name|getRouteId
argument_list|()
decl_stmt|;
name|String
name|fileName
init|=
name|asRelativeFile
argument_list|(
name|t
operator|.
name|getFileName
argument_list|()
argument_list|)
decl_stmt|;
comment|// grab dump data for the route
try|try
block|{
name|List
argument_list|<
name|CoverageData
argument_list|>
name|coverageData
init|=
name|RouteCoverageHelper
operator|.
name|parseDumpRouteCoverageByRouteId
argument_list|(
literal|"target/camel-route-coverage"
argument_list|,
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|coverageData
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"No route coverage data found for route: "
operator|+
name|routeId
operator|+
literal|". Make sure to enable route coverage in your unit tests and assign unique route ids to your routes. Also remember to run unit tests first."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|RouteCoverageNode
argument_list|>
name|coverage
init|=
name|gatherRouteCoverageSummary
argument_list|(
name|t
argument_list|,
name|coverageData
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|templateCoverageData
argument_list|(
name|fileName
argument_list|,
name|routeId
argument_list|,
name|coverage
argument_list|,
name|notCovered
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Route coverage summary:\n\n"
operator|+
name|out
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
literal|"Error during gathering route coverage data for route: "
operator|+
name|routeId
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|failOnError
operator|&&
name|notCovered
operator|.
name|get
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"There are "
operator|+
name|notCovered
operator|.
name|get
argument_list|()
operator|+
literal|" route(s) not fully covered!"
argument_list|)
throw|;
block|}
block|}
comment|// CHECKSTYLE:ON
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|templateCoverageData (String fileName, String routeId, List<RouteCoverageNode> model, AtomicInteger notCovered)
specifier|private
name|String
name|templateCoverageData
parameter_list|(
name|String
name|fileName
parameter_list|,
name|String
name|routeId
parameter_list|,
name|List
argument_list|<
name|RouteCoverageNode
argument_list|>
name|model
parameter_list|,
name|AtomicInteger
name|notCovered
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|sw
init|=
operator|new
name|PrintStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sw
operator|.
name|println
argument_list|(
literal|"Class:\t"
operator|+
name|model
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sw
operator|.
name|println
argument_list|(
literal|"File:\t"
operator|+
name|fileName
argument_list|)
expr_stmt|;
block|}
name|sw
operator|.
name|println
argument_list|(
literal|"RouteId:\t"
operator|+
name|routeId
argument_list|)
expr_stmt|;
name|sw
operator|.
name|println
argument_list|()
expr_stmt|;
name|sw
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%8s   %8s   %s"
argument_list|,
literal|"Line #"
argument_list|,
literal|"Count"
argument_list|,
literal|"Route"
argument_list|)
argument_list|)
expr_stmt|;
name|sw
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%8s   %8s   %s"
argument_list|,
literal|"------"
argument_list|,
literal|"-----"
argument_list|,
literal|"-----"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|covered
init|=
literal|0
decl_stmt|;
for|for
control|(
name|RouteCoverageNode
name|node
range|:
name|model
control|)
block|{
if|if
condition|(
name|node
operator|.
name|getCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|covered
operator|++
expr_stmt|;
block|}
name|String
name|pad
init|=
name|padString
argument_list|(
name|node
operator|.
name|getLevel
argument_list|()
argument_list|)
decl_stmt|;
name|sw
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%8s   %8s   %s"
argument_list|,
name|node
operator|.
name|getLineNumber
argument_list|()
argument_list|,
name|node
operator|.
name|getCount
argument_list|()
argument_list|,
name|pad
operator|+
name|node
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|covered
operator|!=
name|model
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// okay here is a route that was not fully covered
name|notCovered
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
comment|// calculate percentage of route coverage (must use double to have decimals)
name|double
name|percentage
init|=
operator|(
operator|(
name|double
operator|)
name|covered
operator|/
operator|(
name|double
operator|)
name|model
operator|.
name|size
argument_list|()
operator|)
operator|*
literal|100
decl_stmt|;
name|sw
operator|.
name|println
argument_list|()
expr_stmt|;
name|sw
operator|.
name|println
argument_list|(
literal|"Coverage: "
operator|+
name|covered
operator|+
literal|" out of "
operator|+
name|model
operator|.
name|size
argument_list|()
operator|+
literal|" ("
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"%.1f"
argument_list|,
name|percentage
argument_list|)
operator|+
literal|"%)"
argument_list|)
expr_stmt|;
name|sw
operator|.
name|println
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|gatherRouteCoverageSummary (CamelNodeDetails route, List<CoverageData> coverageData)
specifier|private
specifier|static
name|List
argument_list|<
name|RouteCoverageNode
argument_list|>
name|gatherRouteCoverageSummary
parameter_list|(
name|CamelNodeDetails
name|route
parameter_list|,
name|List
argument_list|<
name|CoverageData
argument_list|>
name|coverageData
parameter_list|)
block|{
name|List
argument_list|<
name|RouteCoverageNode
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|CoverageData
argument_list|>
name|it
init|=
name|coverageData
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|AtomicInteger
name|level
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|gatherRouteCoverageSummary
argument_list|(
name|route
argument_list|,
name|it
argument_list|,
name|level
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|gatherRouteCoverageSummary (CamelNodeDetails node, Iterator<CoverageData> it, AtomicInteger level, List<RouteCoverageNode> answer)
specifier|private
specifier|static
name|void
name|gatherRouteCoverageSummary
parameter_list|(
name|CamelNodeDetails
name|node
parameter_list|,
name|Iterator
argument_list|<
name|CoverageData
argument_list|>
name|it
parameter_list|,
name|AtomicInteger
name|level
parameter_list|,
name|List
argument_list|<
name|RouteCoverageNode
argument_list|>
name|answer
parameter_list|)
block|{
name|RouteCoverageNode
name|data
init|=
operator|new
name|RouteCoverageNode
argument_list|()
decl_stmt|;
name|data
operator|.
name|setName
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|data
operator|.
name|setLineNumber
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|node
operator|.
name|getLineNumber
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|setLevel
argument_list|(
name|level
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|data
operator|.
name|setClassName
argument_list|(
name|node
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|data
operator|.
name|setMethodName
argument_list|(
name|node
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
comment|// add data
name|answer
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
comment|// find count
name|boolean
name|found
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|found
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CoverageData
name|holder
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|holder
operator|.
name|getNode
argument_list|()
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|found
condition|)
block|{
name|data
operator|.
name|setCount
argument_list|(
name|holder
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|node
operator|.
name|getOutputs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|level
operator|.
name|addAndGet
argument_list|(
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|CamelNodeDetails
name|child
range|:
name|node
operator|.
name|getOutputs
argument_list|()
control|)
block|{
name|gatherRouteCoverageSummary
argument_list|(
name|child
argument_list|,
name|it
argument_list|,
name|level
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
name|level
operator|.
name|addAndGet
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|padString (int level)
specifier|private
specifier|static
name|String
name|padString
parameter_list|(
name|int
name|level
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
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
name|level
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"  "
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|findJavaFiles (File dir, Set<File> javaFiles)
specifier|private
name|void
name|findJavaFiles
parameter_list|(
name|File
name|dir
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|javaFiles
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|isDirectory
argument_list|()
condition|?
name|dir
operator|.
name|listFiles
argument_list|()
else|:
literal|null
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
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".java"
argument_list|)
condition|)
block|{
name|javaFiles
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
name|findJavaFiles
argument_list|(
name|file
argument_list|,
name|javaFiles
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|findXmlFiles (File dir, Set<File> xmlFiles)
specifier|private
name|void
name|findXmlFiles
parameter_list|(
name|File
name|dir
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|xmlFiles
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|isDirectory
argument_list|()
condition|?
name|dir
operator|.
name|listFiles
argument_list|()
else|:
literal|null
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
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|xmlFiles
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
name|findXmlFiles
argument_list|(
name|file
argument_list|,
name|xmlFiles
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|matchFile (File file)
specifier|private
name|boolean
name|matchFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|excludes
operator|==
literal|null
operator|&&
name|includes
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// exclude take precedence
if|if
condition|(
name|excludes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|exclude
range|:
name|excludes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|exclude
operator|=
name|exclude
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// try both with and without directory in the name
name|String
name|fqn
init|=
name|stripRootPath
argument_list|(
name|asRelativeFile
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|match
init|=
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|fqn
argument_list|,
name|exclude
argument_list|)
operator|||
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|exclude
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
comment|// include
if|if
condition|(
name|includes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|include
range|:
name|includes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|include
operator|=
name|include
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// try both with and without directory in the name
name|String
name|fqn
init|=
name|stripRootPath
argument_list|(
name|asRelativeFile
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|match
init|=
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|fqn
argument_list|,
name|include
argument_list|)
operator|||
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|include
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|// did not match any includes
return|return
literal|false
return|;
block|}
comment|// was not excluded nor failed include so its accepted
return|return
literal|true
return|;
block|}
DECL|method|asRelativeFile (String name)
specifier|private
name|String
name|asRelativeFile
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|answer
init|=
name|name
decl_stmt|;
name|String
name|base
init|=
name|project
operator|.
name|getBasedir
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|base
argument_list|)
condition|)
block|{
name|answer
operator|=
name|name
operator|.
name|substring
argument_list|(
name|base
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// skip leading slash for relative path
if|if
condition|(
name|answer
operator|.
name|startsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|stripRootPath (String name)
specifier|private
name|String
name|stripRootPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// strip out any leading source / resource directory
name|List
name|list
init|=
name|project
operator|.
name|getCompileSourceRoots
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|list
control|)
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
name|dir
operator|=
name|asRelativeFile
argument_list|(
name|dir
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|dir
argument_list|)
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|dir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
name|list
operator|=
name|project
operator|.
name|getTestCompileSourceRoots
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|list
control|)
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
name|dir
operator|=
name|asRelativeFile
argument_list|(
name|dir
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|dir
argument_list|)
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|dir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
name|List
name|resources
init|=
name|project
operator|.
name|getResources
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|resources
control|)
block|{
name|Resource
name|resource
init|=
operator|(
name|Resource
operator|)
name|obj
decl_stmt|;
name|String
name|dir
init|=
name|asRelativeFile
argument_list|(
name|resource
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|dir
argument_list|)
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|dir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
name|resources
operator|=
name|project
operator|.
name|getTestResources
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|resources
control|)
block|{
name|Resource
name|resource
init|=
operator|(
name|Resource
operator|)
name|obj
decl_stmt|;
name|String
name|dir
init|=
name|asRelativeFile
argument_list|(
name|resource
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|dir
argument_list|)
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|dir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

