begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.queue
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|queue
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
name|atomix
operator|.
name|client
operator|.
name|AbstractAtomixClientEndpoint
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
name|UriParam
import|;
end_import

begin_comment
comment|/**  * The atomix-queue component is used to access Atomix's<a href="http://atomix.io/atomix/docs/collections/#distributedqueue">distributed queue</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.20.0"
argument_list|,
name|scheme
operator|=
literal|"atomix-queue"
argument_list|,
name|title
operator|=
literal|"Atomix Queue"
argument_list|,
name|syntax
operator|=
literal|"atomix-queue:resourceName"
argument_list|,
name|consumerClass
operator|=
name|AtomixQueueConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"clustering"
argument_list|)
DECL|class|AtomixQueueEndpoint
specifier|final
class|class
name|AtomixQueueEndpoint
extends|extends
name|AbstractAtomixClientEndpoint
argument_list|<
name|AtomixQueueComponent
argument_list|,
name|AtomixQueueConfiguration
argument_list|>
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|AtomixQueueConfiguration
name|configuration
decl_stmt|;
DECL|method|AtomixQueueEndpoint (String uri, AtomixQueueComponent component, String resourceName)
specifier|public
name|AtomixQueueEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|AtomixQueueComponent
name|component
parameter_list|,
name|String
name|resourceName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|resourceName
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
name|AtomixQueueProducer
argument_list|(
name|this
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
return|return
operator|new
name|AtomixQueueConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getResourceName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|AtomixQueueConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|setConfiguration (AtomixQueueConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AtomixQueueConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
block|}
end_class

end_unit

