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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents the JiBX XML {@link org.apache.camel.spi.DataFormat}  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"jibx"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
DECL|class|JibxDataFormat
specifier|public
class|class
name|JibxDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"unmarshallClass"
argument_list|)
DECL|field|unmarshallTypeName
specifier|private
name|String
name|unmarshallTypeName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|unmarshallClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
decl_stmt|;
DECL|method|JibxDataFormat ()
specifier|public
name|JibxDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"jibx"
argument_list|)
expr_stmt|;
block|}
DECL|method|JibxDataFormat (Class<?> unmarshallClass)
specifier|public
name|JibxDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setUnmarshallClass
argument_list|(
name|unmarshallClass
argument_list|)
expr_stmt|;
block|}
DECL|method|getUnmarshallClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getUnmarshallClass
parameter_list|()
block|{
return|return
name|unmarshallClass
return|;
block|}
DECL|method|setUnmarshallClass (Class<?> unmarshallClass)
specifier|public
name|void
name|setUnmarshallClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
parameter_list|)
block|{
name|this
operator|.
name|unmarshallClass
operator|=
name|unmarshallClass
expr_stmt|;
block|}
DECL|method|getUnmarshallTypeName ()
specifier|public
name|String
name|getUnmarshallTypeName
parameter_list|()
block|{
return|return
name|unmarshallTypeName
return|;
block|}
DECL|method|setUnmarshallTypeName (String unmarshallTypeName)
specifier|public
name|void
name|setUnmarshallTypeName
parameter_list|(
name|String
name|unmarshallTypeName
parameter_list|)
block|{
name|this
operator|.
name|unmarshallTypeName
operator|=
name|unmarshallTypeName
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
name|unmarshallClass
operator|==
literal|null
operator|&&
name|unmarshallTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|unmarshallClass
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
name|unmarshallTypeName
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
name|unmarshallClass
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"unmarshallClass"
argument_list|,
name|unmarshallClass
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

