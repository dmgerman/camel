begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|management
package|;
end_package

begin_comment
comment|/**  * For gathering basic statistics  */
end_comment

begin_interface
DECL|interface|Statistic
specifier|public
interface|interface
name|Statistic
block|{
comment|/**      * Statistics mode      *<ul>      *<li>VALUE - A statistic with this update mode is a simple value that is a straight forward      * representation of the updated value.</li>      *<li>DIFFERENCE - A statistic with this update mode is a value that represents the difference      * between the last two recorded values (or the initial value if two updates have      * not been recorded).</li>      *<li>COUNTER - A statistic with this update mode interprets updates as increments (positive values)      * or decrements (negative values) to the current value.</li>      *<li>MAXIMUM - A statistic with this update mode is a value that represents the maximum value      * amongst the update values applied to this statistic.</li>      *<li>MINIMUM - A statistic with this update mode is a value that represents the minimum value      * amongst the update values applied to this statistic.</li>      *<ul>      */
DECL|enum|UpdateMode
specifier|public
enum|enum
name|UpdateMode
block|{
DECL|enumConstant|VALUE
DECL|enumConstant|DIFFERENCE
DECL|enumConstant|COUNTER
DECL|enumConstant|MAXIMUM
DECL|enumConstant|MINIMUM
name|VALUE
block|,
name|DIFFERENCE
block|,
name|COUNTER
block|,
name|MAXIMUM
block|,
name|MINIMUM
block|}
comment|/**      * Shorthand for updateValue(1).      */
DECL|method|increment ()
name|void
name|increment
parameter_list|()
function_decl|;
comment|/**      * Update statistic value. The update will be applied according to the      * {@link UpdateMode}      *      * @param value the value      */
DECL|method|updateValue (long value)
name|void
name|updateValue
parameter_list|(
name|long
name|value
parameter_list|)
function_decl|;
comment|/**      * Gets the current value of the statistic since the last reset.      *      * @return the value      */
DECL|method|getValue ()
name|long
name|getValue
parameter_list|()
function_decl|;
comment|/**      * Gets the number of times the statistic has been updated since the last reset.      *      * @return the update count      */
DECL|method|getUpdateCount ()
name|long
name|getUpdateCount
parameter_list|()
function_decl|;
comment|/**      * Resets the statistic's value and update count to zero.      */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

