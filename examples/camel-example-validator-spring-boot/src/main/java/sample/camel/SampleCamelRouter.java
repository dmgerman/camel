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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * A simple Camel route that triggers from a timer and calls a bean and prints to system out.  *<p/>  * Use<tt>@Component</tt> to make Camel auto detect this route when starting.  */
end_comment

begin_class
annotation|@
name|Component
DECL|class|SampleCamelRouter
specifier|public
class|class
name|SampleCamelRouter
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"greeting"
argument_list|)
operator|.
name|withBean
argument_list|(
literal|"greetingValidator"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:hello?period={{timer.period}}"
argument_list|)
operator|.
name|outputTypeWithValidate
argument_list|(
literal|"greeting"
argument_list|)
operator|.
name|transform
argument_list|(
name|method
argument_list|(
literal|"myBean"
argument_list|,
literal|"saySomething"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"stream:out"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

