begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|TimeUnit
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
name|locks
operator|.
name|Condition
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
name|locks
operator|.
name|Lock
import|;
end_import

begin_comment
comment|/**  * Empty lock implementation  */
end_comment

begin_class
DECL|class|NoLock
specifier|public
class|class
name|NoLock
implements|implements
name|Lock
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|Lock
name|INSTANCE
init|=
operator|new
name|NoLock
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|lock ()
specifier|public
name|void
name|lock
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|lockInterruptibly ()
specifier|public
name|void
name|lockInterruptibly
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|tryLock ()
specifier|public
name|boolean
name|tryLock
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|tryLock (long time, TimeUnit unit)
specifier|public
name|boolean
name|tryLock
parameter_list|(
name|long
name|time
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|unlock ()
specifier|public
name|void
name|unlock
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|newCondition ()
specifier|public
name|Condition
name|newCondition
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

