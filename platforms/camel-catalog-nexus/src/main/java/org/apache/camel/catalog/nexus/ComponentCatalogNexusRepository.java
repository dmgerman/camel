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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|maven
operator|.
name|ComponentArtifactHelper
operator|.
name|extractComponentJavaType
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
name|maven
operator|.
name|ComponentArtifactHelper
operator|.
name|loadComponentJSonSchema
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
name|maven
operator|.
name|ComponentArtifactHelper
operator|.
name|loadComponentProperties
import|;
end_import

begin_comment
comment|/**  * Nexus repository that can scan for custom Camel components and add to the {@link org.apache.camel.catalog.CamelCatalog}.  */
end_comment

begin_class
DECL|class|ComponentCatalogNexusRepository
specifier|public
class|class
name|ComponentCatalogNexusRepository
extends|extends
name|BaseNexusRepository
block|{
DECL|field|camelCatalog
specifier|private
name|CamelCatalog
name|camelCatalog
decl_stmt|;
DECL|method|ComponentCatalogNexusRepository ()
specifier|public
name|ComponentCatalogNexusRepository
parameter_list|()
block|{
name|super
argument_list|(
literal|"component"
argument_list|)
expr_stmt|;
block|}
DECL|method|getCamelCatalog ()
specifier|public
name|CamelCatalog
name|getCamelCatalog
parameter_list|()
block|{
return|return
name|camelCatalog
return|;
block|}
comment|/**      * Sets the {@link CamelCatalog} to be used.      */
DECL|method|setCamelCatalog (CamelCatalog camelCatalog)
specifier|public
name|void
name|setCamelCatalog
parameter_list|(
name|CamelCatalog
name|camelCatalog
parameter_list|)
block|{
name|this
operator|.
name|camelCatalog
operator|=
name|camelCatalog
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|camelCatalog
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelCatalog must be configured"
argument_list|)
throw|;
block|}
name|super
operator|.
name|start
argument_list|()
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
name|logger
operator|.
name|debug
argument_list|(
literal|"Processing new artifact: {}:{}:{}"
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
name|String
name|url
init|=
name|createArtifactURL
argument_list|(
name|dto
argument_list|)
decl_stmt|;
name|URL
name|jarUrl
init|=
operator|new
name|URL
argument_list|(
name|url
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
name|Throwable
name|e
parameter_list|)
block|{
name|logger
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
comment|/**      * Adds the component to the {@link CamelCatalog}      *      * @param dto           the artifact      * @param camelCatalog  the Camel Catalog      * @param scheme        component name      * @param javaType      component java class      * @param json          component json schema      */
DECL|method|addComponent (NexusArtifactDto dto, CamelCatalog camelCatalog, String scheme, String javaType, String json)
specifier|protected
name|void
name|addComponent
parameter_list|(
name|NexusArtifactDto
name|dto
parameter_list|,
name|CamelCatalog
name|camelCatalog
parameter_list|,
name|String
name|scheme
parameter_list|,
name|String
name|javaType
parameter_list|,
name|String
name|json
parameter_list|)
block|{
name|camelCatalog
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
name|logger
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
block|}
comment|/**      * Adds any discovered third party Camel components from the artifact.      */
DECL|method|addCustomCamelComponentsFromArtifact (NexusArtifactDto dto, URL jarUrl)
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
init|)
block|{
comment|// is there any custom Camel components in this library?
name|Properties
name|properties
init|=
name|loadComponentProperties
argument_list|(
name|log
argument_list|,
name|classLoader
argument_list|)
decl_stmt|;
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
name|log
argument_list|,
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
name|log
argument_list|,
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
name|addComponent
argument_list|(
name|dto
argument_list|,
name|getCamelCatalog
argument_list|()
argument_list|,
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
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
block|}
end_class

end_unit

