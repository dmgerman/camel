begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.java
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|java
package|;
end_package

begin_class
DECL|class|MyFieldMethodCallRouteBuilder
specifier|public
class|class
name|MyFieldMethodCallRouteBuilder
extends|extends
name|MyBasePortRouteBuilder
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
name|int
name|port2
init|=
name|getNextPort
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"netty-http:http://0.0.0.0:{{port}}/foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:input1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty-http:http://0.0.0.0:"
operator|+
name|port2
operator|+
literal|"/bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty-http:http://0.0.0.0:"
operator|+
name|port2
operator|+
literal|"/bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:input2"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

