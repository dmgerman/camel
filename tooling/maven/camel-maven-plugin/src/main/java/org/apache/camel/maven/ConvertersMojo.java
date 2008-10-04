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
name|OutputStreamWriter
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Locale
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
name|ResourceBundle
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ReportingTypeConverterLoader
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
name|impl
operator|.
name|ReportingTypeConverterLoader
operator|.
name|TypeMapping
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
name|impl
operator|.
name|ReportingTypeConverterRegistry
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
name|util
operator|.
name|ObjectHelper
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
name|ArtifactNotFoundException
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
name|ArtifactResolutionException
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
name|artifact
operator|.
name|versioning
operator|.
name|InvalidVersionSpecificationException
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
name|versioning
operator|.
name|VersionRange
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
name|doxia
operator|.
name|module
operator|.
name|xhtml
operator|.
name|decoration
operator|.
name|render
operator|.
name|RenderingContext
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
name|doxia
operator|.
name|sink
operator|.
name|Sink
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
name|doxia
operator|.
name|site
operator|.
name|decoration
operator|.
name|Body
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
name|doxia
operator|.
name|site
operator|.
name|decoration
operator|.
name|DecorationModel
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
name|doxia
operator|.
name|site
operator|.
name|decoration
operator|.
name|Skin
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
name|doxia
operator|.
name|siterenderer
operator|.
name|Renderer
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
name|doxia
operator|.
name|siterenderer
operator|.
name|RendererException
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
name|doxia
operator|.
name|siterenderer
operator|.
name|SiteRenderingContext
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
name|doxia
operator|.
name|siterenderer
operator|.
name|sink
operator|.
name|SiteRendererSink
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
name|reporting
operator|.
name|AbstractMavenReport
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
name|reporting
operator|.
name|MavenReportException
import|;
end_import

begin_comment
comment|/**  * Generate report of available type conversions.  *  * @goal converters-report  * @requiresDependencyResolution runtime  * @phase verify  */
end_comment

begin_class
DECL|class|ConvertersMojo
specifier|public
class|class
name|ConvertersMojo
extends|extends
name|AbstractMavenReport
block|{
DECL|field|WIKI_TYPECONVERER_URL
specifier|private
specifier|static
specifier|final
name|String
name|WIKI_TYPECONVERER_URL
init|=
literal|"http://activemq.apache.org/camel/type-converter.html"
decl_stmt|;
DECL|field|CONVERTER_TYPE_STATIC
specifier|private
specifier|static
specifier|final
name|String
name|CONVERTER_TYPE_STATIC
init|=
literal|"org.apache.camel.impl.converter.StaticMethodTypeConverter"
decl_stmt|;
DECL|field|CONVERTER_TYPE_INSTANCE
specifier|private
specifier|static
specifier|final
name|String
name|CONVERTER_TYPE_INSTANCE
init|=
literal|"org.apache.camel.impl.converter.InstanceMethodTypeConverter"
decl_stmt|;
DECL|field|REPORT_METHOD_STATIC
specifier|private
specifier|static
specifier|final
name|String
name|REPORT_METHOD_STATIC
init|=
literal|"STATIC"
decl_stmt|;
DECL|field|REPORT_METHOD_INSTANCE
specifier|private
specifier|static
specifier|final
name|String
name|REPORT_METHOD_INSTANCE
init|=
literal|"INSTANCE"
decl_stmt|;
DECL|field|REPORT_METHOD_UNKNOWN
specifier|private
specifier|static
specifier|final
name|String
name|REPORT_METHOD_UNKNOWN
init|=
literal|"UNKNOWN"
decl_stmt|;
comment|/**      * Remote repositories which will be searched for source attachments.      *      * @parameter expression="${project.remoteArtifactRepositories}"      * @required      * @readonly      */
DECL|field|remoteArtifactRepositories
specifier|protected
name|List
name|remoteArtifactRepositories
decl_stmt|;
comment|/**      * Local maven repository.      *      * @parameter expression="${localRepository}"      * @required      * @readonly      */
DECL|field|localRepository
specifier|protected
name|ArtifactRepository
name|localRepository
decl_stmt|;
comment|/**      * The component that is used to resolve additional artifacts required.      *      * @component      */
DECL|field|artifactResolver
specifier|protected
name|ArtifactResolver
name|artifactResolver
decl_stmt|;
comment|/**      * The component used for creating artifact instances.      *      * @component      */
DECL|field|artifactFactory
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * Base output directory for reports.      *      * @parameter default-value="${project.build.directory}/site"      * @readonly      * @required      */
DECL|field|outputDirectory
specifier|private
name|File
name|outputDirectory
decl_stmt|;
comment|/**      * Reference to Maven 2 Project.      *      * @parameter expression="${project}"      * @required      * @readonly      */
DECL|field|project
specifier|private
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Doxia SiteRenderer.      *      * @component      */
DECL|field|renderer
specifier|private
name|Renderer
name|renderer
decl_stmt|;
comment|/**      * Gets resource bundle for given locale.      *      * @param locale    locale      * @return resource bundle      */
DECL|method|getBundle (final Locale locale)
specifier|protected
name|ResourceBundle
name|getBundle
parameter_list|(
specifier|final
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
literal|"camel-maven-plugin"
argument_list|,
name|locale
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @param locale      *            report locale.      * @return report description.      * @see org.apache.maven.reporting.MavenReport#getDescription(Locale)      */
DECL|method|getDescription (final Locale locale)
specifier|public
name|String
name|getDescription
parameter_list|(
specifier|final
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.description"
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.maven.reporting.MavenReport#getName(Locale)      */
DECL|method|getName (final Locale locale)
specifier|public
name|String
name|getName
parameter_list|(
specifier|final
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.name"
argument_list|)
return|;
block|}
DECL|method|getOutputName ()
specifier|public
name|String
name|getOutputName
parameter_list|()
block|{
return|return
literal|"camel-converters"
return|;
block|}
annotation|@
name|Override
DECL|method|getOutputDirectory ()
specifier|protected
name|String
name|getOutputDirectory
parameter_list|()
block|{
return|return
name|outputDirectory
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getProject ()
specifier|protected
name|MavenProject
name|getProject
parameter_list|()
block|{
return|return
name|this
operator|.
name|project
return|;
block|}
annotation|@
name|Override
DECL|method|getSiteRenderer ()
specifier|protected
name|Renderer
name|getSiteRenderer
parameter_list|()
block|{
return|return
name|renderer
return|;
block|}
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
operator|!
name|canGenerateReport
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
name|DecorationModel
name|model
init|=
operator|new
name|DecorationModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setBody
argument_list|(
operator|new
name|Body
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"outputEncoding"
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"project"
argument_list|,
name|project
argument_list|)
expr_stmt|;
name|Locale
name|locale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|SiteRenderingContext
name|siteContext
init|=
name|renderer
operator|.
name|createContextForSkin
argument_list|(
name|getSkinArtifactFile
argument_list|(
name|model
argument_list|)
argument_list|,
name|attributes
argument_list|,
name|model
argument_list|,
name|getName
argument_list|(
name|locale
argument_list|)
argument_list|,
name|locale
argument_list|)
decl_stmt|;
name|RenderingContext
name|context
init|=
operator|new
name|RenderingContext
argument_list|(
name|getReportOutputDirectory
argument_list|()
argument_list|,
name|getOutputName
argument_list|()
operator|+
literal|".html"
argument_list|)
decl_stmt|;
name|SiteRendererSink
name|sink
init|=
operator|new
name|SiteRendererSink
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|generate
argument_list|(
name|sink
argument_list|,
name|locale
argument_list|)
expr_stmt|;
name|Writer
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|getReportOutputDirectory
argument_list|()
argument_list|,
name|getOutputName
argument_list|()
operator|+
literal|".html"
argument_list|)
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|renderer
operator|.
name|generateDocument
argument_list|(
name|writer
argument_list|,
name|sink
argument_list|,
name|siteContext
argument_list|)
expr_stmt|;
name|renderer
operator|.
name|copyResources
argument_list|(
name|siteContext
argument_list|,
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"src/site/resources"
argument_list|)
argument_list|,
name|outputDirectory
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
name|MojoExecutionException
argument_list|(
literal|"Error copying resources."
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RendererException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error while rendering report."
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|MojoFailureException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Cannot find skin artifact for report."
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|MavenReportException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error generating report."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|executeReport (Locale locale)
specifier|protected
name|void
name|executeReport
parameter_list|(
name|Locale
name|locale
parameter_list|)
throws|throws
name|MavenReportException
block|{
if|if
condition|(
operator|!
name|createOutputDirectory
argument_list|(
name|outputDirectory
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MavenReportException
argument_list|(
literal|"Failed to create report directory "
operator|+
name|outputDirectory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|ClassLoader
name|oldClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
comment|// TODO: this badly needs some refactoring
comment|// mojo.createClassLoader creates a URLClassLoader with whatever is in
comment|// ${project.testClasspathElements}, reason why we don't see all converters
comment|// in the report. First we need a list of classpath elements the user
comment|// could customize via plugin configuration, and elements of that list
comment|// be added to the URLClassLoader. This should also be factored out into
comment|// a utility class.
comment|// TODO: there is some interference with the site plugin that needs investigated.
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|project
operator|.
name|getTestClasspathElements
argument_list|()
decl_stmt|;
name|EmbeddedMojo
name|mojo
init|=
operator|new
name|EmbeddedMojo
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|setClasspathElements
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|ClassLoader
name|newClassLoader
init|=
name|mojo
operator|.
name|createClassLoader
argument_list|(
name|oldClassLoader
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|newClassLoader
argument_list|)
expr_stmt|;
name|ReportingTypeConverterLoader
name|loader
init|=
operator|new
name|ReportingTypeConverterLoader
argument_list|()
decl_stmt|;
name|ReportingTypeConverterRegistry
name|registry
init|=
operator|new
name|ReportingTypeConverterRegistry
argument_list|()
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"FOUND type mapping; count = "
operator|+
name|loader
operator|.
name|getTypeConversions
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
index|[]
name|errors
init|=
name|registry
operator|.
name|getErrors
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|error
range|:
name|errors
control|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
name|generateReport
argument_list|(
name|getSink
argument_list|()
argument_list|,
name|locale
argument_list|,
name|loader
operator|.
name|getTypeConversions
argument_list|()
argument_list|)
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
name|MavenReportException
argument_list|(
literal|"Failed to generate TypeConverters report"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createOutputDirectory (final File outputDir)
specifier|private
name|boolean
name|createOutputDirectory
parameter_list|(
specifier|final
name|File
name|outputDir
parameter_list|)
block|{
if|if
condition|(
name|outputDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|outputDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"File with same name already exists: "
operator|+
name|outputDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|outputDir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Cannot make output directory at: "
operator|+
name|outputDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|getSkinArtifactFile (DecorationModel decoration)
specifier|private
name|File
name|getSkinArtifactFile
parameter_list|(
name|DecorationModel
name|decoration
parameter_list|)
throws|throws
name|MojoFailureException
block|{
name|Skin
name|skin
init|=
name|decoration
operator|.
name|getSkin
argument_list|()
decl_stmt|;
if|if
condition|(
name|skin
operator|==
literal|null
condition|)
block|{
name|skin
operator|=
name|Skin
operator|.
name|getDefaultSkin
argument_list|()
expr_stmt|;
block|}
name|String
name|version
init|=
name|skin
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
decl_stmt|;
try|try
block|{
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|version
operator|=
name|Artifact
operator|.
name|RELEASE_VERSION
expr_stmt|;
block|}
name|VersionRange
name|versionSpec
init|=
name|VersionRange
operator|.
name|createFromVersionSpec
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|artifact
operator|=
name|artifactFactory
operator|.
name|createDependencyArtifact
argument_list|(
name|skin
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|skin
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|versionSpec
argument_list|,
literal|"jar"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|artifactResolver
operator|.
name|resolve
argument_list|(
name|artifact
argument_list|,
name|remoteArtifactRepositories
argument_list|,
name|localRepository
argument_list|)
expr_stmt|;
return|return
name|artifact
operator|.
name|getFile
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InvalidVersionSpecificationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"The skin version '"
operator|+
name|version
operator|+
literal|"' is not valid: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ArtifactResolutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Unable to fink skin: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ArtifactNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"The skin does not exist: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|converterType (String converterClassName)
specifier|private
name|String
name|converterType
parameter_list|(
name|String
name|converterClassName
parameter_list|)
block|{
if|if
condition|(
name|CONVERTER_TYPE_STATIC
operator|.
name|equals
argument_list|(
name|converterClassName
argument_list|)
condition|)
block|{
return|return
name|REPORT_METHOD_STATIC
return|;
block|}
elseif|else
if|if
condition|(
name|CONVERTER_TYPE_INSTANCE
operator|.
name|equals
argument_list|(
name|converterClassName
argument_list|)
condition|)
block|{
return|return
name|REPORT_METHOD_INSTANCE
return|;
block|}
else|else
block|{
return|return
name|REPORT_METHOD_UNKNOWN
return|;
block|}
block|}
DECL|method|generateReport (Sink sink, Locale locale, TypeMapping[] mappings)
specifier|private
name|void
name|generateReport
parameter_list|(
name|Sink
name|sink
parameter_list|,
name|Locale
name|locale
parameter_list|,
name|TypeMapping
index|[]
name|mappings
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|beginReport
argument_list|(
name|sink
argument_list|,
name|locale
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|classes
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
name|packages
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
name|Class
argument_list|<
name|?
argument_list|>
name|prevFrom
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|prevTo
init|=
literal|null
decl_stmt|;
name|sink
operator|.
name|table
argument_list|()
expr_stmt|;
name|tableHeader
argument_list|(
name|sink
argument_list|,
name|locale
argument_list|)
expr_stmt|;
for|for
control|(
name|TypeMapping
name|mapping
range|:
name|mappings
control|)
block|{
name|boolean
name|ignored
init|=
literal|false
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|from
init|=
name|mapping
operator|.
name|getFromType
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|to
init|=
name|mapping
operator|.
name|getToType
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|from
argument_list|,
name|prevFrom
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|to
argument_list|,
name|prevTo
argument_list|)
condition|)
block|{
name|ignored
operator|=
literal|true
expr_stmt|;
block|}
name|prevFrom
operator|=
name|from
expr_stmt|;
name|prevTo
operator|=
name|to
expr_stmt|;
name|Method
name|method
init|=
name|mapping
operator|.
name|getMethod
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|methodClass
init|=
name|method
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
name|String
name|packageName
init|=
name|methodClass
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|packages
operator|.
name|containsKey
argument_list|(
name|packageName
argument_list|)
condition|)
block|{
name|classes
operator|=
name|packages
operator|.
name|get
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|classes
operator|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|packages
operator|.
name|put
argument_list|(
name|packageName
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
name|classes
operator|.
name|add
argument_list|(
name|methodClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ignored
condition|)
block|{
name|sink
operator|.
name|italic
argument_list|()
expr_stmt|;
name|this
operator|.
name|tableRow
argument_list|(
name|sink
argument_list|,
name|from
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|to
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|methodClass
argument_list|,
name|mapping
operator|.
name|getConverterType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sink
operator|.
name|italic_
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|tableRow
argument_list|(
name|sink
argument_list|,
name|from
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|to
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|methodClass
argument_list|,
name|mapping
operator|.
name|getConverterType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|sink
operator|.
name|table_
argument_list|()
expr_stmt|;
name|generatePackageReport
argument_list|(
name|sink
argument_list|,
name|packages
argument_list|)
expr_stmt|;
name|endReport
argument_list|(
name|sink
argument_list|)
expr_stmt|;
block|}
DECL|method|generatePackageReport (Sink sink, Map<String, Set<String>> packages)
specifier|private
name|void
name|generatePackageReport
parameter_list|(
name|Sink
name|sink
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
name|packages
parameter_list|)
block|{
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
name|packages
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sink
operator|.
name|section2
argument_list|()
expr_stmt|;
name|sink
operator|.
name|sectionTitle2
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|sink
operator|.
name|sectionTitle2_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|list
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|clazz
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|sink
operator|.
name|listItem
argument_list|()
expr_stmt|;
name|sink
operator|.
name|anchor
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
name|sink
operator|.
name|anchor_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|listItem_
argument_list|()
expr_stmt|;
block|}
name|sink
operator|.
name|list_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|section2_
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|beginReport (Sink sink, Locale locale)
specifier|private
name|void
name|beginReport
parameter_list|(
name|Sink
name|sink
parameter_list|,
name|Locale
name|locale
parameter_list|)
block|{
name|String
name|title
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.title"
argument_list|)
decl_stmt|;
name|String
name|header
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.header"
argument_list|)
decl_stmt|;
name|String
name|intro
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.intro"
argument_list|)
decl_stmt|;
name|String
name|seealso
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.seealso"
argument_list|)
decl_stmt|;
name|sink
operator|.
name|head
argument_list|()
expr_stmt|;
name|sink
operator|.
name|title
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|sink
operator|.
name|title_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|head_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|body
argument_list|()
expr_stmt|;
name|sink
operator|.
name|section1
argument_list|()
expr_stmt|;
name|sink
operator|.
name|sectionTitle1
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|sink
operator|.
name|sectionTitle1_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|paragraph
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|intro
argument_list|)
expr_stmt|;
name|sink
operator|.
name|paragraph_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|paragraph
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|seealso
argument_list|)
expr_stmt|;
name|sink
operator|.
name|list
argument_list|()
expr_stmt|;
name|sink
operator|.
name|listItem
argument_list|()
expr_stmt|;
name|sink
operator|.
name|link
argument_list|(
name|WIKI_TYPECONVERER_URL
argument_list|)
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|WIKI_TYPECONVERER_URL
argument_list|)
expr_stmt|;
name|sink
operator|.
name|link_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|listItem_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|list_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|paragraph_
argument_list|()
expr_stmt|;
block|}
DECL|method|tableHeader (Sink sink, Locale locale)
specifier|private
name|void
name|tableHeader
parameter_list|(
name|Sink
name|sink
parameter_list|,
name|Locale
name|locale
parameter_list|)
block|{
name|String
name|caption
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.table.caption"
argument_list|)
decl_stmt|;
name|String
name|head1
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.table.head1"
argument_list|)
decl_stmt|;
name|String
name|head2
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.table.head2"
argument_list|)
decl_stmt|;
name|String
name|head3
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.table.head3"
argument_list|)
decl_stmt|;
name|String
name|head4
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.table.head4"
argument_list|)
decl_stmt|;
name|String
name|head5
init|=
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
literal|"report.converters.report.table.head5"
argument_list|)
decl_stmt|;
name|sink
operator|.
name|tableCaption
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|caption
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableCaption_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableRow
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|head1
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|head2
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|head3
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|head4
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|head5
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableHeaderCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableRow
argument_list|()
expr_stmt|;
block|}
DECL|method|tableRow (Sink sink, String from, String to, String method, Class<?> clazz, String type)
specifier|private
name|void
name|tableRow
parameter_list|(
name|Sink
name|sink
parameter_list|,
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|,
name|String
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|sink
operator|.
name|tableRow
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|link
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|clazz
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|sink
operator|.
name|link_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableCell
argument_list|()
expr_stmt|;
name|sink
operator|.
name|text
argument_list|(
name|converterType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|sink
operator|.
name|tableCell_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|tableRow
argument_list|()
expr_stmt|;
block|}
DECL|method|endReport (Sink sink)
specifier|private
name|void
name|endReport
parameter_list|(
name|Sink
name|sink
parameter_list|)
block|{
name|sink
operator|.
name|section1_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|body_
argument_list|()
expr_stmt|;
name|sink
operator|.
name|flush
argument_list|()
expr_stmt|;
name|sink
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

