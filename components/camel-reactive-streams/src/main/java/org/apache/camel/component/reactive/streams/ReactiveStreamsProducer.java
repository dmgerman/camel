begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|DefaultAsyncProducer
import|;
end_import

begin_comment
comment|/**  * The Camel reactive-streams producer.  */
end_comment

begin_class
DECL|class|ReactiveStreamsProducer
specifier|public
class|class
name|ReactiveStreamsProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|endpoint
specifier|private
name|ReactiveStreamsEndpoint
name|endpoint
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|service
specifier|private
name|CamelReactiveStreamsService
name|service
decl_stmt|;
DECL|method|ReactiveStreamsProducer (ReactiveStreamsEndpoint endpoint, String name)
specifier|public
name|ReactiveStreamsProducer
parameter_list|(
name|ReactiveStreamsEndpoint
name|endpoint
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
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
name|service
operator|.
name|process
argument_list|(
name|name
argument_list|,
name|exchange
argument_list|,
parameter_list|(
name|data
parameter_list|,
name|error
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|setException
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|this
operator|.
name|service
operator|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|service
operator|.
name|attachCamelProducer
argument_list|(
name|endpoint
operator|.
name|getStream
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|this
operator|.
name|service
operator|.
name|detachCamelProducer
argument_list|(
name|endpoint
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|ReactiveStreamsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

