begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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
name|model
operator|.
name|ProcessorDefinition
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

begin_comment
comment|/**  * A support class for {@link Breakpoint} implementations to use as base class.  *<p/>  * Will be in active state.  *  * @version   */
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
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
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
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|afterProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition, long timeTaken)
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
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|onEvent (Exchange exchange, EventObject event, ProcessorDefinition<?> definition)
specifier|public
name|void
name|onEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

