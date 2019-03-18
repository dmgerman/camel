begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|jcr
operator|.
name|observation
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|observation
operator|.
name|EventIterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|observation
operator|.
name|EventListener
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
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A JCR {@link EventListener} which can be used to delegate processing to a  * Camel endpoint.  */
end_comment

begin_class
DECL|class|EndpointEventListener
specifier|public
class|class
name|EndpointEventListener
implements|implements
name|EventListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EndpointEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JcrEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|method|EndpointEventListener (JcrEndpoint endpoint, Processor processor)
specifier|public
name|EndpointEventListener
parameter_list|(
name|JcrEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|onEvent (EventIterator events)
specifier|public
name|void
name|onEvent
parameter_list|(
name|EventIterator
name|events
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onEvent START"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} consumer received JCR events: {}"
argument_list|,
name|endpoint
argument_list|,
name|events
argument_list|)
expr_stmt|;
name|RuntimeCamelException
name|rce
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|events
argument_list|)
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processor, {}, is processing exchange, {}"
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|rce
operator|=
name|exchange
operator|.
name|getException
argument_list|(
name|RuntimeCamelException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|rce
operator|=
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rce
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onEvent END throwing exception: {}"
argument_list|,
name|rce
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|rce
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"onEvent END"
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchange (EventIterator events)
specifier|private
name|Exchange
name|createExchange
parameter_list|(
name|EventIterator
name|events
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Event
argument_list|>
name|eventList
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|events
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|events
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|eventList
operator|.
name|add
argument_list|(
name|events
operator|.
name|nextEvent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|eventList
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

