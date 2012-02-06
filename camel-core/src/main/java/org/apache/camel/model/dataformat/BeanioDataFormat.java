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

begin_comment
comment|/**  * Represents the BeanIO {@link org.apache.camel.spi.DataFormat}  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"beanio"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BeanioDataFormat
specifier|public
class|class
name|BeanioDataFormat
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
DECL|field|mapping
specifier|private
name|String
name|mapping
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|streamName
specifier|private
name|String
name|streamName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreUnidentifiedRecords
specifier|private
name|Boolean
name|ignoreUnidentifiedRecords
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreUnexpectedRecords
specifier|private
name|Boolean
name|ignoreUnexpectedRecords
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreInvalidRecords
specifier|private
name|Boolean
name|ignoreInvalidRecords
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|method|BeanioDataFormat ()
specifier|public
name|BeanioDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"beanio"
argument_list|)
expr_stmt|;
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
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"mapping"
argument_list|,
name|mapping
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"streamName"
argument_list|,
name|streamName
argument_list|)
expr_stmt|;
if|if
condition|(
name|ignoreUnidentifiedRecords
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"ignoreUnidentifiedRecords"
argument_list|,
name|ignoreUnidentifiedRecords
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreUnexpectedRecords
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"ignoreUnexpectedRecords"
argument_list|,
name|ignoreUnexpectedRecords
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreInvalidRecords
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"ignoreInvalidRecords"
argument_list|,
name|ignoreInvalidRecords
argument_list|)
expr_stmt|;
block|}
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
block|}
DECL|method|getMapping ()
specifier|public
name|String
name|getMapping
parameter_list|()
block|{
return|return
name|mapping
return|;
block|}
DECL|method|setMapping (String mapping)
specifier|public
name|void
name|setMapping
parameter_list|(
name|String
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|mapping
operator|=
name|mapping
expr_stmt|;
block|}
DECL|method|getStreamName ()
specifier|public
name|String
name|getStreamName
parameter_list|()
block|{
return|return
name|streamName
return|;
block|}
DECL|method|setStreamName (String streamName)
specifier|public
name|void
name|setStreamName
parameter_list|(
name|String
name|streamName
parameter_list|)
block|{
name|this
operator|.
name|streamName
operator|=
name|streamName
expr_stmt|;
block|}
DECL|method|getIgnoreUnidentifiedRecords ()
specifier|public
name|Boolean
name|getIgnoreUnidentifiedRecords
parameter_list|()
block|{
return|return
name|ignoreUnidentifiedRecords
return|;
block|}
DECL|method|setIgnoreUnidentifiedRecords (Boolean ignoreUnidentifiedRecords)
specifier|public
name|void
name|setIgnoreUnidentifiedRecords
parameter_list|(
name|Boolean
name|ignoreUnidentifiedRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnidentifiedRecords
operator|=
name|ignoreUnidentifiedRecords
expr_stmt|;
block|}
DECL|method|getIgnoreUnexpectedRecords ()
specifier|public
name|Boolean
name|getIgnoreUnexpectedRecords
parameter_list|()
block|{
return|return
name|ignoreUnexpectedRecords
return|;
block|}
DECL|method|setIgnoreUnexpectedRecords (Boolean ignoreUnexpectedRecords)
specifier|public
name|void
name|setIgnoreUnexpectedRecords
parameter_list|(
name|Boolean
name|ignoreUnexpectedRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnexpectedRecords
operator|=
name|ignoreUnexpectedRecords
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidRecords ()
specifier|public
name|Boolean
name|getIgnoreInvalidRecords
parameter_list|()
block|{
return|return
name|ignoreInvalidRecords
return|;
block|}
DECL|method|setIgnoreInvalidRecords (Boolean ignoreInvalidRecords)
specifier|public
name|void
name|setIgnoreInvalidRecords
parameter_list|(
name|Boolean
name|ignoreInvalidRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidRecords
operator|=
name|ignoreInvalidRecords
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
block|}
end_class

end_unit

