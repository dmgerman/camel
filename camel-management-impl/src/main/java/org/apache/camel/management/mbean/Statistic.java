begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Base implementation of {@link Statistic}  *<p/>  * The following modes is available:  *<ul>  *<li>VALUE - A statistic with this update mode is a simple value that is a straight forward  * representation of the updated value.</li>  *<li>DELTA - A statistic with this update mode is a value that represents the delta  * between the last two recorded values (or the initial value if two updates have  * not been recorded). This value can be negative if the delta goes up or down.</li>  *<li>COUNTER - A statistic with this update mode interprets updates as increments (positive values)  * or decrements (negative values) to the current value.</li>  *<li>MAXIMUM - A statistic with this update mode is a value that represents the maximum value  * amongst the update values applied to this statistic.</li>  *<li>MINIMUM - A statistic with this update mode is a value that represents the minimum value  * amongst the update values applied to this statistic.</li>  *<ul>  * The MAXIMUM and MINIMUM modes are not 100% thread-safe as there can be a lost-update problem.  * This is on purpose because the performance overhead to ensure atomic updates costs to much  * on CPU and memory footprint. The other modes are thread-safe.  */
end_comment

begin_class
DECL|class|Statistic
specifier|public
specifier|abstract
class|class
name|Statistic
block|{
DECL|method|updateValue (long newValue)
specifier|public
specifier|abstract
name|void
name|updateValue
parameter_list|(
name|long
name|newValue
parameter_list|)
function_decl|;
DECL|method|increment ()
specifier|public
name|void
name|increment
parameter_list|()
block|{
name|updateValue
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|decrement ()
specifier|public
name|void
name|decrement
parameter_list|()
block|{
name|updateValue
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
specifier|abstract
name|long
name|getValue
parameter_list|()
function_decl|;
comment|/**      * Whether the statistic has been updated one or more times.      * Notice this is only working for value, maximum and minimum modes.      */
DECL|method|isUpdated ()
specifier|public
specifier|abstract
name|boolean
name|isUpdated
parameter_list|()
function_decl|;
DECL|method|reset ()
specifier|public
specifier|abstract
name|void
name|reset
parameter_list|()
function_decl|;
block|}
end_class

end_unit

