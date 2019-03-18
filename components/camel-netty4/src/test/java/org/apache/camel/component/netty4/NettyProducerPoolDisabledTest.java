begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|NettyProducerPoolDisabledTest
specifier|public
class|class
name|NettyProducerPoolDisabledTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testProducerPoolDisabled ()
specifier|public
name|void
name|testProducerPoolDisabled
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye "
operator|+
name|i
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true&producerPoolEnabled=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true"
argument_list|)
comment|// body should be a String when using textline codec
operator|.
name|validate
argument_list|(
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|regexReplaceAll
argument_list|(
literal|"Hello"
argument_list|,
literal|"Bye"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

