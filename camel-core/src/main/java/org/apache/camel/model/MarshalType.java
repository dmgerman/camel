begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|XmlElements
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
name|Processor
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
name|model
operator|.
name|dataformat
operator|.
name|ArtixDSDataFormat
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
name|dataformat
operator|.
name|DataFormatType
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
name|dataformat
operator|.
name|JaxbDataFormat
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
name|dataformat
operator|.
name|SerializationDataFormat
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
name|dataformat
operator|.
name|XMLBeansDataFormat
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
name|processor
operator|.
name|MarshalProcessor
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Marshals to a binary payload using the given {@link DataFormatType}  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"marshal"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|MarshalType
specifier|public
class|class
name|MarshalType
extends|extends
name|OutputType
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
comment|// TODO cannot use @XmlElementRef as it doesn't allow optional properties
comment|// @XmlElementRef
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"artixDS"
argument_list|,
name|type
operator|=
name|ArtixDSDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"jaxb"
argument_list|,
name|type
operator|=
name|JaxbDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"serialization"
argument_list|,
name|type
operator|=
name|SerializationDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"xmlBeans"
argument_list|,
name|type
operator|=
name|XMLBeansDataFormat
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|dataFormatType
specifier|private
name|DataFormatType
name|dataFormatType
decl_stmt|;
DECL|method|MarshalType ()
specifier|public
name|MarshalType
parameter_list|()
block|{     }
DECL|method|MarshalType (DataFormatType dataFormatType)
specifier|public
name|MarshalType
parameter_list|(
name|DataFormatType
name|dataFormatType
parameter_list|)
block|{
name|this
operator|.
name|dataFormatType
operator|=
name|dataFormatType
expr_stmt|;
block|}
DECL|method|MarshalType (String ref)
specifier|public
name|MarshalType
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
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
if|if
condition|(
name|dataFormatType
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Marshal["
operator|+
name|dataFormatType
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"Marshal[ref:  "
operator|+
name|ref
operator|+
literal|"]"
return|;
block|}
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getDataFormatType ()
specifier|public
name|DataFormatType
name|getDataFormatType
parameter_list|()
block|{
return|return
name|dataFormatType
return|;
block|}
DECL|method|setDataFormatType (DataFormatType dataFormatType)
specifier|public
name|void
name|setDataFormatType
parameter_list|(
name|DataFormatType
name|dataFormatType
parameter_list|)
block|{
name|this
operator|.
name|dataFormatType
operator|=
name|dataFormatType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|DataFormat
name|dataFormat
init|=
name|DataFormatType
operator|.
name|getDataFormat
argument_list|(
name|routeContext
argument_list|,
name|getDataFormatType
argument_list|()
argument_list|,
name|ref
argument_list|)
decl_stmt|;
return|return
operator|new
name|MarshalProcessor
argument_list|(
name|dataFormat
argument_list|)
return|;
block|}
block|}
end_class

end_unit

