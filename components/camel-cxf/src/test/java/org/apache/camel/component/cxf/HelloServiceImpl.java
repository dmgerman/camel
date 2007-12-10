begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_class
DECL|class|HelloServiceImpl
specifier|public
class|class
name|HelloServiceImpl
implements|implements
name|HelloService
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|HelloServiceImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|invocationCount
specifier|private
name|int
name|invocationCount
decl_stmt|;
DECL|method|echo (String text)
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"call for echo with "
operator|+
name|text
argument_list|)
expr_stmt|;
return|return
name|text
return|;
block|}
DECL|method|ping ()
specifier|public
name|void
name|ping
parameter_list|()
block|{
name|invocationCount
operator|++
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"call for oneway ping"
argument_list|)
expr_stmt|;
block|}
DECL|method|getInvocationCount ()
specifier|public
name|int
name|getInvocationCount
parameter_list|()
block|{
return|return
name|invocationCount
return|;
block|}
DECL|method|sayHello ()
specifier|public
name|String
name|sayHello
parameter_list|()
block|{
return|return
literal|"hello"
return|;
block|}
block|}
end_class

end_unit

