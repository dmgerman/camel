begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|BrowsableEndpoint
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a  * href="http://activemq.apache.org/camel/queue.html">Queue components</a> for  * asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SedaEndpoint
specifier|public
class|class
name|SedaEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|Exchange
argument_list|>
implements|implements
name|BrowsableEndpoint
block|{
DECL|field|queue
specifier|private
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
DECL|method|SedaEndpoint (String endpointUri, Component component, BlockingQueue<Exchange> queue)
specifier|public
name|SedaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
block|}
DECL|method|SedaEndpoint (String uri, SedaComponent component, Map parameters)
specifier|public
name|SedaEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SedaComponent
name|component
parameter_list|,
name|Map
name|parameters
parameter_list|)
block|{
name|this
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|component
operator|.
name|createQueue
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
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
name|CollectionProducer
argument_list|(
name|this
argument_list|,
name|getQueue
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
name|SedaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|getQueue ()
specifier|public
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|getQueue
parameter_list|()
block|{
return|return
name|queue
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
DECL|method|getExchanges ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|getExchanges
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|getQueue
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

