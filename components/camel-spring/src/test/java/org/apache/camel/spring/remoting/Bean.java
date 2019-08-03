begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.remoting
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|remoting
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
name|Message
import|;
end_import

begin_class
DECL|class|Bean
specifier|public
class|class
name|Bean
block|{
DECL|method|sayHi (final BaseClass baseClass, Message message)
specifier|public
name|String
name|sayHi
parameter_list|(
specifier|final
name|BaseClass
name|baseClass
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
name|baseClass
operator|.
name|hi
argument_list|()
return|;
block|}
DECL|class|BaseClass
specifier|public
specifier|static
class|class
name|BaseClass
block|{
DECL|method|hi ()
specifier|public
name|String
name|hi
parameter_list|()
block|{
return|return
literal|"Hello from Base"
return|;
block|}
block|}
DECL|class|SubClass
specifier|public
specifier|static
class|class
name|SubClass
extends|extends
name|BaseClass
block|{
annotation|@
name|Override
DECL|method|hi ()
specifier|public
name|String
name|hi
parameter_list|()
block|{
return|return
literal|"Hello from Sub"
return|;
block|}
block|}
block|}
end_class

end_unit

