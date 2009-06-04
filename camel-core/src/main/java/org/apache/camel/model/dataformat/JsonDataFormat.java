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
name|ObjectHelper
import|;
end_import

begin_class
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
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|prettyPrint
specifier|private
name|Boolean
name|prettyPrint
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
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
argument_list|(
name|required
operator|=
literal|false
argument_list|)
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
DECL|method|JsonDataFormat ()
specifier|public
name|JsonDataFormat
parameter_list|()
block|{     }
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
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"org.apache.camel.dataformat.xstream.JsonDataFormat"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"org.apache.camel.component.jackson.JacksonDataFormat"
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
name|unmarshalType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"unmarshalType"
argument_list|,
name|unmarshalType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

