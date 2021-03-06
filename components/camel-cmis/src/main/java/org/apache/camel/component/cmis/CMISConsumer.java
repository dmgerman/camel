begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cmis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|support
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|client
operator|.
name|api
operator|.
name|OperationContext
import|;
end_import

begin_comment
comment|/**  * The CMIS consumer.  */
end_comment

begin_class
DECL|class|CMISConsumer
specifier|public
class|class
name|CMISConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|sessionFacadeFactory
specifier|private
specifier|final
name|CMISSessionFacadeFactory
name|sessionFacadeFactory
decl_stmt|;
DECL|field|sessionFacade
specifier|private
name|CMISSessionFacade
name|sessionFacade
decl_stmt|;
DECL|method|CMISConsumer (CMISEndpoint cmisEndpoint, Processor processor, CMISSessionFacadeFactory sessionFacadeFactory)
specifier|public
name|CMISConsumer
parameter_list|(
name|CMISEndpoint
name|cmisEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|CMISSessionFacadeFactory
name|sessionFacadeFactory
parameter_list|)
block|{
name|super
argument_list|(
name|cmisEndpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|sessionFacadeFactory
operator|=
name|sessionFacadeFactory
expr_stmt|;
name|this
operator|.
name|sessionFacade
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|CMISEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|CMISEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getSessionFacade
argument_list|()
operator|.
name|poll
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createOperationContext ()
specifier|public
name|OperationContext
name|createOperationContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getSessionFacade
argument_list|()
operator|.
name|createOperationContext
argument_list|()
return|;
block|}
DECL|method|sendExchangeWithPropsAndBody (Map<String, Object> properties, InputStream inputStream)
name|int
name|sendExchangeWithPropsAndBody
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Polling node : {}"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"cmis:name"
argument_list|)
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
DECL|method|getSessionFacade ()
specifier|private
name|CMISSessionFacade
name|getSessionFacade
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|sessionFacade
operator|==
literal|null
condition|)
block|{
name|sessionFacade
operator|=
name|sessionFacadeFactory
operator|.
name|create
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|sessionFacade
operator|.
name|initSession
argument_list|()
expr_stmt|;
block|}
return|return
name|sessionFacade
return|;
block|}
block|}
end_class

end_unit

