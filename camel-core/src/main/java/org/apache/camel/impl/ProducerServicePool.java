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
name|concurrent
operator|.
name|BlockingQueue
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * A service pool for {@link Producer}.  *<p/>  * For instance camel-mina and camel-ftp leverages this to allow a pool of producers so we  * can support concurrent producers in a thread safe manner.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ProducerServicePool
specifier|public
class|class
name|ProducerServicePool
extends|extends
name|DefaultServicePool
argument_list|<
name|Endpoint
argument_list|,
name|Producer
argument_list|>
block|{
DECL|method|ProducerServicePool (int capacity)
specifier|public
name|ProducerServicePool
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
DECL|method|size ()
specifier|synchronized
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|BlockingQueue
argument_list|<
name|Producer
argument_list|>
name|queue
range|:
name|pool
operator|.
name|values
argument_list|()
control|)
block|{
name|size
operator|+=
name|queue
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
block|}
end_class

end_unit

