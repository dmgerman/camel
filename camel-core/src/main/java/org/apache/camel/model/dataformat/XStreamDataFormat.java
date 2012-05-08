begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

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
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlAdapter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|model
operator|.
name|DataFormatDefinition
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
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|RouteContext
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
name|CamelContextHelper
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

begin_comment
comment|/**  * Represents the XStream XML {@link org.apache.camel.spi.DataFormat}  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"xstream"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
DECL|class|XStreamDataFormat
specifier|public
class|class
name|XStreamDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|driver
specifier|private
name|String
name|driver
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|driverRef
specifier|private
name|String
name|driverRef
decl_stmt|;
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|ConvertersAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"converters"
argument_list|)
DECL|field|converters
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|converters
decl_stmt|;
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|AliasAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"aliases"
argument_list|)
DECL|field|aliases
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
decl_stmt|;
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|OmitFieldsAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"omitFields"
argument_list|)
DECL|field|omitFields
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|omitFields
decl_stmt|;
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|ImplicitCollectionsAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"implicitCollections"
argument_list|)
DECL|field|implicitCollections
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|implicitCollections
decl_stmt|;
DECL|method|XStreamDataFormat ()
specifier|public
name|XStreamDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"xstream"
argument_list|)
expr_stmt|;
block|}
DECL|method|XStreamDataFormat (String encoding)
specifier|public
name|XStreamDataFormat
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getDriver ()
specifier|public
name|String
name|getDriver
parameter_list|()
block|{
return|return
name|driver
return|;
block|}
DECL|method|setDriver (String driver)
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
name|this
operator|.
name|driver
operator|=
name|driver
expr_stmt|;
block|}
DECL|method|getDriverRef ()
specifier|public
name|String
name|getDriverRef
parameter_list|()
block|{
return|return
name|driverRef
return|;
block|}
DECL|method|setDriverRef (String driverRef)
specifier|public
name|void
name|setDriverRef
parameter_list|(
name|String
name|driverRef
parameter_list|)
block|{
name|this
operator|.
name|driverRef
operator|=
name|driverRef
expr_stmt|;
block|}
DECL|method|getConverters ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConverters
parameter_list|()
block|{
return|return
name|converters
return|;
block|}
DECL|method|setConverters (List<String> converters)
specifier|public
name|void
name|setConverters
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|converters
parameter_list|)
block|{
name|this
operator|.
name|converters
operator|=
name|converters
expr_stmt|;
block|}
DECL|method|getAliases ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAliases
parameter_list|()
block|{
return|return
name|aliases
return|;
block|}
DECL|method|setAliases (Map<String, String> aliases)
specifier|public
name|void
name|setAliases
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
parameter_list|)
block|{
name|this
operator|.
name|aliases
operator|=
name|aliases
expr_stmt|;
block|}
DECL|method|getOmitFields ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getOmitFields
parameter_list|()
block|{
return|return
name|omitFields
return|;
block|}
DECL|method|setOmitFields (Map<String, String[]> omitFields)
specifier|public
name|void
name|setOmitFields
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|omitFields
parameter_list|)
block|{
name|this
operator|.
name|omitFields
operator|=
name|omitFields
expr_stmt|;
block|}
DECL|method|getImplicitCollections ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getImplicitCollections
parameter_list|()
block|{
return|return
name|implicitCollections
return|;
block|}
DECL|method|setImplicitCollections (Map<String, String[]> implicitCollections)
specifier|public
name|void
name|setImplicitCollections
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|implicitCollections
parameter_list|)
block|{
name|this
operator|.
name|implicitCollections
operator|=
name|implicitCollections
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createDataFormat (RouteContext routeContext)
specifier|protected
name|DataFormat
name|createDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
literal|"json"
operator|.
name|equals
argument_list|(
name|this
operator|.
name|driver
argument_list|)
condition|)
block|{
name|setProperty
argument_list|(
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-xstream"
argument_list|)
expr_stmt|;
block|}
name|DataFormat
name|answer
init|=
name|super
operator|.
name|createDataFormat
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
comment|// need to lookup the reference for the xstreamDriver
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|driverRef
argument_list|)
condition|)
block|{
name|setProperty
argument_list|(
name|answer
argument_list|,
literal|"xstreamDriver"
argument_list|,
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|driverRef
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|converters
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"converters"
argument_list|,
name|this
operator|.
name|converters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|aliases
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"aliases"
argument_list|,
name|this
operator|.
name|aliases
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|omitFields
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"omitFields"
argument_list|,
name|this
operator|.
name|omitFields
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|implicitCollections
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"implicitCollections"
argument_list|,
name|this
operator|.
name|implicitCollections
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|XmlTransient
DECL|class|ConvertersAdapter
specifier|public
specifier|static
class|class
name|ConvertersAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|ConverterList
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|marshal (List<String> v)
specifier|public
name|ConverterList
name|marshal
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|v
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|ConverterEntry
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|ConverterEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|str
range|:
name|v
control|)
block|{
name|ConverterEntry
name|entry
init|=
operator|new
name|ConverterEntry
argument_list|()
decl_stmt|;
name|entry
operator|.
name|setClsName
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|ConverterList
name|converterList
init|=
operator|new
name|ConverterList
argument_list|()
decl_stmt|;
name|converterList
operator|.
name|setList
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|converterList
return|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (ConverterList v)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|unmarshal
parameter_list|(
name|ConverterList
name|v
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ConverterEntry
name|entry
range|:
name|v
operator|.
name|getList
argument_list|()
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getClsName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"converterList"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|ConverterList
specifier|public
specifier|static
class|class
name|ConverterList
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"converter"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|field|list
specifier|private
name|List
argument_list|<
name|ConverterEntry
argument_list|>
name|list
decl_stmt|;
DECL|method|getList ()
specifier|public
name|List
argument_list|<
name|ConverterEntry
argument_list|>
name|getList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
DECL|method|setList (List<ConverterEntry> list)
specifier|public
name|void
name|setList
parameter_list|(
name|List
argument_list|<
name|ConverterEntry
argument_list|>
name|list
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"converterEntry"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|ConverterEntry
specifier|public
specifier|static
class|class
name|ConverterEntry
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"class"
argument_list|)
DECL|field|clsName
specifier|private
name|String
name|clsName
decl_stmt|;
DECL|method|getClsName ()
specifier|public
name|String
name|getClsName
parameter_list|()
block|{
return|return
name|clsName
return|;
block|}
DECL|method|setClsName (String clsName)
specifier|public
name|void
name|setClsName
parameter_list|(
name|String
name|clsName
parameter_list|)
block|{
name|this
operator|.
name|clsName
operator|=
name|clsName
expr_stmt|;
block|}
block|}
annotation|@
name|XmlTransient
DECL|class|ImplicitCollectionsAdapter
specifier|public
specifier|static
class|class
name|ImplicitCollectionsAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|ImplicitCollectionList
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|marshal (Map<String, String[]> v)
specifier|public
name|ImplicitCollectionList
name|marshal
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|v
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|ImplicitCollectionEntry
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|ImplicitCollectionEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|e
range|:
name|v
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ImplicitCollectionEntry
name|entry
init|=
operator|new
name|ImplicitCollectionEntry
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|ImplicitCollectionList
name|collectionList
init|=
operator|new
name|ImplicitCollectionList
argument_list|()
decl_stmt|;
name|collectionList
operator|.
name|setList
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|collectionList
return|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (ImplicitCollectionList v)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|unmarshal
parameter_list|(
name|ImplicitCollectionList
name|v
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ImplicitCollectionEntry
name|entry
range|:
name|v
operator|.
name|getList
argument_list|()
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getClsName
argument_list|()
argument_list|,
name|entry
operator|.
name|getFields
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"implicitCollectionList"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|ImplicitCollectionList
specifier|public
specifier|static
class|class
name|ImplicitCollectionList
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"class"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|field|list
specifier|private
name|List
argument_list|<
name|ImplicitCollectionEntry
argument_list|>
name|list
decl_stmt|;
DECL|method|getList ()
specifier|public
name|List
argument_list|<
name|ImplicitCollectionEntry
argument_list|>
name|getList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
DECL|method|setList (List<ImplicitCollectionEntry> list)
specifier|public
name|void
name|setList
parameter_list|(
name|List
argument_list|<
name|ImplicitCollectionEntry
argument_list|>
name|list
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"implicitCollectionEntry"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|ImplicitCollectionEntry
specifier|public
specifier|static
class|class
name|ImplicitCollectionEntry
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"name"
argument_list|)
DECL|field|clsName
specifier|private
name|String
name|clsName
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"field"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|field|fields
specifier|private
name|String
index|[]
name|fields
decl_stmt|;
DECL|method|ImplicitCollectionEntry ()
specifier|public
name|ImplicitCollectionEntry
parameter_list|()
block|{         }
DECL|method|ImplicitCollectionEntry (String clsName, String[] fields)
specifier|public
name|ImplicitCollectionEntry
parameter_list|(
name|String
name|clsName
parameter_list|,
name|String
index|[]
name|fields
parameter_list|)
block|{
name|this
operator|.
name|clsName
operator|=
name|clsName
expr_stmt|;
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
DECL|method|getClsName ()
specifier|public
name|String
name|getClsName
parameter_list|()
block|{
return|return
name|clsName
return|;
block|}
DECL|method|setClsName (String clsName)
specifier|public
name|void
name|setClsName
parameter_list|(
name|String
name|clsName
parameter_list|)
block|{
name|this
operator|.
name|clsName
operator|=
name|clsName
expr_stmt|;
block|}
DECL|method|getFields ()
specifier|public
name|String
index|[]
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
DECL|method|setFields (String[] fields)
specifier|public
name|void
name|setFields
parameter_list|(
name|String
index|[]
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Alias[ImplicitCollection="
operator|+
name|clsName
operator|+
literal|", fields="
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|this
operator|.
name|fields
argument_list|)
operator|+
literal|"]"
return|;
block|}
block|}
annotation|@
name|XmlTransient
DECL|class|AliasAdapter
specifier|public
specifier|static
class|class
name|AliasAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|AliasList
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|marshal (Map<String, String> value)
specifier|public
name|AliasList
name|marshal
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|value
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|AliasEntry
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<
name|AliasEntry
argument_list|>
argument_list|(
name|value
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|value
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ret
operator|.
name|add
argument_list|(
operator|new
name|AliasEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|AliasList
name|jaxbMap
init|=
operator|new
name|AliasList
argument_list|()
decl_stmt|;
name|jaxbMap
operator|.
name|setList
argument_list|(
name|ret
argument_list|)
expr_stmt|;
return|return
name|jaxbMap
return|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (AliasList value)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|unmarshal
parameter_list|(
name|AliasList
name|value
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|getList
argument_list|()
operator|==
literal|null
operator|||
name|value
operator|.
name|getList
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AliasEntry
name|alias
range|:
name|value
operator|.
name|getList
argument_list|()
control|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|alias
operator|.
name|getName
argument_list|()
argument_list|,
name|alias
operator|.
name|getClsName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"aliasList"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|AliasList
specifier|public
specifier|static
class|class
name|AliasList
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"alias"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|field|list
specifier|private
name|List
argument_list|<
name|AliasEntry
argument_list|>
name|list
decl_stmt|;
DECL|method|getList ()
specifier|public
name|List
argument_list|<
name|AliasEntry
argument_list|>
name|getList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
DECL|method|setList (List<AliasEntry> list)
specifier|public
name|void
name|setList
parameter_list|(
name|List
argument_list|<
name|AliasEntry
argument_list|>
name|list
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"aliasEntry"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|AliasEntry
specifier|public
specifier|static
class|class
name|AliasEntry
block|{
annotation|@
name|XmlAttribute
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"class"
argument_list|)
DECL|field|clsName
specifier|private
name|String
name|clsName
decl_stmt|;
DECL|method|AliasEntry ()
specifier|public
name|AliasEntry
parameter_list|()
block|{         }
DECL|method|AliasEntry (String key, String clsName)
specifier|public
name|AliasEntry
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|clsName
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|clsName
operator|=
name|clsName
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getClsName ()
specifier|public
name|String
name|getClsName
parameter_list|()
block|{
return|return
name|clsName
return|;
block|}
DECL|method|setClsName (String clsName)
specifier|public
name|void
name|setClsName
parameter_list|(
name|String
name|clsName
parameter_list|)
block|{
name|this
operator|.
name|clsName
operator|=
name|clsName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Alias[name="
operator|+
name|name
operator|+
literal|", class="
operator|+
name|clsName
operator|+
literal|"]"
return|;
block|}
block|}
annotation|@
name|XmlTransient
DECL|class|OmitFieldsAdapter
specifier|public
specifier|static
class|class
name|OmitFieldsAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|OmitFieldList
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|marshal (Map<String, String[]> v)
specifier|public
name|OmitFieldList
name|marshal
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|v
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|OmitFieldEntry
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|OmitFieldEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|e
range|:
name|v
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|OmitFieldEntry
name|entry
init|=
operator|new
name|OmitFieldEntry
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|OmitFieldList
name|collectionList
init|=
operator|new
name|OmitFieldList
argument_list|()
decl_stmt|;
name|collectionList
operator|.
name|setList
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|collectionList
return|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (OmitFieldList v)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|unmarshal
parameter_list|(
name|OmitFieldList
name|v
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|getList
argument_list|()
operator|==
literal|null
operator|||
name|v
operator|.
name|getList
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|OmitFieldEntry
name|entry
range|:
name|v
operator|.
name|getList
argument_list|()
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getClsName
argument_list|()
argument_list|,
name|entry
operator|.
name|getFields
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"omitFieldList"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|OmitFieldList
specifier|public
specifier|static
class|class
name|OmitFieldList
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"omitField"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|field|list
specifier|private
name|List
argument_list|<
name|OmitFieldEntry
argument_list|>
name|list
decl_stmt|;
DECL|method|getList ()
specifier|public
name|List
argument_list|<
name|OmitFieldEntry
argument_list|>
name|getList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
DECL|method|setList (List<OmitFieldEntry> list)
specifier|public
name|void
name|setList
parameter_list|(
name|List
argument_list|<
name|OmitFieldEntry
argument_list|>
name|list
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"omitFieldEntry"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|class|OmitFieldEntry
specifier|public
specifier|static
class|class
name|OmitFieldEntry
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"class"
argument_list|)
DECL|field|clsName
specifier|private
name|String
name|clsName
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"field"
argument_list|,
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|)
DECL|field|fields
specifier|private
name|String
index|[]
name|fields
decl_stmt|;
DECL|method|OmitFieldEntry ()
specifier|public
name|OmitFieldEntry
parameter_list|()
block|{         }
DECL|method|OmitFieldEntry (String clsName, String[] fields)
specifier|public
name|OmitFieldEntry
parameter_list|(
name|String
name|clsName
parameter_list|,
name|String
index|[]
name|fields
parameter_list|)
block|{
name|this
operator|.
name|clsName
operator|=
name|clsName
expr_stmt|;
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
DECL|method|getClsName ()
specifier|public
name|String
name|getClsName
parameter_list|()
block|{
return|return
name|clsName
return|;
block|}
DECL|method|setClsName (String clsName)
specifier|public
name|void
name|setClsName
parameter_list|(
name|String
name|clsName
parameter_list|)
block|{
name|this
operator|.
name|clsName
operator|=
name|clsName
expr_stmt|;
block|}
DECL|method|getFields ()
specifier|public
name|String
index|[]
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
DECL|method|setFields (String[] fields)
specifier|public
name|void
name|setFields
parameter_list|(
name|String
index|[]
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"OmitField["
operator|+
name|clsName
operator|+
literal|", fields="
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|this
operator|.
name|fields
argument_list|)
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

