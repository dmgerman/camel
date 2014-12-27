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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Label
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents the Json {@link DataFormat}  *  * @version   */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"dataformat,transformation"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"json"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|JsonDataFormat
specifier|public
class|class
name|JsonDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|prettyPrint
specifier|private
name|Boolean
name|prettyPrint
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|library
specifier|private
name|JsonLibrary
name|library
init|=
name|JsonLibrary
operator|.
name|XStream
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|unmarshalTypeName
specifier|private
name|String
name|unmarshalTypeName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|unmarshalType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|jsonView
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|include
specifier|private
name|String
name|include
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|allowJmsType
specifier|private
name|Boolean
name|allowJmsType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|collectionTypeName
specifier|private
name|String
name|collectionTypeName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|collectionType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|collectionType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useList
specifier|private
name|Boolean
name|useList
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|enableJaxbAnnotationModule
specifier|private
name|Boolean
name|enableJaxbAnnotationModule
decl_stmt|;
DECL|method|JsonDataFormat ()
specifier|public
name|JsonDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
DECL|method|JsonDataFormat (JsonLibrary library)
specifier|public
name|JsonDataFormat
parameter_list|(
name|JsonLibrary
name|library
parameter_list|)
block|{
name|this
operator|.
name|library
operator|=
name|library
expr_stmt|;
block|}
DECL|method|getPrettyPrint ()
specifier|public
name|Boolean
name|getPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
DECL|method|setPrettyPrint (Boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|Boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
DECL|method|getUnmarshalTypeName ()
specifier|public
name|String
name|getUnmarshalTypeName
parameter_list|()
block|{
return|return
name|unmarshalTypeName
return|;
block|}
DECL|method|setUnmarshalTypeName (String unmarshalTypeName)
specifier|public
name|void
name|setUnmarshalTypeName
parameter_list|(
name|String
name|unmarshalTypeName
parameter_list|)
block|{
name|this
operator|.
name|unmarshalTypeName
operator|=
name|unmarshalTypeName
expr_stmt|;
block|}
DECL|method|getUnmarshalType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getUnmarshalType
parameter_list|()
block|{
return|return
name|unmarshalType
return|;
block|}
DECL|method|setUnmarshalType (Class<?> unmarshalType)
specifier|public
name|void
name|setUnmarshalType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
block|}
DECL|method|getLibrary ()
specifier|public
name|JsonLibrary
name|getLibrary
parameter_list|()
block|{
return|return
name|library
return|;
block|}
DECL|method|setLibrary (JsonLibrary library)
specifier|public
name|void
name|setLibrary
parameter_list|(
name|JsonLibrary
name|library
parameter_list|)
block|{
name|this
operator|.
name|library
operator|=
name|library
expr_stmt|;
block|}
DECL|method|getJsonView ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getJsonView
parameter_list|()
block|{
return|return
name|jsonView
return|;
block|}
DECL|method|setJsonView (Class<?> jsonView)
specifier|public
name|void
name|setJsonView
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|this
operator|.
name|jsonView
operator|=
name|jsonView
expr_stmt|;
block|}
DECL|method|getInclude ()
specifier|public
name|String
name|getInclude
parameter_list|()
block|{
return|return
name|include
return|;
block|}
DECL|method|setInclude (String include)
specifier|public
name|void
name|setInclude
parameter_list|(
name|String
name|include
parameter_list|)
block|{
name|this
operator|.
name|include
operator|=
name|include
expr_stmt|;
block|}
DECL|method|getAllowJmsType ()
specifier|public
name|Boolean
name|getAllowJmsType
parameter_list|()
block|{
return|return
name|allowJmsType
return|;
block|}
DECL|method|setAllowJmsType (Boolean allowJmsType)
specifier|public
name|void
name|setAllowJmsType
parameter_list|(
name|Boolean
name|allowJmsType
parameter_list|)
block|{
name|this
operator|.
name|allowJmsType
operator|=
name|allowJmsType
expr_stmt|;
block|}
DECL|method|getCollectionTypeName ()
specifier|public
name|String
name|getCollectionTypeName
parameter_list|()
block|{
return|return
name|collectionTypeName
return|;
block|}
DECL|method|setCollectionTypeName (String collectionTypeName)
specifier|public
name|void
name|setCollectionTypeName
parameter_list|(
name|String
name|collectionTypeName
parameter_list|)
block|{
name|this
operator|.
name|collectionTypeName
operator|=
name|collectionTypeName
expr_stmt|;
block|}
DECL|method|getUseList ()
specifier|public
name|Boolean
name|getUseList
parameter_list|()
block|{
return|return
name|useList
return|;
block|}
DECL|method|setUseList (Boolean useList)
specifier|public
name|void
name|setUseList
parameter_list|(
name|Boolean
name|useList
parameter_list|)
block|{
name|this
operator|.
name|useList
operator|=
name|useList
expr_stmt|;
block|}
DECL|method|getEnableJaxbAnnotationModule ()
specifier|public
name|Boolean
name|getEnableJaxbAnnotationModule
parameter_list|()
block|{
return|return
name|enableJaxbAnnotationModule
return|;
block|}
DECL|method|setEnableJaxbAnnotationModule (Boolean enableJaxbAnnotationModule)
specifier|public
name|void
name|setEnableJaxbAnnotationModule
parameter_list|(
name|Boolean
name|enableJaxbAnnotationModule
parameter_list|)
block|{
name|this
operator|.
name|enableJaxbAnnotationModule
operator|=
name|enableJaxbAnnotationModule
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
name|library
operator|==
name|JsonLibrary
operator|.
name|XStream
condition|)
block|{
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-xstream"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|library
operator|==
name|JsonLibrary
operator|.
name|Jackson
condition|)
block|{
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-jackson"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-gson"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unmarshalType
operator|==
literal|null
operator|&&
name|unmarshalTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|unmarshalType
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|unmarshalTypeName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|collectionType
operator|==
literal|null
operator|&&
name|collectionTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|collectionType
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|collectionTypeName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|super
operator|.
name|createDataFormat
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|unmarshalType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"unmarshalType"
argument_list|,
name|unmarshalType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prettyPrint
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"prettyPrint"
argument_list|,
name|prettyPrint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jsonView
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"jsonView"
argument_list|,
name|jsonView
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|include
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"include"
argument_list|,
name|include
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allowJmsType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"allowJmsType"
argument_list|,
name|allowJmsType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|collectionType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"collectionType"
argument_list|,
name|collectionType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useList
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"useList"
argument_list|,
name|useList
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enableJaxbAnnotationModule
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"enableJaxbAnnotationModule"
argument_list|,
name|enableJaxbAnnotationModule
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

