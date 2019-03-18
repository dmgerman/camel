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
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consume
import|;
end_import

begin_class
DECL|class|MyMultiArgumentService
specifier|public
class|class
name|MyMultiArgumentService
implements|implements
name|MyMultiArgumentServiceInterface
block|{
annotation|@
name|Override
annotation|@
name|Consume
argument_list|(
name|uri
operator|=
literal|"direct:myargs"
argument_list|)
DECL|method|doSomething (String arg1, String arg2, Long arg3)
specifier|public
name|void
name|doSomething
parameter_list|(
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|,
name|Long
name|arg3
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World 1"
argument_list|,
name|arg1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World 2"
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|3
argument_list|)
argument_list|,
name|arg3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

