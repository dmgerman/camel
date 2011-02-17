begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|PassThroughJmsKeyFormatStrategyTest
specifier|public
class|class
name|PassThroughJmsKeyFormatStrategyTest
extends|extends
name|Assert
block|{
DECL|field|strategy
specifier|private
name|JmsKeyFormatStrategy
name|strategy
init|=
operator|new
name|PassThroughJmsKeyFormatStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testEncodeValidKeys ()
specifier|public
name|void
name|testEncodeValidKeys
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|strategy
operator|.
name|encodeKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo123bar"
argument_list|,
name|strategy
operator|.
name|encodeKey
argument_list|(
literal|"foo123bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelFileName"
argument_list|,
name|strategy
operator|.
name|encodeKey
argument_list|(
literal|"CamelFileName"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.camel.MyBean"
argument_list|,
name|strategy
operator|.
name|encodeKey
argument_list|(
literal|"org.apache.camel.MyBean"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Content-Type"
argument_list|,
name|strategy
operator|.
name|encodeKey
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"My-Header.You"
argument_list|,
name|strategy
operator|.
name|encodeKey
argument_list|(
literal|"My-Header.You"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeccodeValidKeys ()
specifier|public
name|void
name|testDeccodeValidKeys
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|strategy
operator|.
name|decodeKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo123bar"
argument_list|,
name|strategy
operator|.
name|decodeKey
argument_list|(
literal|"foo123bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelFileName"
argument_list|,
name|strategy
operator|.
name|decodeKey
argument_list|(
literal|"CamelFileName"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Content-Type"
argument_list|,
name|strategy
operator|.
name|decodeKey
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"My-Header.You"
argument_list|,
name|strategy
operator|.
name|decodeKey
argument_list|(
literal|"My-Header.You"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

