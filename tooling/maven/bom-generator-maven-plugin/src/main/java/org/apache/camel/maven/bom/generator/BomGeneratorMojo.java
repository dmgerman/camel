begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|filter
argument_list|(
name|mng
operator|.
name|getDependencies
argument_list|()
argument_list|)
decl_stmt|;
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
name|LinkedList
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
return|return
name|pom
return|;
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
try|try
init|(
name|FileWriter
name|out
init|=
operator|new
name|FileWriter
argument_list|(
name|targetPom
argument_list|)
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
block|}
end_class

end_unit

