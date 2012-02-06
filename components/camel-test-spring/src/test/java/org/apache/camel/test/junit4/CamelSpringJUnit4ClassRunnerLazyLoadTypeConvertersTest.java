begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.junit4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
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
name|test
operator|.
name|spring
operator|.
name|LazyLoadTypeConverters
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_class
annotation|@
name|LazyLoadTypeConverters
argument_list|(
literal|false
argument_list|)
DECL|class|CamelSpringJUnit4ClassRunnerLazyLoadTypeConvertersTest
specifier|public
class|class
name|CamelSpringJUnit4ClassRunnerLazyLoadTypeConvertersTest
extends|extends
name|CamelSpringJUnit4ClassRunnerPlainTest
block|{
annotation|@
name|Test
annotation|@
name|Override
DECL|method|testLazyLoadTypeConverters ()
specifier|public
name|void
name|testLazyLoadTypeConverters
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|camelContext
operator|.
name|isLazyLoadTypeConverters
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|camelContext2
operator|.
name|isLazyLoadTypeConverters
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

