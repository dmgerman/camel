begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vm
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
name|component
operator|.
name|seda
operator|.
name|SedaComponent
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
name|seda
operator|.
name|SedaEndpoint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  * An implementation of the<a href="http://activemq.apache.org/camel/vm.html">VM components</a>  * for asynchronous SEDA exchanges on a {@link BlockingQueue} within the classloader tree containing  * the camel-core.jar. i.e. to handle communicating across CamelContext instances and possibly across  * web application contexts, providing that camel-core.jar is on the system classpath.  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|VmComponent
specifier|public
class|class
name|VmComponent
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|SedaComponent
argument_list|<
name|E
argument_list|>
block|{
DECL|field|queues
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|queues
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
argument_list|<
name|E
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|blockingQueue
init|=
operator|(
name|BlockingQueue
argument_list|<
name|E
argument_list|>
operator|)
name|getBlockingQueue
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
operator|new
name|SedaEndpoint
argument_list|<
name|E
argument_list|>
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|blockingQueue
argument_list|)
return|;
block|}
DECL|method|getBlockingQueue (String uri)
specifier|protected
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|getBlockingQueue
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
synchronized|synchronized
init|(
name|queues
init|)
block|{
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
name|queues
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
operator|(
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
operator|)
name|createQueue
argument_list|()
expr_stmt|;
name|queues
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
block|}
end_class

end_unit

