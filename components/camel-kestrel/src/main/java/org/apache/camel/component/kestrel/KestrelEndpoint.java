begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kestrel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kestrel
package|;
end_package

begin_import
import|import
name|net
operator|.
name|spy
operator|.
name|memcached
operator|.
name|MemcachedClient
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * Camel endpoint for communication with a kestrel based queue.  */
end_comment

begin_class
DECL|class|KestrelEndpoint
specifier|public
class|class
name|KestrelEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|/**      * The configuration of this endpoint      */
DECL|field|configuration
specifier|private
name|KestrelConfiguration
name|configuration
decl_stmt|;
comment|/**      * The queue we are polling      */
DECL|field|queue
specifier|private
name|String
name|queue
decl_stmt|;
comment|/**      * The kestrel component itself      */
DECL|field|component
specifier|private
name|KestrelComponent
name|component
decl_stmt|;
DECL|method|KestrelEndpoint (String endPointURI, KestrelComponent component, KestrelConfiguration configuration, String queue)
specifier|public
name|KestrelEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|KestrelComponent
name|component
parameter_list|,
name|KestrelConfiguration
name|configuration
parameter_list|,
name|String
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|endPointURI
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
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|KestrelConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (KestrelConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|KestrelConfiguration
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
DECL|method|getQueue ()
specifier|public
name|String
name|getQueue
parameter_list|()
block|{
return|return
name|queue
return|;
block|}
DECL|method|setQueue (String queue)
specifier|public
name|void
name|setQueue
parameter_list|(
name|String
name|queue
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
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
name|KestrelProducer
argument_list|(
name|this
argument_list|,
name|getMemcachedClient
argument_list|()
argument_list|)
return|;
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
name|KestrelConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getMemcachedClient
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return a client to kestrel using the memcached client as configured by this endpoint      */
DECL|method|getMemcachedClient ()
specifier|private
name|MemcachedClient
name|getMemcachedClient
parameter_list|()
block|{
return|return
name|component
operator|.
name|getMemcachedClient
argument_list|(
name|configuration
argument_list|,
name|queue
argument_list|)
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
return|return
literal|false
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

