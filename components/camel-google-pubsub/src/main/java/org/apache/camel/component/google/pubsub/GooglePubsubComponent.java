begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.pubsub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|pubsub
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link GooglePubsubEndpoint}.  */
end_comment

begin_class
DECL|class|GooglePubsubComponent
specifier|public
class|class
name|GooglePubsubComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|connectionFactory
specifier|private
name|GooglePubsubConnectionFactory
name|connectionFactory
decl_stmt|;
DECL|method|GooglePubsubComponent ()
specifier|public
name|GooglePubsubComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|parts
init|=
name|remaining
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Google PubSub Endpoint format \"projectId:destinationName[:subscriptionName]\""
argument_list|)
throw|;
block|}
name|GooglePubsubEndpoint
name|pubsubEndpoint
init|=
operator|new
name|GooglePubsubEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|pubsubEndpoint
operator|.
name|setProjectId
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|pubsubEndpoint
operator|.
name|setDestinationName
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|pubsubEndpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|pubsubEndpoint
return|;
block|}
comment|/**      * Sets the connection factory to use:      * provides the ability to explicitly manage connection credentials:      * - the path to the key file      * - the Service Account Key / Email pair      */
DECL|method|getConnectionFactory ()
specifier|public
name|GooglePubsubConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
if|if
condition|(
name|connectionFactory
operator|==
literal|null
condition|)
block|{
name|connectionFactory
operator|=
operator|new
name|GooglePubsubConnectionFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (GooglePubsubConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|GooglePubsubConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

