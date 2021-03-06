begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
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
name|NamedNode
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
name|spi
operator|.
name|Breakpoint
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
name|CamelEvent
operator|.
name|ExchangeEvent
import|;
end_import

begin_comment
comment|/**  * A support class for {@link Breakpoint} implementations to use as base class.  *<p/>  * Will be in active state.  */
end_comment

begin_class
DECL|class|BreakpointSupport
specifier|public
specifier|abstract
class|class
name|BreakpointSupport
implements|implements
name|Breakpoint
block|{
DECL|field|state
specifier|private
name|State
name|state
init|=
name|State
operator|.
name|Active
decl_stmt|;
annotation|@
name|Override
DECL|method|getState ()
specifier|public
name|State
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
annotation|@
name|Override
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|Suspended
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|activate ()
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|Active
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeProcess (Exchange exchange, Processor processor, NamedNode definition)
specifier|public
name|void
name|beforeProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|afterProcess (Exchange exchange, Processor processor, NamedNode definition, long timeTaken)
specifier|public
name|void
name|afterProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|NamedNode
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onEvent (Exchange exchange, ExchangeEvent event, NamedNode definition)
specifier|public
name|void
name|onEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ExchangeEvent
name|event
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

