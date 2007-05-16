begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|Date
import|;
end_import

begin_comment
comment|/**  * A helper class for working with times in various units  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|Time
specifier|public
class|class
name|Time
block|{
DECL|field|number
specifier|private
name|long
name|number
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
DECL|method|millis (long value)
specifier|public
specifier|static
name|Time
name|millis
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
operator|new
name|Time
argument_list|(
name|value
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
DECL|method|micros (long value)
specifier|public
specifier|static
name|Time
name|micros
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
operator|new
name|Time
argument_list|(
name|value
argument_list|,
name|TimeUnit
operator|.
name|MICROSECONDS
argument_list|)
return|;
block|}
DECL|method|nanos (long value)
specifier|public
specifier|static
name|Time
name|nanos
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
operator|new
name|Time
argument_list|(
name|value
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
DECL|method|seconds (long value)
specifier|public
specifier|static
name|Time
name|seconds
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
operator|new
name|Time
argument_list|(
name|value
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
return|;
block|}
DECL|method|minutes (long value)
specifier|public
specifier|static
name|Time
name|minutes
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
operator|new
name|Time
argument_list|(
name|minutesAsSeconds
argument_list|(
name|value
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
DECL|method|hours (long value)
specifier|public
specifier|static
name|Time
name|hours
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
operator|new
name|Time
argument_list|(
name|hoursAsSeconds
argument_list|(
name|value
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
DECL|method|days (long value)
specifier|public
specifier|static
name|Time
name|days
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
operator|new
name|Time
argument_list|(
name|daysAsSeconds
argument_list|(
name|value
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
DECL|method|Time (long number, TimeUnit timeUnit)
specifier|public
name|Time
parameter_list|(
name|long
name|number
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
name|this
operator|.
name|timeUnit
operator|=
name|timeUnit
expr_stmt|;
block|}
DECL|method|toMillis ()
specifier|public
name|long
name|toMillis
parameter_list|()
block|{
return|return
name|timeUnit
operator|.
name|toMillis
argument_list|(
name|number
argument_list|)
return|;
block|}
DECL|method|toDate ()
specifier|public
name|Date
name|toDate
parameter_list|()
block|{
return|return
operator|new
name|Date
argument_list|(
name|toMillis
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getNumber ()
specifier|public
name|long
name|getNumber
parameter_list|()
block|{
return|return
name|number
return|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|timeUnit
return|;
block|}
DECL|method|minutesAsSeconds (long value)
specifier|protected
specifier|static
name|long
name|minutesAsSeconds
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
name|value
operator|*
literal|60
return|;
block|}
DECL|method|hoursAsSeconds (long value)
specifier|protected
specifier|static
name|long
name|hoursAsSeconds
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
name|minutesAsSeconds
argument_list|(
name|value
argument_list|)
operator|*
literal|60
return|;
block|}
DECL|method|daysAsSeconds (long value)
specifier|protected
specifier|static
name|long
name|daysAsSeconds
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
name|hoursAsSeconds
argument_list|(
name|value
argument_list|)
operator|*
literal|24
return|;
block|}
block|}
end_class

end_unit

