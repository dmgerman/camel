begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|Hazelcast
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
name|test
operator|.
name|junit4
operator|.
name|CamelSpringTestSupport
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|HazelcastAtomicnumberProducerForSpringTest
specifier|public
class|class
name|HazelcastAtomicnumberProducerForSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/META-INF/spring/test-camel-context-atomicnumber.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testSet ()
specifier|public
name|void
name|testSet
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:set"
argument_list|,
literal|4711
argument_list|)
expr_stmt|;
name|long
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4711
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:set"
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|long
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1234
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIncrement ()
specifier|public
name|void
name|testIncrement
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:set"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|long
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:increment"
argument_list|,
literal|null
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDecrement ()
specifier|public
name|void
name|testDecrement
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:set"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|long
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:decrement"
argument_list|,
literal|null
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDestroy ()
specifier|public
name|void
name|testDestroy
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:set"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:destroy"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|long
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the body is destory
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

