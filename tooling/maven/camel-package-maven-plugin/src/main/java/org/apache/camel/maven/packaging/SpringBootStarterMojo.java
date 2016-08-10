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
name|ByteArrayInputStream
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
name|InputStream
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
name|io
operator|.
name|Writer
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
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|com
operator|.
name|sun
operator|.
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|internal
operator|.
name|serialize
operator|.
name|OutputFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|internal
operator|.
name|serialize
operator|.
name|XMLSerializer
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
name|metadata
operator|.
name|ArtifactMetadataSource
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
name|ArtifactCollector
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
name|filter
operator|.
name|ArtifactFilter
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
name|filter
operator|.
name|ScopeArtifactFilter
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyNode
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilder
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilderException
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|traversal
operator|.
name|CollectingDependencyNodeVisitor
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|cache
operator|.
name|URLTemplateLoader
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|template
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|template
operator|.
name|Template
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|template
operator|.
name|TemplateException
import|;
end_import

begin_comment
comment|/**  * Generate Spring Boot starter for the component  *  * @goal prepare-spring-boot-starter  */
end_comment

begin_class
DECL|class|SpringBootStarterMojo
specifier|public
class|class
name|SpringBootStarterMojo
extends|extends
name|AbstractMojo
block|{
comment|// TO ADD?: "camel-chronicle", "camel-guava-eventbus" ?, "camel-johnzon", "camel-ribbon"
DECL|field|IGNORE_MODULES
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|IGNORE_MODULES
init|=
block|{
comment|/* OSGi -> */
literal|"camel-core-osgi"
block|,
literal|"camel-eventadmin"
block|,
literal|"camel-paxlogging"
block|,
comment|/* deprecated (and not working perfectly) -> */
literal|"camel-swagger"
block|,
literal|"camel-mina"
block|,
comment|/* others (not managed) -> */
literal|"camel-zipkin"
block|}
decl_stmt|;
DECL|field|IGNORE_TEST_MODULES
specifier|private
specifier|static
specifier|final
name|boolean
name|IGNORE_TEST_MODULES
init|=
literal|true
decl_stmt|;
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The project directory      *      * @parameter default-value="${basedir}"      */
DECL|field|baseDir
specifier|protected
name|File
name|baseDir
decl_stmt|;
comment|/**      * @component      * @required      * @readonly      */
DECL|field|artifactFactory
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * @component      * @required      * @readonly      */
DECL|field|artifactMetadataSource
specifier|protected
name|ArtifactMetadataSource
name|artifactMetadataSource
decl_stmt|;
comment|/**      * @component      * @required      * @readonly      */
DECL|field|artifactCollector
specifier|protected
name|ArtifactCollector
name|artifactCollector
decl_stmt|;
comment|/**      * @component      * @required      * @readonly      */
DECL|field|treeBuilder
specifier|protected
name|DependencyTreeBuilder
name|treeBuilder
decl_stmt|;
comment|/**      * @parameter default-value="${localRepository}"      * @readonly      * @required      */
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
if|if
condition|(
operator|!
name|isStarterAllowed
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: starter not allowed for module "
operator|+
name|project
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|": skipping."
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
comment|// create the starter directory
name|File
name|starterDir
init|=
name|starterDir
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: starter dir for the component is: "
operator|+
name|starterDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|starterDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|starterDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|// create the base pom.xml
name|Document
name|pom
init|=
name|createBasePom
argument_list|()
decl_stmt|;
comment|// Apply changes to the starter pom
name|fixLoggingDependencies
argument_list|(
name|pom
argument_list|)
expr_stmt|;
name|includeAdditionalDependencies
argument_list|(
name|pom
argument_list|)
expr_stmt|;
comment|// Write the starter pom
name|File
name|pomFile
init|=
operator|new
name|File
argument_list|(
name|starterDir
argument_list|,
literal|"pom.xml"
argument_list|)
decl_stmt|;
name|writeXmlFormatted
argument_list|(
name|pom
argument_list|,
name|pomFile
argument_list|)
expr_stmt|;
comment|// write LICENSE, USAGE and spring.provides files
name|writeStaticFiles
argument_list|()
expr_stmt|;
name|writeSpringProvides
argument_list|()
expr_stmt|;
comment|// synchronized all starters with their parent pom 'modules' section
name|synchronizeParentPom
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Unable to create starter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|starterDir ()
specifier|private
name|File
name|starterDir
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|SpringBootHelper
operator|.
name|starterDir
argument_list|(
name|baseDir
argument_list|,
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|allStartersDir ()
specifier|private
name|File
name|allStartersDir
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|SpringBootHelper
operator|.
name|allStartersDir
argument_list|(
name|baseDir
argument_list|)
return|;
block|}
DECL|method|includeAdditionalDependencies (Document pom)
specifier|private
name|void
name|includeAdditionalDependencies
parameter_list|(
name|Document
name|pom
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/spring-boot-additional-dependencies.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|deps
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|deps
operator|!=
literal|null
operator|&&
name|deps
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: the following dependencies will be added to the starter: "
operator|+
name|deps
argument_list|)
expr_stmt|;
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
name|Node
name|dependencies
init|=
operator|(
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|compile
argument_list|(
literal|"/project/dependencies"
argument_list|)
operator|.
name|evaluate
argument_list|(
name|pom
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
operator|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|dep
range|:
name|deps
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|Element
name|dependency
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"dependency"
argument_list|)
decl_stmt|;
name|dependencies
operator|.
name|appendChild
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|String
index|[]
name|comps
init|=
name|dep
operator|.
name|split
argument_list|(
literal|"\\:"
argument_list|)
decl_stmt|;
name|String
name|groupIdStr
init|=
name|comps
index|[
literal|0
index|]
decl_stmt|;
name|String
name|artifactIdStr
init|=
name|comps
index|[
literal|1
index|]
decl_stmt|;
name|String
name|versionStr
init|=
name|comps
operator|.
name|length
operator|>
literal|2
condition|?
name|comps
index|[
literal|2
index|]
else|:
literal|null
decl_stmt|;
name|Element
name|groupId
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|groupId
operator|.
name|setTextContent
argument_list|(
name|groupIdStr
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|appendChild
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|Element
name|artifactId
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"artifactId"
argument_list|)
decl_stmt|;
name|artifactId
operator|.
name|setTextContent
argument_list|(
name|artifactIdStr
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|appendChild
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
if|if
condition|(
name|versionStr
operator|!=
literal|null
condition|)
block|{
name|Element
name|version
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"version"
argument_list|)
decl_stmt|;
name|version
operator|.
name|setTextContent
argument_list|(
name|versionStr
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|appendChild
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|fixLoggingDependencies (Document pom)
specifier|private
name|void
name|fixLoggingDependencies
parameter_list|(
name|Document
name|pom
parameter_list|)
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|loggingImpl
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"commons-logging:commons-logging"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"log4j:log4j"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"log4j:apache-log4j-extras"
argument_list|)
expr_stmt|;
comment|// removing also the default implementation
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"ch.qos.logback:logback-core"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"ch.qos.logback:logback-classic"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"org.apache.logging.log4j:log4j"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"org.slf4j:slf4j-jcl"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"org.slf4j:slf4j-jdk14"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"org.slf4j:slf4j-log4j12"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"org.slf4j:slf4j-log4j13"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"org.slf4j:slf4j-nop"
argument_list|)
expr_stmt|;
name|loggingImpl
operator|.
name|add
argument_list|(
literal|"org.slf4j:slf4j-simple"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|includedLibs
init|=
name|filterIncludedArtifacts
argument_list|(
name|loggingImpl
argument_list|)
decl_stmt|;
if|if
condition|(
name|includedLibs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: the following dependencies will be removed from the starter: "
operator|+
name|includedLibs
argument_list|)
expr_stmt|;
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
name|Node
name|dependency
init|=
operator|(
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|compile
argument_list|(
literal|"/project/dependencies/dependency[artifactId/text() = '"
operator|+
name|project
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"']"
argument_list|)
operator|.
name|evaluate
argument_list|(
name|pom
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
operator|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Element
name|exclusions
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"exclusions"
argument_list|)
decl_stmt|;
name|dependency
operator|.
name|appendChild
argument_list|(
name|exclusions
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|lib
range|:
name|includedLibs
control|)
block|{
name|String
name|groupIdStr
init|=
name|lib
operator|.
name|split
argument_list|(
literal|"\\:"
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|String
name|artifactIdStr
init|=
name|lib
operator|.
name|split
argument_list|(
literal|"\\:"
argument_list|)
index|[
literal|1
index|]
decl_stmt|;
name|Element
name|exclusion
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"exclusion"
argument_list|)
decl_stmt|;
name|Element
name|groupId
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|groupId
operator|.
name|setTextContent
argument_list|(
name|groupIdStr
argument_list|)
expr_stmt|;
name|exclusion
operator|.
name|appendChild
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|Element
name|artifactId
init|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"artifactId"
argument_list|)
decl_stmt|;
name|artifactId
operator|.
name|setTextContent
argument_list|(
name|artifactIdStr
argument_list|)
expr_stmt|;
name|exclusion
operator|.
name|appendChild
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|exclusions
operator|.
name|appendChild
argument_list|(
name|exclusion
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|filterIncludedArtifacts (Set<String> artifacts)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|filterIncludedArtifacts
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|artifacts
parameter_list|)
throws|throws
name|DependencyTreeBuilderException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|included
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|ArtifactFilter
name|artifactFilter
init|=
operator|new
name|ScopeArtifactFilter
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|DependencyNode
name|node
init|=
name|treeBuilder
operator|.
name|buildDependencyTree
argument_list|(
name|project
argument_list|,
name|localRepository
argument_list|,
name|artifactFactory
argument_list|,
name|artifactMetadataSource
argument_list|,
name|artifactFilter
argument_list|,
name|artifactCollector
argument_list|)
decl_stmt|;
name|CollectingDependencyNodeVisitor
name|visitor
init|=
operator|new
name|CollectingDependencyNodeVisitor
argument_list|()
decl_stmt|;
name|node
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DependencyNode
argument_list|>
name|nodes
init|=
name|visitor
operator|.
name|getNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|DependencyNode
name|dependencyNode
range|:
name|nodes
control|)
block|{
name|int
name|state
init|=
name|dependencyNode
operator|.
name|getState
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|dependencyNode
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
if|if
condition|(
name|state
operator|==
name|DependencyNode
operator|.
name|INCLUDED
operator|&&
operator|!
name|Artifact
operator|.
name|SCOPE_TEST
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|canonicalName
init|=
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|artifact
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifacts
operator|.
name|contains
argument_list|(
name|canonicalName
argument_list|)
condition|)
block|{
name|included
operator|.
name|add
argument_list|(
name|canonicalName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|included
return|;
block|}
DECL|method|synchronizeParentPom ()
specifier|private
name|void
name|synchronizeParentPom
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|pomFile
init|=
operator|new
name|File
argument_list|(
name|allStartersDir
argument_list|()
argument_list|,
literal|"pom.xml"
argument_list|)
decl_stmt|;
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
name|pomFile
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
name|Node
name|modules
init|=
operator|(
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|compile
argument_list|(
literal|"/project/modules"
argument_list|)
operator|.
name|evaluate
argument_list|(
name|pom
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
operator|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// cleanup current modules
while|while
condition|(
name|modules
operator|.
name|hasChildNodes
argument_list|()
condition|)
block|{
name|modules
operator|.
name|removeChild
argument_list|(
name|modules
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|File
name|starterDir
range|:
name|Arrays
operator|.
name|asList
argument_list|(
name|allStartersDir
argument_list|()
operator|.
name|listFiles
argument_list|(
parameter_list|(
name|f
parameter_list|,
name|n
parameter_list|)
lambda|->
name|f
operator|.
name|isDirectory
argument_list|()
operator|&&
name|n
operator|.
name|endsWith
argument_list|(
name|SpringBootHelper
operator|.
name|STARTER_SUFFIX
argument_list|)
argument_list|)
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
control|)
block|{
name|Node
name|module
operator|=
name|pom
operator|.
name|createElement
argument_list|(
literal|"module"
argument_list|)
block|;
name|module
operator|.
name|setTextContent
argument_list|(
name|starterDir
operator|.
name|getName
argument_list|()
argument_list|)
block|;
name|modules
operator|.
name|appendChild
argument_list|(
name|module
argument_list|)
block|;         }
name|writeXmlFormatted
argument_list|(
name|pom
argument_list|,
name|pomFile
argument_list|)
expr_stmt|;
block|}
DECL|method|createBasePom ()
specifier|private
name|Document
name|createBasePom
parameter_list|()
throws|throws
name|Exception
block|{
name|Template
name|pomTemplate
init|=
name|getTemplate
argument_list|(
literal|"spring-boot-starter-template-pom.template"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"componentId"
argument_list|,
name|getComponentId
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"componentName"
argument_list|,
name|project
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"componentDescription"
argument_list|,
name|project
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|pomTemplate
operator|.
name|process
argument_list|(
name|props
argument_list|,
name|sw
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
name|sw
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ByteArrayInputStream
name|bin
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|xml
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
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
name|bin
argument_list|)
decl_stmt|;
return|return
name|pom
return|;
block|}
DECL|method|writeStaticFiles ()
specifier|private
name|void
name|writeStaticFiles
parameter_list|()
throws|throws
name|IOException
throws|,
name|TemplateException
block|{
try|try
init|(
name|InputStream
name|isNotice
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/spring-boot-starter-NOTICE.txt"
argument_list|)
init|;
name|FileWriter
name|outNotice
operator|=
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|starterDir
argument_list|()
argument_list|,
literal|"src/main/resources/META-INF/NOTICE.txt"
argument_list|)
argument_list|)
init|;
name|InputStream
name|isLicense
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/spring-boot-starter-LICENSE.txt"
argument_list|)
init|;
name|FileWriter
name|outLicense
operator|=
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|starterDir
argument_list|()
argument_list|,
literal|"src/main/resources/META-INF/LICENSE.txt"
argument_list|)
argument_list|)
init|)
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|isNotice
argument_list|,
name|outNotice
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|isLicense
argument_list|,
name|outLicense
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writeSpringProvides ()
specifier|private
name|void
name|writeSpringProvides
parameter_list|()
throws|throws
name|IOException
throws|,
name|TemplateException
block|{
name|Template
name|fileTemplate
init|=
name|getTemplate
argument_list|(
literal|"spring-boot-starter-template-spring.provides"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|outDir
init|=
operator|new
name|File
argument_list|(
name|starterDir
argument_list|()
argument_list|,
literal|"src/main/resources/META-INF"
argument_list|)
decl_stmt|;
name|outDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
literal|"spring.provides"
argument_list|)
decl_stmt|;
try|try
init|(
name|FileWriter
name|outWriter
init|=
operator|new
name|FileWriter
argument_list|(
name|outFile
argument_list|)
init|)
block|{
name|fileTemplate
operator|.
name|process
argument_list|(
name|props
argument_list|,
name|outWriter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTemplate (String name)
specifier|private
name|Template
name|getTemplate
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Configuration
name|cfg
init|=
operator|new
name|Configuration
argument_list|(
name|Configuration
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setTemplateLoader
argument_list|(
operator|new
name|URLTemplateLoader
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|URL
name|getURL
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|SpringBootStarterMojo
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/"
operator|+
name|name
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setDefaultEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Template
name|template
init|=
name|cfg
operator|.
name|getTemplate
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|template
return|;
block|}
DECL|method|isStarterAllowed ()
specifier|private
name|boolean
name|isStarterAllowed
parameter_list|()
block|{
for|for
control|(
name|String
name|ignored
range|:
name|IGNORE_MODULES
control|)
block|{
if|if
condition|(
name|ignored
operator|.
name|equals
argument_list|(
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: component inside ignore list"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|IGNORE_TEST_MODULES
operator|&&
name|project
operator|.
name|getArtifactId
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"camel-test-"
argument_list|)
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: test components are ignored"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|project
operator|.
name|getPackaging
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|project
operator|.
name|getPackaging
argument_list|()
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: ignored for wrong packaging"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// include 'camel-core'
if|if
condition|(
name|baseDir
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"camel-core"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// Build a starter for all components under the 'components' dir and include submodules ending with '-component'
if|if
condition|(
name|baseDir
operator|.
name|getParentFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"components"
argument_list|)
operator|||
name|baseDir
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-component"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Spring-Boot-Starter: component directory mismatch"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
DECL|method|getComponentId ()
specifier|private
name|String
name|getComponentId
parameter_list|()
block|{
name|String
name|componentName
init|=
name|project
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|componentId
init|=
name|componentName
operator|.
name|replace
argument_list|(
literal|"camel-"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
return|return
name|componentId
return|;
block|}
DECL|method|writeXmlFormatted (Document xml, File destination)
specifier|private
name|void
name|writeXmlFormatted
parameter_list|(
name|Document
name|xml
parameter_list|,
name|File
name|destination
parameter_list|)
throws|throws
name|Exception
block|{
name|OutputFormat
name|format
init|=
operator|new
name|OutputFormat
argument_list|(
name|xml
argument_list|)
decl_stmt|;
name|format
operator|.
name|setLineWidth
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|format
operator|.
name|setIndenting
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|format
operator|.
name|setIndent
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLSerializer
name|serializer
init|=
operator|new
name|XMLSerializer
argument_list|(
name|sw
argument_list|,
name|format
argument_list|)
decl_stmt|;
name|serializer
operator|.
name|serialize
argument_list|(
name|xml
argument_list|)
expr_stmt|;
comment|// Fix the output (cannot find a good serializer)
comment|// The apache header is put in the wrong location
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|lastTagLoc
init|=
name|b
operator|.
name|lastIndexOf
argument_list|(
literal|"<"
argument_list|)
decl_stmt|;
name|int
name|lastCloseHeaderLoc
init|=
name|b
operator|.
name|lastIndexOf
argument_list|(
literal|"-->"
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastCloseHeaderLoc
operator|>
name|lastTagLoc
condition|)
block|{
comment|// The apache header has been put at the end
name|int
name|headerLoc
init|=
name|b
operator|.
name|lastIndexOf
argument_list|(
literal|"<!--"
argument_list|)
decl_stmt|;
name|String
name|apacheHeader
init|=
name|b
operator|.
name|substring
argument_list|(
name|headerLoc
argument_list|,
name|lastCloseHeaderLoc
operator|+
literal|3
argument_list|)
decl_stmt|;
name|b
operator|.
name|delete
argument_list|(
name|headerLoc
argument_list|,
name|lastCloseHeaderLoc
operator|+
literal|3
argument_list|)
expr_stmt|;
name|int
name|pos
init|=
name|b
operator|.
name|indexOf
argument_list|(
literal|"?>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|insert
argument_list|(
name|pos
operator|+
literal|2
argument_list|,
literal|"\n"
operator|+
name|apacheHeader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
name|apacheHeader
argument_list|)
expr_stmt|;
block|}
block|}
try|try
init|(
name|Writer
name|out
init|=
operator|new
name|FileWriter
argument_list|(
name|destination
argument_list|)
init|)
block|{
name|IOUtils
operator|.
name|write
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

