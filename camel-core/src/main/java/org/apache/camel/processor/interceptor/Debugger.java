begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMap
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
name|ArrayList
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
name|CamelContext
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
name|DefaultCamelContext
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
name|ProcessorType
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
name|InterceptStrategy
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
comment|/**  * An interceptor strategy for debugging and tracing routes  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|Debugger
specifier|public
class|class
name|Debugger
implements|implements
name|InterceptStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|Debugger
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchangeBufferSize
specifier|private
name|int
name|exchangeBufferSize
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|interceptors
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DebugInterceptor
argument_list|>
name|interceptors
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DebugInterceptor
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * A helper method to return the debugger instance for a given {@link CamelContext} if one is enabled      *      * @param context the camel context the debugger is connected to      * @return the debugger or null if none can be found      */
DECL|method|getDebugger (CamelContext context)
specifier|public
specifier|static
name|Debugger
name|getDebugger
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|defaultCamelContext
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|list
init|=
name|defaultCamelContext
operator|.
name|getInterceptStrategies
argument_list|()
decl_stmt|;
for|for
control|(
name|InterceptStrategy
name|interceptStrategy
range|:
name|list
control|)
block|{
if|if
condition|(
name|interceptStrategy
operator|instanceof
name|Debugger
condition|)
block|{
return|return
operator|(
name|Debugger
operator|)
name|interceptStrategy
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getInterceptor (String id)
specifier|public
name|DebugInterceptor
name|getInterceptor
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|interceptors
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
comment|/**      * Returns the list of exchanges sent to the given node in the DSL      */
DECL|method|getExchanges (String id)
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|getExchanges
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|DebugInterceptor
name|interceptor
init|=
name|getInterceptor
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|interceptor
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|interceptor
operator|.
name|getExchanges
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns the breakpoint object for the given node in the DSL      */
DECL|method|getBreakpoint (String id)
specifier|public
name|Breakpoint
name|getBreakpoint
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|DebugInterceptor
name|interceptor
init|=
name|getInterceptor
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|interceptor
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|interceptor
operator|.
name|getBreakpoint
argument_list|()
return|;
block|}
block|}
DECL|method|wrapProcessorInInterceptors (ProcessorType processorType, Processor target)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|ProcessorType
name|processorType
parameter_list|,
name|Processor
name|target
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|processorType
operator|.
name|idOrCreate
argument_list|()
decl_stmt|;
name|DebugInterceptor
name|interceptor
init|=
operator|new
name|DebugInterceptor
argument_list|(
name|processorType
argument_list|,
name|target
argument_list|,
name|createExchangeList
argument_list|()
argument_list|)
decl_stmt|;
name|interceptors
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|interceptor
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"adding interceptor: "
operator|+
name|interceptor
argument_list|)
expr_stmt|;
block|}
return|return
name|interceptor
return|;
block|}
DECL|method|createExchangeList ()
specifier|protected
name|List
argument_list|<
name|Exchange
argument_list|>
name|createExchangeList
parameter_list|()
block|{
if|if
condition|(
name|exchangeBufferSize
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|exchangeBufferSize
operator|>
literal|0
condition|)
block|{
comment|// TODO lets create a non blocking fixed size queue
return|return
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

