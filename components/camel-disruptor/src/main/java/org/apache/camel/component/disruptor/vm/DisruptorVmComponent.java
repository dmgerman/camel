begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
operator|.
name|vm
package|;
end_package

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
name|atomic
operator|.
name|AtomicInteger
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
name|disruptor
operator|.
name|DisruptorComponent
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
name|disruptor
operator|.
name|DisruptorReference
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
name|annotations
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a href="http://camel.apache.org/vm.html">VM components</a>  * for asynchronous SEDA exchanges on a  *<a href="https://github.com/LMAX-Exchange/disruptor">LMAX Disruptor</a> within the classloader tree containing  * the camel-disruptor.jar. i.e. to handle communicating across CamelContext instances and possibly across  * web application contexts, providing that camel-disruptor.jar is on the system classpath.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"disruptor-vm"
argument_list|)
DECL|class|DisruptorVmComponent
specifier|public
class|class
name|DisruptorVmComponent
extends|extends
name|DisruptorComponent
block|{
DECL|field|DISRUPTORS
specifier|protected
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DisruptorReference
argument_list|>
name|DISRUPTORS
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|START_COUNTER
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|START_COUNTER
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|getDisruptors ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DisruptorReference
argument_list|>
name|getDisruptors
parameter_list|()
block|{
return|return
name|DISRUPTORS
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
name|START_COUNTER
operator|.
name|incrementAndGet
argument_list|()
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
if|if
condition|(
name|START_COUNTER
operator|.
name|decrementAndGet
argument_list|()
operator|<=
literal|0
condition|)
block|{
comment|// clear queues when no more vm components in use
name|getDisruptors
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

