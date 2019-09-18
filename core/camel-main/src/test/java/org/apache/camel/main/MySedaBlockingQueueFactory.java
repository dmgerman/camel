begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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
name|component
operator|.
name|seda
operator|.
name|BlockingQueueFactory
import|;
end_import

begin_class
DECL|class|MySedaBlockingQueueFactory
specifier|public
class|class
name|MySedaBlockingQueueFactory
implements|implements
name|BlockingQueueFactory
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
DECL|method|getCounter ()
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
return|;
block|}
DECL|method|setCounter (int counter)
specifier|public
name|void
name|setCounter
parameter_list|(
name|int
name|counter
parameter_list|)
block|{
name|this
operator|.
name|counter
operator|=
name|counter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|BlockingQueue
name|create
parameter_list|()
block|{
comment|// just return null
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|create (int capacity)
specifier|public
name|BlockingQueue
name|create
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
comment|// just return null
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

