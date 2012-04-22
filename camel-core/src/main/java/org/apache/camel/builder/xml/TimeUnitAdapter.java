begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlAdapter
import|;
end_import

begin_comment
comment|/**  *<code>TimeUnitAdapter</code> is a simple adapter to convert between Strings  * and instances of the {@link TimeUnit} enumeration  */
end_comment

begin_class
DECL|class|TimeUnitAdapter
specifier|public
class|class
name|TimeUnitAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|String
argument_list|,
name|TimeUnit
argument_list|>
block|{
annotation|@
name|Override
DECL|method|marshal (TimeUnit v)
specifier|public
name|String
name|marshal
parameter_list|(
name|TimeUnit
name|v
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|v
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (String v)
specifier|public
name|TimeUnit
name|unmarshal
parameter_list|(
name|String
name|v
parameter_list|)
throws|throws
name|Exception
block|{
name|TimeUnit
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|TimeUnit
operator|.
name|valueOf
argument_list|(
name|v
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

