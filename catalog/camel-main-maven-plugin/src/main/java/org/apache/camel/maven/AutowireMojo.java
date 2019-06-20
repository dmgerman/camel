begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|catalog
operator|.
name|CamelCatalog
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
name|catalog
operator|.
name|JSonSchemaHelper
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
name|support
operator|.
name|IntrospectionSupport
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
name|support
operator|.
name|PatternHelper
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
name|IOHelper
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
name|OrderedProperties
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
name|StringHelper
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
name|LifecyclePhase
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
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reflections
operator|.
name|Reflections
import|;
end_import

begin_comment
comment|/**  * Pre scans your project and prepare autowiring by classpath scanning  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"autowire"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PROCESS_CLASSES
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|COMPILE
argument_list|)
DECL|class|AutowireMojo
specifier|public
class|class
name|AutowireMojo
extends|extends
name|AbstractMainMojo
block|{
comment|/**      * When autowiring has detected multiple implementations (2 or more) of a given interface, which      * cannot be mapped, should they be logged so you can see and add manual mapping if needed.      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.logUnmapped"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|logUnmapped
specifier|protected
name|boolean
name|logUnmapped
decl_stmt|;
comment|/**      * The output directory for generated autowire file      */
annotation|@
name|Parameter
argument_list|(
name|readonly
operator|=
literal|true
argument_list|,
name|defaultValue
operator|=
literal|"${project.build.directory}/classes/META-INF/services/org/apache/camel/"
argument_list|)
DECL|field|outFolder
specifier|protected
name|File
name|outFolder
decl_stmt|;
comment|/**      * To exclude autowiring specific properties with these key names.      * You can also configure a single entry and separate the excludes with comma      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.exclude"
argument_list|)
DECL|field|exclude
specifier|protected
name|String
index|[]
name|exclude
decl_stmt|;
comment|/**      * To include autowiring specific properties or component with these key names.      * You can also configure a single entry and separate the includes with comma      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.include"
argument_list|)
DECL|field|include
specifier|protected
name|String
index|[]
name|include
decl_stmt|;
comment|/**      * To setup special mappings between known types as key=value pairs.      * You can also configure a single entry and separate the mappings with comma      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.mappings"
argument_list|)
DECL|field|mappings
specifier|protected
name|String
index|[]
name|mappings
decl_stmt|;
comment|/**      * Optional mappings file loaded from classpath, with mapping that override any default mappings      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/classes/camel-main-mappings.properties"
argument_list|)
DECL|field|mappingsFile
specifier|protected
name|File
name|mappingsFile
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
comment|// perform common tasks
name|super
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// load default mappings
name|Properties
name|mappingProperties
init|=
name|loadDefaultMappings
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Loaded default-mappings: "
operator|+
name|mappingProperties
argument_list|)
expr_stmt|;
comment|// add extra mappings
if|if
condition|(
name|this
operator|.
name|mappings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|m
range|:
name|this
operator|.
name|mappings
control|)
block|{
name|String
name|key
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|m
argument_list|,
literal|"="
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|m
argument_list|,
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|mappingProperties
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Added mapping from pom.xml: "
operator|+
name|key
operator|+
literal|"="
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Properties
name|mappingFileProperties
init|=
name|loadMappingsFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|mappingFileProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Loaded mappings file: "
operator|+
name|mappingsFile
operator|+
literal|" with mappings: "
operator|+
name|mappingFileProperties
argument_list|)
expr_stmt|;
name|mappingProperties
operator|.
name|putAll
argument_list|(
name|mappingFileProperties
argument_list|)
expr_stmt|;
block|}
comment|// find the autowire via classpath scanning
name|List
argument_list|<
name|String
argument_list|>
name|autowires
init|=
name|findAutowireComponentOptionsByClasspath
argument_list|(
name|catalog
argument_list|,
name|camelComponentsOnClasspath
argument_list|,
name|reflections
argument_list|,
name|mappingProperties
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|autowires
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|outFolder
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|outFolder
argument_list|,
literal|"autowire.properties"
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
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"# Generated by camel build tools\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|line
range|:
name|autowires
control|)
block|{
name|fos
operator|.
name|write
argument_list|(
name|line
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
name|IOHelper
operator|.
name|close
argument_list|(
name|fos
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Created file: "
operator|+
name|file
operator|+
literal|" (autowire by classpath: "
operator|+
name|autowires
operator|.
name|size
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Cannot write to file "
operator|+
name|file
operator|+
literal|" due "
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
block|}
DECL|method|loadDefaultMappings ()
specifier|protected
name|Properties
name|loadDefaultMappings
parameter_list|()
throws|throws
name|MojoFailureException
block|{
name|Properties
name|mappings
init|=
operator|new
name|OrderedProperties
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|AutowireMojo
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/default-mappings.properties"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|mappings
operator|.
name|load
argument_list|(
name|is
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
literal|"Cannot load default-mappings.properties from classpath"
argument_list|)
throw|;
block|}
return|return
name|mappings
return|;
block|}
DECL|method|loadMappingsFile ()
specifier|protected
name|Properties
name|loadMappingsFile
parameter_list|()
throws|throws
name|MojoFailureException
block|{
name|Properties
name|mappings
init|=
operator|new
name|OrderedProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|mappingsFile
operator|.
name|exists
argument_list|()
operator|&&
name|mappingsFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|mappingsFile
argument_list|)
decl_stmt|;
name|mappings
operator|.
name|load
argument_list|(
name|is
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
literal|"Cannot load file: "
operator|+
name|mappingsFile
argument_list|)
throw|;
block|}
block|}
return|return
name|mappings
return|;
block|}
DECL|method|findAutowireComponentOptionsByClasspath (CamelCatalog catalog, Set<String> components, Reflections reflections, Properties mappingProperties)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|findAutowireComponentOptionsByClasspath
parameter_list|(
name|CamelCatalog
name|catalog
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|components
parameter_list|,
name|Reflections
name|reflections
parameter_list|,
name|Properties
name|mappingProperties
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|autowires
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|componentName
range|:
name|components
control|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Autowiring Camel component: "
operator|+
name|componentName
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|catalog
operator|.
name|componentJSonSchema
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|==
literal|null
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Cannot find component JSon metadata for component: "
operator|+
name|componentName
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"componentProperties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
name|JSonSchemaHelper
operator|.
name|getNames
argument_list|(
name|rows
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
name|JSonSchemaHelper
operator|.
name|getRow
argument_list|(
name|rows
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|row
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|String
name|javaType
init|=
name|safeJavaType
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"object"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|isValidPropertyName
argument_list|(
name|componentName
argument_list|,
name|name
argument_list|)
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Skipping property name: "
operator|+
name|name
argument_list|)
expr_stmt|;
continue|continue;
block|}
try|try
block|{
name|Class
name|clazz
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|javaType
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|.
name|isInterface
argument_list|()
operator|&&
name|isComplexUserType
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|reflections
operator|.
name|getSubTypesOf
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
comment|// filter classes (must not be interfaces, must be public, must not be abstract, must be top level) and also a valid autowire class
name|classes
operator|=
name|classes
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|c
lambda|->
operator|!
name|c
operator|.
name|isInterface
argument_list|()
operator|&&
name|Modifier
operator|.
name|isPublic
argument_list|(
name|c
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
operator|!
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|c
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|c
operator|.
name|getEnclosingClass
argument_list|()
operator|==
literal|null
operator|&&
name|isValidAutowireClass
argument_list|(
name|c
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
expr_stmt|;
name|Class
name|best
init|=
name|chooseBestKnownType
argument_list|(
name|componentName
argument_list|,
name|name
argument_list|,
name|clazz
argument_list|,
name|classes
argument_list|,
name|mappingProperties
argument_list|)
decl_stmt|;
if|if
condition|(
name|best
operator|!=
literal|null
condition|)
block|{
name|String
name|line
init|=
literal|"camel.component."
operator|+
name|componentName
operator|+
literal|"."
operator|+
name|name
operator|+
literal|"=#class:"
operator|+
name|best
operator|.
name|getName
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|autowires
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
comment|// TODO: get options from best class (getter/setter pairs)
comment|// we dont have documentation
comment|// add as spring boot options
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Cannot load class: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|autowires
return|;
block|}
DECL|method|chooseBestKnownType (String componentName, String optionName, Class type, Set<Class<?>> candidates, Properties knownTypes)
specifier|protected
name|Class
name|chooseBestKnownType
parameter_list|(
name|String
name|componentName
parameter_list|,
name|String
name|optionName
parameter_list|,
name|Class
name|type
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|candidates
parameter_list|,
name|Properties
name|knownTypes
parameter_list|)
block|{
name|String
name|known
init|=
name|knownTypes
operator|.
name|getProperty
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|known
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|k
range|:
name|known
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
control|)
block|{
comment|// special as we should skip this option
if|if
condition|(
literal|"#skip#"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Class
name|found
init|=
name|candidates
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|k
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
return|return
name|found
return|;
block|}
block|}
block|}
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|candidates
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
if|if
condition|(
name|logUnmapped
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Cannot chose best type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" among "
operator|+
name|candidates
operator|.
name|size
argument_list|()
operator|+
literal|" implementations: "
operator|+
name|candidates
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Cannot autowire option camel.component."
operator|+
name|componentName
operator|+
literal|"."
operator|+
name|optionName
operator|+
literal|" as the interface: "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" has "
operator|+
name|candidates
operator|.
name|size
argument_list|()
operator|+
literal|" implementations in the classpath:"
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
name|c
range|:
name|candidates
control|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"\t\t"
operator|+
name|c
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|isValidPropertyName (String componentName, String name)
specifier|protected
name|boolean
name|isValidPropertyName
parameter_list|(
name|String
name|componentName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
comment|// we want to regard names as the same if they are using dash or not, and also to be case insensitive.
name|String
name|prefix
init|=
literal|"camel.component."
operator|+
name|componentName
operator|+
literal|"."
decl_stmt|;
name|name
operator|=
name|StringHelper
operator|.
name|dashToCamelCase
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|exclude
operator|!=
literal|null
operator|&&
name|exclude
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|// works on components too
for|for
control|(
name|String
name|pattern
range|:
name|exclude
control|)
block|{
name|pattern
operator|=
name|pattern
operator|.
name|trim
argument_list|()
expr_stmt|;
name|pattern
operator|=
name|StringHelper
operator|.
name|dashToCamelCase
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|componentName
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|name
argument_list|,
name|pattern
argument_list|)
operator|||
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|include
operator|!=
literal|null
operator|&&
name|include
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|include
control|)
block|{
name|pattern
operator|=
name|pattern
operator|.
name|trim
argument_list|()
expr_stmt|;
name|pattern
operator|=
name|StringHelper
operator|.
name|dashToCamelCase
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|componentName
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|name
argument_list|,
name|pattern
argument_list|)
operator|||
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|// we have include enabled and none matched so it should be false
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|isComplexUserType (Class type)
specifier|private
specifier|static
name|boolean
name|isComplexUserType
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
comment|// lets consider all non java, as complex types
return|return
name|type
operator|!=
literal|null
operator|&&
operator|!
name|type
operator|.
name|isPrimitive
argument_list|()
operator|&&
operator|!
name|type
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"java."
argument_list|)
return|;
block|}
DECL|method|isValidAutowireClass (Class clazz)
specifier|protected
name|boolean
name|isValidAutowireClass
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
comment|// skip all from Apache Camel and regular JDK as they would be default anyway
return|return
operator|!
name|clazz
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.apache.camel"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

