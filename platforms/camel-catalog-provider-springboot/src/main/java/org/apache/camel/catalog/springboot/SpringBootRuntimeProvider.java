begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|springboot
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
name|CatalogHelper
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
name|RuntimeProvider
import|;
end_import

begin_comment
comment|/**  * A Spring Boot based {@link RuntimeProvider} which only includes the supported Camel components, data formats, and languages  * which can be installed in Spring Boot using the starter dependencies.  */
end_comment

begin_class
DECL|class|SpringBootRuntimeProvider
specifier|public
class|class
name|SpringBootRuntimeProvider
implements|implements
name|RuntimeProvider
block|{
DECL|field|COMPONENT_DIR
specifier|private
specifier|static
specifier|final
name|String
name|COMPONENT_DIR
init|=
literal|"org/apache/camel/catalog/springboot/components"
decl_stmt|;
DECL|field|DATAFORMAT_DIR
specifier|private
specifier|static
specifier|final
name|String
name|DATAFORMAT_DIR
init|=
literal|"org/apache/camel/catalog/springboot/dataformats"
decl_stmt|;
DECL|field|LANGUAGE_DIR
specifier|private
specifier|static
specifier|final
name|String
name|LANGUAGE_DIR
init|=
literal|"org/apache/camel/catalog/springboot/languages"
decl_stmt|;
DECL|field|OTHER_DIR
specifier|private
specifier|static
specifier|final
name|String
name|OTHER_DIR
init|=
literal|"org/apache/camel/catalog/springboot/others"
decl_stmt|;
DECL|field|COMPONENTS_CATALOG
specifier|private
specifier|static
specifier|final
name|String
name|COMPONENTS_CATALOG
init|=
literal|"org/apache/camel/catalog/springboot/components.properties"
decl_stmt|;
DECL|field|DATA_FORMATS_CATALOG
specifier|private
specifier|static
specifier|final
name|String
name|DATA_FORMATS_CATALOG
init|=
literal|"org/apache/camel/catalog/springboot/dataformats.properties"
decl_stmt|;
DECL|field|LANGUAGE_CATALOG
specifier|private
specifier|static
specifier|final
name|String
name|LANGUAGE_CATALOG
init|=
literal|"org/apache/camel/catalog/springboot/languages.properties"
decl_stmt|;
DECL|field|OTHER_CATALOG
specifier|private
specifier|static
specifier|final
name|String
name|OTHER_CATALOG
init|=
literal|"org/apache/camel/catalog/springboot/others.properties"
decl_stmt|;
DECL|field|camelCatalog
specifier|private
name|CamelCatalog
name|camelCatalog
decl_stmt|;
annotation|@
name|Override
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
annotation|@
name|Override
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
DECL|method|getProviderName ()
specifier|public
name|String
name|getProviderName
parameter_list|()
block|{
return|return
literal|"springboot"
return|;
block|}
annotation|@
name|Override
DECL|method|getProviderGroupId ()
specifier|public
name|String
name|getProviderGroupId
parameter_list|()
block|{
return|return
literal|"org.apache.camel"
return|;
block|}
annotation|@
name|Override
DECL|method|getProviderArtifactId ()
specifier|public
name|String
name|getProviderArtifactId
parameter_list|()
block|{
return|return
literal|"camel-catalog-provider-springboot"
return|;
block|}
annotation|@
name|Override
DECL|method|getComponentJSonSchemaDirectory ()
specifier|public
name|String
name|getComponentJSonSchemaDirectory
parameter_list|()
block|{
return|return
name|COMPONENT_DIR
return|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatJSonSchemaDirectory ()
specifier|public
name|String
name|getDataFormatJSonSchemaDirectory
parameter_list|()
block|{
return|return
name|DATAFORMAT_DIR
return|;
block|}
annotation|@
name|Override
DECL|method|getLanguageJSonSchemaDirectory ()
specifier|public
name|String
name|getLanguageJSonSchemaDirectory
parameter_list|()
block|{
return|return
name|LANGUAGE_DIR
return|;
block|}
annotation|@
name|Override
DECL|method|getOtherJSonSchemaDirectory ()
specifier|public
name|String
name|getOtherJSonSchemaDirectory
parameter_list|()
block|{
return|return
name|OTHER_DIR
return|;
block|}
annotation|@
name|Override
DECL|method|findComponentNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|camelCatalog
operator|.
name|getVersionManager
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|COMPONENTS_CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|CatalogHelper
operator|.
name|loadLines
argument_list|(
name|is
argument_list|,
name|names
argument_list|)
expr_stmt|;
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
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|findDataFormatNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|camelCatalog
operator|.
name|getVersionManager
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|DATA_FORMATS_CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|CatalogHelper
operator|.
name|loadLines
argument_list|(
name|is
argument_list|,
name|names
argument_list|)
expr_stmt|;
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
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|findLanguageNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|camelCatalog
operator|.
name|getVersionManager
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|LANGUAGE_CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|CatalogHelper
operator|.
name|loadLines
argument_list|(
name|is
argument_list|,
name|names
argument_list|)
expr_stmt|;
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
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|findOtherNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findOtherNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|camelCatalog
operator|.
name|getVersionManager
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|OTHER_CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|CatalogHelper
operator|.
name|loadLines
argument_list|(
name|is
argument_list|,
name|names
argument_list|)
expr_stmt|;
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
return|return
name|names
return|;
block|}
block|}
end_class

end_unit

