begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_class
DECL|class|StatisticValue
specifier|public
class|class
name|StatisticValue
extends|extends
name|Statistic
block|{
DECL|field|value
specifier|private
specifier|final
name|AtomicLong
name|value
init|=
operator|new
name|AtomicLong
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
DECL|method|updateValue (long newValue)
specifier|public
name|void
name|updateValue
parameter_list|(
name|long
name|newValue
parameter_list|)
block|{
name|value
operator|.
name|set
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|long
name|getValue
parameter_list|()
block|{
return|return
name|value
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|""
operator|+
name|value
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isUpdated ()
specifier|public
name|boolean
name|isUpdated
parameter_list|()
block|{
return|return
name|value
operator|.
name|get
argument_list|()
operator|!=
operator|-
literal|1
return|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|value
operator|.
name|set
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

