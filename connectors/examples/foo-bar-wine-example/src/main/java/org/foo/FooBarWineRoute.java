begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.foo
package|package
name|org
operator|.
name|foo
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

begin_comment
comment|/**  * Camel route that uses the foo, bar and wine connectors  */
end_comment

begin_class
DECL|class|FooBarWineRoute
specifier|public
class|class
name|FooBarWineRoute
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
name|from
argument_list|(
literal|"foo:ThirstyBear?period=2000"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Who is this: ${header.whoami}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"wine:Wine?amount=2"
argument_list|)
operator|.
name|log
argument_list|(
literal|"ThirstyBear ordered ${body}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"foo:Moes?period=5000"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Who is this: ${header.whoami}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bar:Beer?amount=5&celebrity=true"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Moes ordered ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

