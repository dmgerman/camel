begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
package|;
end_package

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|EtcdClient
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

begin_class
DECL|class|EtcdStatsEndpoint
specifier|public
class|class
name|EtcdStatsEndpoint
extends|extends
name|AbstractEtcdPollingEndpoint
block|{
DECL|method|EtcdStatsEndpoint ( String uri, EtcdComponent component, EtcdConfiguration configuration, EtcdNamespace namespace, String path)
specifier|public
name|EtcdStatsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|EtcdComponent
name|component
parameter_list|,
name|EtcdConfiguration
name|configuration
parameter_list|,
name|EtcdNamespace
name|namespace
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|,
name|namespace
argument_list|,
name|path
argument_list|)
expr_stmt|;
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
return|return
operator|new
name|EtcdStatsProducer
argument_list|(
name|this
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|getNamespace
argument_list|()
argument_list|,
name|getPath
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|EtcdStatsConsumer
name|consumer
init|=
operator|new
name|EtcdStatsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|getStats (EtcdClient client)
name|Object
name|getStats
parameter_list|(
name|EtcdClient
name|client
parameter_list|)
block|{
switch|switch
condition|(
name|getPath
argument_list|()
condition|)
block|{
case|case
name|EtcdConstants
operator|.
name|ETCD_LEADER_STATS_PATH
case|:
return|return
name|client
operator|.
name|getLeaderStats
argument_list|()
return|;
case|case
name|EtcdConstants
operator|.
name|ETCD_SELF_STATS_PATH
case|:
return|return
name|client
operator|.
name|getSelfStats
argument_list|()
return|;
case|case
name|EtcdConstants
operator|.
name|ETCD_STORE_STATS_PATH
case|:
return|return
name|client
operator|.
name|getStoreStats
argument_list|()
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No stats for "
operator|+
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

