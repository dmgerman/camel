begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.bom.generator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|bom
operator|.
name|generator
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
name|Collection
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|OutputKeys
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
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpression
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|Artifact
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
name|factory
operator|.
name|ArtifactFactory
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
name|repository
operator|.
name|ArtifactRepository
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
name|resolver
operator|.
name|ArtifactResolver
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
name|Dependency
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
name|DependencyManagement
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
name|Exclusion
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
name|Model
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
name|io
operator|.
name|xpp3
operator|.
name|MavenXpp3Reader
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

begin_comment
comment|/**  * Generate BOM by flattening the current project's dependency management section and applying exclusions.  *  * @goal generate  * @phase validate  */
end_comment

begin_class
DECL|class|BomGeneratorMojo
specifier|public
class|class
name|BomGeneratorMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The source pom template file.      *      * @parameter default-value="${basedir}/pom.xml"      */
DECL|field|sourcePom
specifier|protected
name|File
name|sourcePom
decl_stmt|;
comment|/**      * The pom file.      *      * @parameter default-value="${project.build.directory}/${project.name}-pom.xml"      */
DECL|field|targetPom
specifier|protected
name|File
name|targetPom
decl_stmt|;
comment|/**      * The user configuration      *      * @parameter      * @readonly      */
DECL|field|dependencies
specifier|protected
name|DependencySet
name|dependencies
decl_stmt|;
comment|/**      * The conflict checks configured by the user      *      * @parameter      * @readonly      */
DECL|field|checkConflicts
specifier|protected
name|ExternalBomConflictCheckSet
name|checkConflicts
decl_stmt|;
comment|/**      * Used to look up Artifacts in the remote repository.      *      * @component role="org.apache.maven.artifact.factory.ArtifactFactory"      * @required      * @readonly      */
DECL|field|artifactFactory
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * Used to look up Artifacts in the remote repository.      *      * @component role="org.apache.maven.artifact.resolver.ArtifactResolver"      * @required      * @readonly      */
DECL|field|artifactResolver
specifier|protected
name|ArtifactResolver
name|artifactResolver
decl_stmt|;
comment|/**      * List of Remote Repositories used by the resolver      *      * @parameter property="project.remoteArtifactRepositories"      * @readonly      * @required      */
DECL|field|remoteRepositories
specifier|protected
name|List
name|remoteRepositories
decl_stmt|;
comment|/**      * Location of the local repository.      *      * @parameter property="localRepository"      * @readonly      * @required      */
DECL|field|localRepository
specifier|protected
name|ArtifactRepository
name|localRepository
decl_stmt|;
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
try|try
block|{
name|DependencyManagement
name|mng
init|=
name|project
operator|.
name|getDependencyManagement
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Dependency
argument_list|>
name|filteredDependencies
init|=
name|enhance
argument_list|(
name|filter
argument_list|(
name|mng
operator|.
name|getDependencies
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|externallyManagedDependencies
init|=
name|getExternallyManagedDependencies
argument_list|()
decl_stmt|;
name|checkConflictsWithExternalBoms
argument_list|(
name|filteredDependencies
argument_list|,
name|externallyManagedDependencies
argument_list|)
expr_stmt|;
name|Document
name|pom
init|=
name|loadBasePom
argument_list|()
decl_stmt|;
comment|// transform
name|overwriteDependencyManagement
argument_list|(
name|pom
argument_list|,
name|filteredDependencies
argument_list|)
expr_stmt|;
name|writePom
argument_list|(
name|pom
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoFailureException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Cannot generate the output BOM file"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
DECL|method|enhance (List<Dependency> dependencyList)
specifier|private
name|List
argument_list|<
name|Dependency
argument_list|>
name|enhance
parameter_list|(
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencyList
parameter_list|)
block|{
for|for
control|(
name|Dependency
name|dep
range|:
name|dependencyList
control|)
block|{
if|if
condition|(
name|dep
operator|.
name|getGroupId
argument_list|()
operator|.
name|startsWith
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|&&
name|project
operator|.
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|dep
operator|.
name|setVersion
argument_list|(
literal|"${project.version}"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dependencyList
return|;
block|}
DECL|method|filter (List<Dependency> dependencyList)
specifier|private
name|List
argument_list|<
name|Dependency
argument_list|>
name|filter
parameter_list|(
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencyList
parameter_list|)
block|{
name|List
argument_list|<
name|Dependency
argument_list|>
name|outDependencies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|DependencyMatcher
name|inclusions
init|=
operator|new
name|DependencyMatcher
argument_list|(
name|dependencies
operator|.
name|getIncludes
argument_list|()
argument_list|)
decl_stmt|;
name|DependencyMatcher
name|exclusions
init|=
operator|new
name|DependencyMatcher
argument_list|(
name|dependencies
operator|.
name|getExcludes
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Dependency
name|dep
range|:
name|dependencyList
control|)
block|{
name|boolean
name|accept
init|=
name|inclusions
operator|.
name|matches
argument_list|(
name|dep
argument_list|)
operator|&&
operator|!
name|exclusions
operator|.
name|matches
argument_list|(
name|dep
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|dep
operator|+
operator|(
name|accept
condition|?
literal|" included in the BOM"
else|:
literal|" excluded from BOM"
operator|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|accept
condition|)
block|{
name|outDependencies
operator|.
name|add
argument_list|(
name|dep
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|outDependencies
argument_list|,
parameter_list|(
name|d1
parameter_list|,
name|d2
parameter_list|)
lambda|->
operator|(
name|d1
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|d1
operator|.
name|getArtifactId
argument_list|()
operator|)
operator|.
name|compareTo
argument_list|(
name|d2
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|d2
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|outDependencies
return|;
block|}
DECL|method|loadBasePom ()
specifier|private
name|Document
name|loadBasePom
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilder
name|builder
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|pom
init|=
name|builder
operator|.
name|parse
argument_list|(
name|sourcePom
argument_list|)
decl_stmt|;
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|XPathExpression
name|parentVersion
init|=
name|xpath
operator|.
name|compile
argument_list|(
literal|"/project/parent/version"
argument_list|)
decl_stmt|;
name|setActualVersion
argument_list|(
name|pom
argument_list|,
name|parentVersion
argument_list|)
expr_stmt|;
name|XPathExpression
name|projectVersion
init|=
name|xpath
operator|.
name|compile
argument_list|(
literal|"/project/version"
argument_list|)
decl_stmt|;
name|setActualVersion
argument_list|(
name|pom
argument_list|,
name|projectVersion
argument_list|)
expr_stmt|;
return|return
name|pom
return|;
block|}
DECL|method|setActualVersion (Document pom, XPathExpression path)
specifier|private
name|void
name|setActualVersion
parameter_list|(
name|Document
name|pom
parameter_list|,
name|XPathExpression
name|path
parameter_list|)
throws|throws
name|XPathExpressionException
block|{
name|Node
name|node
init|=
operator|(
name|Node
operator|)
name|path
operator|.
name|evaluate
argument_list|(
name|pom
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
operator|&&
name|node
operator|.
name|getTextContent
argument_list|()
operator|!=
literal|null
operator|&&
name|node
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|"${project.version}"
argument_list|)
condition|)
block|{
name|node
operator|.
name|setTextContent
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writePom (Document pom)
specifier|private
name|void
name|writePom
parameter_list|(
name|Document
name|pom
parameter_list|)
throws|throws
name|Exception
block|{
name|XPathExpression
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
operator|.
name|compile
argument_list|(
literal|"//text()[normalize-space(.) = '']"
argument_list|)
decl_stmt|;
name|NodeList
name|emptyNodes
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
name|pom
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
comment|// Remove empty text nodes
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|emptyNodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|emptyNode
init|=
name|emptyNodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|emptyNode
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|emptyNode
argument_list|)
expr_stmt|;
block|}
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|METHOD
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|pom
argument_list|)
decl_stmt|;
name|targetPom
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|String
name|content
decl_stmt|;
try|try
init|(
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
init|)
block|{
name|StreamResult
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|out
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
name|content
operator|=
name|out
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
comment|// Fix header formatting problem
name|content
operator|=
name|content
operator|.
name|replaceFirst
argument_list|(
literal|"-->"
argument_list|,
literal|"-->\n"
argument_list|)
expr_stmt|;
name|writeFileIfChanged
argument_list|(
name|content
argument_list|,
name|targetPom
argument_list|)
expr_stmt|;
block|}
DECL|method|writeFileIfChanged (String content, File file)
specifier|private
name|void
name|writeFileIfChanged
parameter_list|(
name|String
name|content
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|write
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
init|(
name|FileReader
name|fr
init|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
init|)
block|{
name|String
name|oldContent
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|fr
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|content
operator|.
name|equals
argument_list|(
name|oldContent
argument_list|)
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Writing new file "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|fr
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"File "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" left unchanged"
argument_list|)
expr_stmt|;
name|write
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|File
name|parent
init|=
name|file
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|parent
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|write
condition|)
block|{
try|try
init|(
name|FileWriter
name|fw
init|=
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
init|)
block|{
name|IOUtils
operator|.
name|write
argument_list|(
name|content
argument_list|,
name|fw
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|overwriteDependencyManagement (Document pom, List<Dependency> dependencies)
specifier|private
name|void
name|overwriteDependencyManagement
parameter_list|(
name|Document
name|pom
parameter_list|,
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
parameter_list|)
throws|throws
name|Exception
block|{
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|XPathExpression
name|expr
init|=
name|xpath
operator|.
name|compile
argument_list|(
literal|"/project/dependencyManagement/dependencies"
argument_list|)
decl_stmt|;
name|NodeList
name|nodes
init|=
operator|(
name|NodeList
operator|)
name|expr
operator|.
name|evaluate
argument_list|(
name|pom
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodes
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No dependencies found in the dependencyManagement section of the current pom"
argument_list|)
throw|;
block|}
name|Node
name|dependenciesSection
init|=
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// cleanup the dependency management section
while|while
condition|(
name|dependenciesSection
operator|.
name|hasChildNodes
argument_list|()
condition|)
block|{
name|Node
name|child
init|=
name|dependenciesSection
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|dependenciesSection
operator|.
name|removeChild
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Dependency
name|dep
range|:
name|dependencies
control|)
block|{
name|Element
name|dependencyEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"dependency"
argument_list|)
decl_stmt|;
name|Element
name|groupIdEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|groupIdEl
operator|.
name|setTextContent
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyEl
operator|.
name|appendChild
argument_list|(
name|groupIdEl
argument_list|)
expr_stmt|;
name|Element
name|artifactIdEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"artifactId"
argument_list|)
decl_stmt|;
name|artifactIdEl
operator|.
name|setTextContent
argument_list|(
name|dep
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyEl
operator|.
name|appendChild
argument_list|(
name|artifactIdEl
argument_list|)
expr_stmt|;
name|Element
name|versionEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"version"
argument_list|)
decl_stmt|;
name|versionEl
operator|.
name|setTextContent
argument_list|(
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyEl
operator|.
name|appendChild
argument_list|(
name|versionEl
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"jar"
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|typeEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|typeEl
operator|.
name|setTextContent
argument_list|(
name|dep
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyEl
operator|.
name|appendChild
argument_list|(
name|typeEl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dep
operator|.
name|getClassifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Element
name|classifierEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"classifier"
argument_list|)
decl_stmt|;
name|classifierEl
operator|.
name|setTextContent
argument_list|(
name|dep
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyEl
operator|.
name|appendChild
argument_list|(
name|classifierEl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dep
operator|.
name|getScope
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|"compile"
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|scopeEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"scope"
argument_list|)
decl_stmt|;
name|scopeEl
operator|.
name|setTextContent
argument_list|(
name|dep
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyEl
operator|.
name|appendChild
argument_list|(
name|scopeEl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dep
operator|.
name|getExclusions
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|dep
operator|.
name|getExclusions
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Element
name|exclsEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"exclusions"
argument_list|)
decl_stmt|;
for|for
control|(
name|Exclusion
name|e
range|:
name|dep
operator|.
name|getExclusions
argument_list|()
control|)
block|{
name|Element
name|exclEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"exclusion"
argument_list|)
decl_stmt|;
name|Element
name|groupIdExEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|groupIdExEl
operator|.
name|setTextContent
argument_list|(
name|e
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|exclEl
operator|.
name|appendChild
argument_list|(
name|groupIdExEl
argument_list|)
expr_stmt|;
name|Element
name|artifactIdExEl
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"artifactId"
argument_list|)
decl_stmt|;
name|artifactIdExEl
operator|.
name|setTextContent
argument_list|(
name|e
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|exclEl
operator|.
name|appendChild
argument_list|(
name|artifactIdExEl
argument_list|)
expr_stmt|;
name|exclsEl
operator|.
name|appendChild
argument_list|(
name|exclEl
argument_list|)
expr_stmt|;
block|}
name|dependencyEl
operator|.
name|appendChild
argument_list|(
name|exclsEl
argument_list|)
expr_stmt|;
block|}
name|dependenciesSection
operator|.
name|appendChild
argument_list|(
name|dependencyEl
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkConflictsWithExternalBoms (Collection<Dependency> dependencies, Set<String> external)
specifier|private
name|void
name|checkConflictsWithExternalBoms
parameter_list|(
name|Collection
argument_list|<
name|Dependency
argument_list|>
name|dependencies
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|external
parameter_list|)
throws|throws
name|MojoFailureException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|errors
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Dependency
name|d
range|:
name|dependencies
control|)
block|{
name|String
name|key
init|=
name|comparisonKey
argument_list|(
name|d
argument_list|)
decl_stmt|;
if|if
condition|(
name|external
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|errors
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|errors
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"Found "
argument_list|)
operator|.
name|append
argument_list|(
name|errors
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" conflicts between the current managed dependencies and the external BOMS:\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|error
range|:
name|errors
control|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|" - "
argument_list|)
operator|.
name|append
argument_list|(
name|error
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|MojoFailureException
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|getExternallyManagedDependencies ()
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getExternallyManagedDependencies
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|provided
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|checkConflicts
operator|!=
literal|null
operator|&&
name|checkConflicts
operator|.
name|getBoms
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ExternalBomConflictCheck
name|check
range|:
name|checkConflicts
operator|.
name|getBoms
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|bomProvided
init|=
name|getProvidedDependencyManagement
argument_list|(
name|check
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|check
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|check
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|provided
operator|.
name|addAll
argument_list|(
name|bomProvided
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|provided
return|;
block|}
DECL|method|getProvidedDependencyManagement (String groupId, String artifactId, String version)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getProvidedDependencyManagement
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getProvidedDependencyManagement
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
operator|new
name|TreeSet
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getProvidedDependencyManagement (String groupId, String artifactId, String version, Set<String> gaChecked)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getProvidedDependencyManagement
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|gaChecked
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|ga
init|=
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
decl_stmt|;
name|gaChecked
operator|.
name|add
argument_list|(
name|ga
argument_list|)
expr_stmt|;
name|Artifact
name|bom
init|=
name|resolveArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|MavenProject
name|bomProject
init|=
name|loadExternalProjectPom
argument_list|(
name|bom
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|provided
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|bomProject
operator|.
name|getDependencyManagement
argument_list|()
operator|!=
literal|null
operator|&&
name|bomProject
operator|.
name|getDependencyManagement
argument_list|()
operator|.
name|getDependencies
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Dependency
name|dep
range|:
name|bomProject
operator|.
name|getDependencyManagement
argument_list|()
operator|.
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
literal|"pom"
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
literal|"import"
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|subGa
init|=
name|dep
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|dep
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|gaChecked
operator|.
name|contains
argument_list|(
name|subGa
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|sub
init|=
name|getProvidedDependencyManagement
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dep
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|resolveVersion
argument_list|(
name|bomProject
argument_list|,
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|,
name|gaChecked
argument_list|)
decl_stmt|;
name|provided
operator|.
name|addAll
argument_list|(
name|sub
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|provided
operator|.
name|add
argument_list|(
name|comparisonKey
argument_list|(
name|dep
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|provided
return|;
block|}
DECL|method|resolveVersion (MavenProject project, String version)
specifier|private
name|String
name|resolveVersion
parameter_list|(
name|MavenProject
name|project
parameter_list|,
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|version
operator|.
name|contains
argument_list|(
literal|"${"
argument_list|)
condition|)
block|{
name|int
name|start
init|=
name|version
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|version
operator|.
name|indexOf
argument_list|(
literal|"}"
argument_list|)
decl_stmt|;
if|if
condition|(
name|end
operator|>
name|start
condition|)
block|{
name|String
name|prop
init|=
name|version
operator|.
name|substring
argument_list|(
name|start
operator|+
literal|2
argument_list|,
name|end
argument_list|)
decl_stmt|;
name|String
name|resolved
init|=
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
name|prop
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolved
operator|!=
literal|null
condition|)
block|{
name|version
operator|=
name|version
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|start
argument_list|)
operator|+
name|resolved
operator|+
name|version
operator|.
name|substring
argument_list|(
name|end
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|version
return|;
block|}
DECL|method|comparisonKey (Dependency dependency)
specifier|private
name|String
name|comparisonKey
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
return|return
name|dependency
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|dependency
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|":"
operator|+
operator|(
name|dependency
operator|.
name|getType
argument_list|()
operator|!=
literal|null
condition|?
name|dependency
operator|.
name|getType
argument_list|()
else|:
literal|"jar"
operator|)
return|;
block|}
DECL|method|resolveArtifact (String groupId, String artifactId, String version, String type)
specifier|private
name|Artifact
name|resolveArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Artifact
name|art
init|=
name|artifactFactory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"runtime"
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|artifactResolver
operator|.
name|resolve
argument_list|(
name|art
argument_list|,
name|remoteRepositories
argument_list|,
name|localRepository
argument_list|)
expr_stmt|;
return|return
name|art
return|;
block|}
DECL|method|loadExternalProjectPom (File pomFile)
specifier|private
name|MavenProject
name|loadExternalProjectPom
parameter_list|(
name|File
name|pomFile
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|FileReader
name|reader
init|=
operator|new
name|FileReader
argument_list|(
name|pomFile
argument_list|)
init|)
block|{
name|MavenXpp3Reader
name|mavenReader
init|=
operator|new
name|MavenXpp3Reader
argument_list|()
decl_stmt|;
name|Model
name|model
init|=
name|mavenReader
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|MavenProject
name|project
init|=
operator|new
name|MavenProject
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|project
operator|.
name|setFile
argument_list|(
name|pomFile
argument_list|)
expr_stmt|;
return|return
name|project
return|;
block|}
block|}
block|}
end_class

end_unit

