begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|AmazonSimpleDB
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DomainMetadataRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DomainMetadataResult
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
name|Exchange
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
name|Message
import|;
end_import

begin_class
DECL|class|DomainMetadataCommand
specifier|public
class|class
name|DomainMetadataCommand
extends|extends
name|AbstractSdbCommand
block|{
DECL|method|DomainMetadataCommand (AmazonSimpleDB sdbClient, SdbConfiguration configuration, Exchange exchange)
specifier|public
name|DomainMetadataCommand
parameter_list|(
name|AmazonSimpleDB
name|sdbClient
parameter_list|,
name|SdbConfiguration
name|configuration
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
name|sdbClient
argument_list|,
name|configuration
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|DomainMetadataRequest
name|request
init|=
operator|new
name|DomainMetadataRequest
argument_list|()
operator|.
name|withDomainName
argument_list|(
name|determineDomainName
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Sending request [{}] for exchange [{}]..."
argument_list|,
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|DomainMetadataResult
name|result
init|=
name|this
operator|.
name|sdbClient
operator|.
name|domainMetadata
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Received result [{}]"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Message
name|msg
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|TIMESTAMP
argument_list|,
name|result
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEM_COUNT
argument_list|,
name|result
operator|.
name|getItemCount
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_NAME_COUNT
argument_list|,
name|result
operator|.
name|getAttributeNameCount
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_VALUE_COUNT
argument_list|,
name|result
operator|.
name|getAttributeValueCount
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_NAME_SIZE
argument_list|,
name|result
operator|.
name|getAttributeNamesSizeBytes
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_VALUE_SIZE
argument_list|,
name|result
operator|.
name|getAttributeValuesSizeBytes
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEM_NAME_SIZE
argument_list|,
name|result
operator|.
name|getItemNamesSizeBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getMessageForResponse (final Exchange exchange)
specifier|public
specifier|static
name|Message
name|getMessageForResponse
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
end_class

end_unit
