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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Boon data format  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dataformat,transformation"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"boon"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BoonDataFormat
specifier|public
class|class
name|BoonDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|unmarshalType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
decl_stmt|;
DECL|method|BoonDataFormat ()
specifier|public
name|BoonDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"boon"
argument_list|)
expr_stmt|;
block|}
DECL|method|BoonDataFormat (Class<?> unmarshalType)
specifier|public
name|BoonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setUnmarshalType
argument_list|(
name|unmarshalType
argument_list|)
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
name|Class
argument_list|<
name|?
argument_list|>
name|answer
init|=
name|getUnmarshalType
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
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
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

