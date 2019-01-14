begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|DelegateEndpoint
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
name|Producer
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Represents an endpoint which only becomes active when it obtains the master lock  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed ZooKeeper Master Endpoint"
argument_list|)
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"zookeeper-master"
argument_list|,
name|syntax
operator|=
literal|"zookeeper-master:groupName:consumerEndpointUri"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|title
operator|=
literal|"ZooKeeper Master"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"clustering"
argument_list|)
DECL|class|MasterEndpoint
specifier|public
class|class
name|MasterEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|DelegateEndpoint
block|{
DECL|field|component
specifier|private
specifier|final
name|MasterComponent
name|component
decl_stmt|;
DECL|field|consumerEndpoint
specifier|private
specifier|final
name|Endpoint
name|consumerEndpoint
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The name of the cluster group to use"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|groupName
specifier|private
specifier|final
name|String
name|groupName
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The consumer endpoint to use in master/slave mode"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|consumerEndpointUri
specifier|private
specifier|final
name|String
name|consumerEndpointUri
decl_stmt|;
DECL|method|MasterEndpoint (String uri, MasterComponent component, String groupName, String consumerEndpointUri)
specifier|public
name|MasterEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MasterComponent
name|component
parameter_list|,
name|String
name|groupName
parameter_list|,
name|String
name|consumerEndpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|groupName
operator|=
name|groupName
expr_stmt|;
name|this
operator|.
name|consumerEndpointUri
operator|=
name|consumerEndpointUri
expr_stmt|;
name|this
operator|.
name|consumerEndpoint
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|consumerEndpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|consumerEndpoint
return|;
block|}
DECL|method|getConsumerEndpoint ()
specifier|public
name|Endpoint
name|getConsumerEndpoint
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The consumer endpoint url to use in master/slave mode"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
DECL|method|getConsumerEndpointUri ()
specifier|public
name|String
name|getConsumerEndpointUri
parameter_list|()
block|{
return|return
name|consumerEndpointUri
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The name of the cluster group to use"
argument_list|)
DECL|method|getGroupName ()
specifier|public
name|String
name|getGroupName
parameter_list|()
block|{
return|return
name|groupName
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot produce from this endpoint"
argument_list|)
throw|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|MasterConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// to allow properties to be propagated to the child endpoint
return|return
literal|true
return|;
block|}
DECL|method|getComponent ()
specifier|public
name|MasterComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
block|}
end_class

end_unit

