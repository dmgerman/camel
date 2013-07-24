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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ShutdownableService
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * A shared {@link org.apache.camel.impl.DefaultProducerServicePool} which is used by  * {@link org.apache.camel.CamelContext} by default.  *  * @version   */
end_comment

begin_class
DECL|class|SharedProducerServicePool
specifier|public
class|class
name|SharedProducerServicePool
extends|extends
name|DefaultProducerServicePool
implements|implements
name|ShutdownableService
implements|,
name|StaticService
block|{
DECL|method|SharedProducerServicePool ()
specifier|public
name|SharedProducerServicePool
parameter_list|()
block|{     }
DECL|method|SharedProducerServicePool (int capacity)
specifier|public
name|SharedProducerServicePool
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|super
argument_list|(
name|capacity
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
comment|// only let CamelContext stop it since its shared and should
comment|// only be stopped when CamelContext stops
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// now we are shutting down then stop it, which properly stops the pool
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

