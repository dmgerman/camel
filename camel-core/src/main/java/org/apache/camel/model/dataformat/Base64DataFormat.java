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
name|Label
import|;
end_import

begin_comment
comment|/**  * Represents the Base64 {@link org.apache.camel.spi.DataFormat}  *  * @version   */
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
literal|"base64"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|Base64DataFormat
specifier|public
class|class
name|Base64DataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|lineLength
specifier|private
name|Integer
name|lineLength
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lineSeparator
specifier|private
name|String
name|lineSeparator
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|urlSafe
specifier|private
name|Boolean
name|urlSafe
decl_stmt|;
DECL|method|Base64DataFormat ()
specifier|public
name|Base64DataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"base64"
argument_list|)
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
if|if
condition|(
name|getLineLength
argument_list|()
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
literal|"lineLength"
argument_list|,
name|getLineLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getUrlSafe
argument_list|()
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
literal|"urlSafe"
argument_list|,
name|getUrlSafe
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getLineSeparator
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// line separator must be a byte[]
name|byte
index|[]
name|bytes
init|=
name|getLineSeparator
argument_list|()
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"lineSeparator"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getLineLength ()
specifier|public
name|Integer
name|getLineLength
parameter_list|()
block|{
return|return
name|lineLength
return|;
block|}
DECL|method|setLineLength (Integer lineLength)
specifier|public
name|void
name|setLineLength
parameter_list|(
name|Integer
name|lineLength
parameter_list|)
block|{
name|this
operator|.
name|lineLength
operator|=
name|lineLength
expr_stmt|;
block|}
DECL|method|getLineSeparator ()
specifier|public
name|String
name|getLineSeparator
parameter_list|()
block|{
return|return
name|lineSeparator
return|;
block|}
DECL|method|setLineSeparator (String lineSeparator)
specifier|public
name|void
name|setLineSeparator
parameter_list|(
name|String
name|lineSeparator
parameter_list|)
block|{
name|this
operator|.
name|lineSeparator
operator|=
name|lineSeparator
expr_stmt|;
block|}
DECL|method|getUrlSafe ()
specifier|public
name|Boolean
name|getUrlSafe
parameter_list|()
block|{
return|return
name|urlSafe
return|;
block|}
DECL|method|setUrlSafe (Boolean urlSafe)
specifier|public
name|void
name|setUrlSafe
parameter_list|(
name|Boolean
name|urlSafe
parameter_list|)
block|{
name|this
operator|.
name|urlSafe
operator|=
name|urlSafe
expr_stmt|;
block|}
block|}
end_class

end_unit

