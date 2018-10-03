begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * A helper class for working with times in various units  */
end_comment

begin_class
DECL|class|Time
specifier|public
class|class
name|Time
block|{
DECL|field|number
specifier|private
specifier|final
name|long
name|number
decl_stmt|;
DECL|field|timeUnit
specifier|private
specifier|final
name|TimeUnit
name|timeUnit
decl_stmt|;
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|number
operator|+
literal|" "
operator|+
name|timeUnit
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
return|;
block|}
block|}
end_class

end_unit

