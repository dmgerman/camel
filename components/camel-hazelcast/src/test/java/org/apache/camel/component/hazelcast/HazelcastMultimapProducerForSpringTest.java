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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|MultiMap
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
name|AfterClass
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
DECL|class|HazelcastMultimapProducerForSpringTest
specifier|public
class|class
name|HazelcastMultimapProducerForSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|map
specifier|private
name|MultiMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|map
operator|=
name|Hazelcast
operator|.
name|getMultiMap
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|this
operator|.
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownClass ()
specifier|public
specifier|static
name|void
name|tearDownClass
parameter_list|()
block|{
name|Hazelcast
operator|.
name|shutdownAll
argument_list|()
expr_stmt|;
block|}
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
literal|"/META-INF/spring/test-camel-context-multimap.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testPut ()
specifier|public
name|void
name|testPut
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"my-foo"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"my-bar"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"4711"
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|map
operator|.
name|get
argument_list|(
literal|"4711"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"my-foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"my-bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveValue ()
specifier|public
name|void
name|testRemoveValue
parameter_list|()
block|{
name|map
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"4711"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:removevalue"
argument_list|,
literal|"my-foo"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"4711"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"4711"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"my-bar"
argument_list|)
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
name|map
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:out"
argument_list|,
literal|5000
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"my-foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDelete ()
specifier|public
name|void
name|testDelete
parameter_list|()
block|{
name|map
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:delete"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

