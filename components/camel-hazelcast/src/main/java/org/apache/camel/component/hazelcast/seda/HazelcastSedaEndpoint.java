begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|seda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastCommand
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastDefaultComponent
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastDefaultEndpoint
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The hazelcast-seda component is used to access<a href="http://www.hazelcast.com/">Hazelcast</a> {@link BlockingQueue}.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.7.0"
argument_list|,
name|scheme
operator|=
literal|"hazelcast-seda"
argument_list|,
name|title
operator|=
literal|"Hazelcast SEDA"
argument_list|,
name|syntax
operator|=
literal|"hazelcast-seda:cacheName"
argument_list|,
name|label
operator|=
literal|"cache,datagrid"
argument_list|)
DECL|class|HazelcastSedaEndpoint
specifier|public
class|class
name|HazelcastSedaEndpoint
extends|extends
name|HazelcastDefaultEndpoint
block|{
DECL|field|queue
specifier|private
specifier|final
name|BlockingQueue
argument_list|<
name|Object
argument_list|>
name|queue
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|HazelcastSedaConfiguration
name|configuration
decl_stmt|;
DECL|method|HazelcastSedaEndpoint (final HazelcastInstance hazelcastInstance, final String uri, final HazelcastDefaultComponent component, final HazelcastSedaConfiguration configuration)
specifier|public
name|HazelcastSedaEndpoint
parameter_list|(
specifier|final
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|HazelcastDefaultComponent
name|component
parameter_list|,
specifier|final
name|HazelcastSedaConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|hazelcastInstance
argument_list|,
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|hazelcastInstance
operator|.
name|getQueue
argument_list|(
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Queue name is missing."
argument_list|)
throw|;
block|}
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|seda
argument_list|)
expr_stmt|;
block|}
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
name|HazelcastSedaProducer
argument_list|(
name|this
argument_list|,
name|getQueue
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createConsumer (final Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|HazelcastSedaConsumer
name|answer
init|=
operator|new
name|HazelcastSedaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getQueue ()
specifier|public
name|BlockingQueue
argument_list|<
name|Object
argument_list|>
name|getQueue
parameter_list|()
block|{
return|return
name|queue
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|HazelcastSedaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

