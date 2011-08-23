begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|spi
operator|.
name|management
operator|.
name|Statistic
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link Statistic}  */
end_comment

begin_class
DECL|class|DefaultStatistic
specifier|public
class|class
name|DefaultStatistic
implements|implements
name|Statistic
block|{
DECL|field|updateMode
specifier|private
specifier|final
name|Statistic
operator|.
name|UpdateMode
name|updateMode
decl_stmt|;
DECL|field|value
specifier|private
name|long
name|value
decl_stmt|;
DECL|field|updateCount
specifier|private
name|long
name|updateCount
decl_stmt|;
comment|/**      * Instantiates a new statistic.      *      * @param updateMode The statistic update mode.      */
DECL|method|DefaultStatistic (Statistic.UpdateMode updateMode)
specifier|public
name|DefaultStatistic
parameter_list|(
name|Statistic
operator|.
name|UpdateMode
name|updateMode
parameter_list|)
block|{
name|this
operator|.
name|updateMode
operator|=
name|updateMode
expr_stmt|;
block|}
DECL|method|updateValue (long newValue)
specifier|public
specifier|synchronized
name|void
name|updateValue
parameter_list|(
name|long
name|newValue
parameter_list|)
block|{
switch|switch
condition|(
name|this
operator|.
name|updateMode
condition|)
block|{
case|case
name|COUNTER
case|:
name|this
operator|.
name|value
operator|+=
name|newValue
expr_stmt|;
break|break;
case|case
name|VALUE
case|:
name|this
operator|.
name|value
operator|=
name|newValue
expr_stmt|;
break|break;
case|case
name|DIFFERENCE
case|:
name|this
operator|.
name|value
operator|-=
name|newValue
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|value
operator|<
literal|0
condition|)
block|{
name|this
operator|.
name|value
operator|=
operator|-
name|this
operator|.
name|value
expr_stmt|;
block|}
break|break;
case|case
name|MAXIMUM
case|:
comment|// initialize value at first time
if|if
condition|(
name|this
operator|.
name|updateCount
operator|==
literal|0
operator|||
name|this
operator|.
name|value
operator|<
name|newValue
condition|)
block|{
name|this
operator|.
name|value
operator|=
name|newValue
expr_stmt|;
block|}
break|break;
case|case
name|MINIMUM
case|:
comment|// initialize value at first time
if|if
condition|(
name|this
operator|.
name|updateCount
operator|==
literal|0
operator|||
name|this
operator|.
name|value
operator|>
name|newValue
condition|)
block|{
name|this
operator|.
name|value
operator|=
name|newValue
expr_stmt|;
block|}
break|break;
default|default:
block|}
name|this
operator|.
name|updateCount
operator|++
expr_stmt|;
block|}
DECL|method|increment ()
specifier|public
specifier|synchronized
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
DECL|method|getValue ()
specifier|public
specifier|synchronized
name|long
name|getValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|value
return|;
block|}
DECL|method|getUpdateCount ()
specifier|public
specifier|synchronized
name|long
name|getUpdateCount
parameter_list|()
block|{
return|return
name|this
operator|.
name|updateCount
return|;
block|}
DECL|method|reset ()
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
block|{
name|this
operator|.
name|value
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|updateCount
operator|=
literal|0
expr_stmt|;
block|}
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
return|;
block|}
block|}
end_class

end_unit

