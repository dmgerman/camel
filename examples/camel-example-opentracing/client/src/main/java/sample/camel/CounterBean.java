begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.camel
package|package
name|sample
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
annotation|@
name|Named
argument_list|(
literal|"counterBean"
argument_list|)
DECL|class|CounterBean
specifier|public
class|class
name|CounterBean
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
DECL|method|someMethod (String body)
specifier|public
name|String
name|someMethod
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
literal|""
operator|+
operator|++
name|counter
return|;
block|}
block|}
end_class

end_unit

