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
comment|/**  * UniVocity fixed-width data format  */
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
literal|"univocity-fixed"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|UniVocityFixedWidthDataFormat
specifier|public
class|class
name|UniVocityFixedWidthDataFormat
extends|extends
name|UniVocityAbstractDataFormat
block|{
annotation|@
name|XmlAttribute
DECL|field|skipTrailingCharsUntilNewline
specifier|protected
name|Boolean
name|skipTrailingCharsUntilNewline
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|recordEndsOnNewline
specifier|protected
name|Boolean
name|recordEndsOnNewline
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|padding
specifier|protected
name|String
name|padding
decl_stmt|;
DECL|method|UniVocityFixedWidthDataFormat ()
specifier|public
name|UniVocityFixedWidthDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"univocity-fixed"
argument_list|)
expr_stmt|;
block|}
DECL|method|getSkipTrailingCharsUntilNewline ()
specifier|public
name|Boolean
name|getSkipTrailingCharsUntilNewline
parameter_list|()
block|{
return|return
name|skipTrailingCharsUntilNewline
return|;
block|}
DECL|method|setSkipTrailingCharsUntilNewline (Boolean skipTrailingCharsUntilNewline)
specifier|public
name|void
name|setSkipTrailingCharsUntilNewline
parameter_list|(
name|Boolean
name|skipTrailingCharsUntilNewline
parameter_list|)
block|{
name|this
operator|.
name|skipTrailingCharsUntilNewline
operator|=
name|skipTrailingCharsUntilNewline
expr_stmt|;
block|}
DECL|method|getRecordEndsOnNewline ()
specifier|public
name|Boolean
name|getRecordEndsOnNewline
parameter_list|()
block|{
return|return
name|recordEndsOnNewline
return|;
block|}
DECL|method|setRecordEndsOnNewline (Boolean recordEndsOnNewline)
specifier|public
name|void
name|setRecordEndsOnNewline
parameter_list|(
name|Boolean
name|recordEndsOnNewline
parameter_list|)
block|{
name|this
operator|.
name|recordEndsOnNewline
operator|=
name|recordEndsOnNewline
expr_stmt|;
block|}
DECL|method|getPadding ()
specifier|public
name|String
name|getPadding
parameter_list|()
block|{
return|return
name|padding
return|;
block|}
DECL|method|setPadding (String padding)
specifier|public
name|void
name|setPadding
parameter_list|(
name|String
name|padding
parameter_list|)
block|{
name|this
operator|.
name|padding
operator|=
name|padding
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
name|super
operator|.
name|configureDataFormat
argument_list|(
name|dataFormat
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|int
index|[]
name|lengths
init|=
operator|new
name|int
index|[
name|headers
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lengths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|length
init|=
name|headers
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|length
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The length of all headers must be defined."
argument_list|)
throw|;
block|}
name|lengths
index|[
name|i
index|]
operator|=
name|length
expr_stmt|;
block|}
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"fieldLengths"
argument_list|,
name|lengths
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipTrailingCharsUntilNewline
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
literal|"skipTrailingCharsUntilNewline"
argument_list|,
name|skipTrailingCharsUntilNewline
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|recordEndsOnNewline
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
literal|"recordEndsOnNewline"
argument_list|,
name|recordEndsOnNewline
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|padding
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
literal|"padding"
argument_list|,
name|singleCharOf
argument_list|(
literal|"padding"
argument_list|,
name|padding
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

