begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|direct
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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|ExchangePattern
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
name|DefaultConsumer
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
name|impl
operator|.
name|DefaultExchange
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
name|DefaultProducer
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
name|converter
operator|.
name|AsyncProcessorTypeConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Represents a direct endpoint that synchronously invokes the consumers of the  * endpoint when a producer sends a message to it.  *   * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|DirectEndpoint
specifier|public
class|class
name|DirectEndpoint
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|DefaultEndpoint
argument_list|<
name|E
argument_list|>
block|{
DECL|class|DirectProducer
specifier|private
specifier|final
class|class
name|DirectProducer
extends|extends
name|DefaultProducer
implements|implements
name|AsyncProcessor
block|{
DECL|method|DirectProducer (Endpoint endpoint)
specifier|private
name|DirectProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No consumers available on "
operator|+
name|this
operator|+
literal|" for "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|DefaultConsumer
argument_list|<
name|E
argument_list|>
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|int
name|size
init|=
name|consumers
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No consumers available on "
operator|+
name|this
operator|+
literal|" for "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|size
operator|>
literal|1
condition|)
block|{
comment|// Too hard to do multiple async.. do it sync
try|try
block|{
for|for
control|(
name|DefaultConsumer
argument_list|<
name|E
argument_list|>
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|error
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|DefaultConsumer
argument_list|<
name|E
argument_list|>
name|consumer
range|:
name|consumers
control|)
block|{
name|AsyncProcessor
name|processor
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|consumer
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DirectEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|allowMultipleConsumers
name|boolean
name|allowMultipleConsumers
init|=
literal|true
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|CopyOnWriteArrayList
argument_list|<
name|DefaultConsumer
argument_list|<
name|E
argument_list|>
argument_list|>
name|consumers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|DefaultConsumer
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|DirectEndpoint (String uri, DirectComponent<E> component)
specifier|public
name|DirectEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|DirectComponent
argument_list|<
name|E
argument_list|>
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
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
name|DirectProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|E
argument_list|>
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
name|DefaultConsumer
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|allowMultipleConsumers
operator|&&
operator|!
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Endpoint "
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|" only allows 1 active consumer but you attempted to start a 2nd consumer."
argument_list|)
throw|;
block|}
name|consumers
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
name|consumers
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|isAllowMultipleConsumers ()
specifier|public
name|boolean
name|isAllowMultipleConsumers
parameter_list|()
block|{
return|return
name|allowMultipleConsumers
return|;
block|}
DECL|method|setAllowMultipleConsumers (boolean allowMutlipleConsumers)
specifier|public
name|void
name|setAllowMultipleConsumers
parameter_list|(
name|boolean
name|allowMutlipleConsumers
parameter_list|)
block|{
name|this
operator|.
name|allowMultipleConsumers
operator|=
name|allowMutlipleConsumers
expr_stmt|;
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
block|}
end_class

end_unit

