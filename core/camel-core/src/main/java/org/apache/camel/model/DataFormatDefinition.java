begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|XmlAnyAttribute
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
name|namespace
operator|.
name|QName
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
comment|/**  * Represents a Camel data format  */
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
name|XmlType
argument_list|(
name|name
operator|=
literal|"dataFormat"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DataFormatDefinition
specifier|public
class|class
name|DataFormatDefinition
extends|extends
name|IdentifiedType
implements|implements
name|OtherAttributesAware
implements|,
name|DefinitionPropertyPlaceholderConfigurer
block|{
annotation|@
name|XmlTransient
DECL|field|dataFormat
specifier|private
name|DataFormat
name|dataFormat
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|dataFormatName
specifier|private
name|String
name|dataFormatName
decl_stmt|;
comment|// use xs:any to support optional property placeholders
annotation|@
name|XmlAnyAttribute
DECL|field|otherAttributes
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|otherAttributes
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
decl_stmt|;
DECL|method|DataFormatDefinition ()
specifier|public
name|DataFormatDefinition
parameter_list|()
block|{     }
DECL|method|DataFormatDefinition (DataFormat dataFormat)
specifier|public
name|DataFormatDefinition
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
expr_stmt|;
block|}
DECL|method|DataFormatDefinition (String dataFormatName)
specifier|protected
name|DataFormatDefinition
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
block|{
name|this
operator|.
name|dataFormatName
operator|=
name|dataFormatName
expr_stmt|;
block|}
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
name|dataFormatName
return|;
block|}
DECL|method|setDataFormatName (String dataFormatName)
specifier|public
name|void
name|setDataFormatName
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
block|{
name|this
operator|.
name|dataFormatName
operator|=
name|dataFormatName
expr_stmt|;
block|}
DECL|method|getDataFormat ()
specifier|public
name|DataFormat
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
DECL|method|setDataFormat (DataFormat dataFormat)
specifier|public
name|void
name|setDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOtherAttributes ()
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|getOtherAttributes
parameter_list|()
block|{
return|return
name|otherAttributes
return|;
block|}
comment|/**      * Adds an optional attribute      */
annotation|@
name|Override
DECL|method|setOtherAttributes (Map<QName, Object> otherAttributes)
specifier|public
name|void
name|setOtherAttributes
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|otherAttributes
parameter_list|)
block|{
name|this
operator|.
name|otherAttributes
operator|=
name|otherAttributes
expr_stmt|;
block|}
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
comment|/**      * Whether the data format should set the<tt>Content-Type</tt> header with the type from the data format if the      * data format is capable of doing so.      *<p/>      * For example<tt>application/xml</tt> for data formats marshalling to XML, or<tt>application/json</tt>      * for data formats marshalling to JSon etc.      */
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
name|String
name|name
init|=
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"DataFormat"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|indexOf
argument_list|(
literal|"DataFormat"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

