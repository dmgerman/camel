begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.nexus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|nexus
package|;
end_package

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|CatalogHelper
operator|.
name|loadText
import|;
end_import

begin_comment
comment|/**  * Nexus repository that can scan for custom Camel components and add to the {@link org.apache.camel.catalog.CamelCatalog}.  */
end_comment

begin_class
DECL|class|ComponentNexusRepository
specifier|public
class|class
name|ComponentNexusRepository
extends|extends
name|BaseNexusRepository
block|{
DECL|method|ComponentNexusRepository ()
specifier|public
name|ComponentNexusRepository
parameter_list|()
block|{
name|super
argument_list|(
literal|"component"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onNewArtifacts (Set<NexusArtifactDto> newArtifacts)
specifier|public
name|void
name|onNewArtifacts
parameter_list|(
name|Set
argument_list|<
name|NexusArtifactDto
argument_list|>
name|newArtifacts
parameter_list|)
block|{
comment|// now download the new artifact JARs and look inside to find more details
for|for
control|(
name|NexusArtifactDto
name|dto
range|:
name|newArtifacts
control|)
block|{
try|try
block|{
comment|// download using url classloader reader
name|URL
name|jarUrl
init|=
operator|new
name|URL
argument_list|(
name|dto
operator|.
name|getArtifactLink
argument_list|()
argument_list|)
decl_stmt|;
name|addCustomCamelComponentsFromArtifact
argument_list|(
name|dto
argument_list|,
name|jarUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error downloading component JAR "
operator|+
name|dto
operator|.
name|getArtifactLink
argument_list|()
operator|+
literal|". This exception is ignored. "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Adds any discovered third party Camel components from the artifact.      */
DECL|method|addCustomCamelComponentsFromArtifact (NexusArtifactDto dto, URL jarUrl )
specifier|private
name|void
name|addCustomCamelComponentsFromArtifact
parameter_list|(
name|NexusArtifactDto
name|dto
parameter_list|,
name|URL
name|jarUrl
parameter_list|)
block|{
try|try
init|(
name|URLClassLoader
name|classLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|jarUrl
block|}
argument_list|)
init|;
init|)
block|{
comment|// is there any custom Camel components in this library?
name|Properties
name|properties
init|=
name|loadComponentProperties
argument_list|(
name|classLoader
argument_list|)
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|String
name|components
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"components"
argument_list|)
decl_stmt|;
if|if
condition|(
name|components
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|part
init|=
name|components
operator|.
name|split
argument_list|(
literal|"\\s"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|scheme
range|:
name|part
control|)
block|{
if|if
condition|(
operator|!
name|getCamelCatalog
argument_list|()
operator|.
name|findComponentNames
argument_list|()
operator|.
name|contains
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
comment|// find the class name
name|String
name|javaType
init|=
name|extractComponentJavaType
argument_list|(
name|classLoader
argument_list|,
name|scheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|javaType
operator|!=
literal|null
condition|)
block|{
name|String
name|json
init|=
name|loadComponentJSonSchema
argument_list|(
name|classLoader
argument_list|,
name|scheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Added component: {}:{}:{} to Camel Catalog"
argument_list|,
name|dto
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dto
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dto
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|getCamelCatalog
argument_list|()
operator|.
name|addComponent
argument_list|(
name|scheme
argument_list|,
name|javaType
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error scanning JAR for custom Camel components"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|loadComponentProperties (URLClassLoader classLoader)
specifier|private
name|Properties
name|loadComponentProperties
parameter_list|(
name|URLClassLoader
name|classLoader
parameter_list|)
block|{
name|Properties
name|answer
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
comment|// load the component files using the recommended way by a component.properties file
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
literal|"META-INF/services/org/apache/camel/component.properties"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|answer
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
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error loading META-INF/services/org/apache/camel/component.properties file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|extractComponentJavaType (URLClassLoader classLoader, String scheme)
specifier|private
name|String
name|extractComponentJavaType
parameter_list|(
name|URLClassLoader
name|classLoader
parameter_list|,
name|String
name|scheme
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
literal|"META-INF/services/org/apache/camel/component/"
operator|+
name|scheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
return|return
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error loading META-INF/services/org/apache/camel/component/"
operator|+
name|scheme
operator|+
literal|" file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|loadComponentJSonSchema (URLClassLoader classLoader, String scheme)
specifier|private
name|String
name|loadComponentJSonSchema
parameter_list|(
name|URLClassLoader
name|classLoader
parameter_list|,
name|String
name|scheme
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
name|String
name|path
init|=
literal|null
decl_stmt|;
name|String
name|javaType
init|=
name|extractComponentJavaType
argument_list|(
name|classLoader
argument_list|,
name|scheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|javaType
operator|!=
literal|null
condition|)
block|{
name|int
name|pos
init|=
name|javaType
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|path
operator|=
name|javaType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|+
literal|"/"
operator|+
name|scheme
operator|+
literal|".json"
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|loadText
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error loading "
operator|+
name|path
operator|+
literal|" file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

