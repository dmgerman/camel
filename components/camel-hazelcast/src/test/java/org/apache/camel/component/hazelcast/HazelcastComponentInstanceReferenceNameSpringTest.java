begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
DECL|class|HazelcastComponentInstanceReferenceNameSpringTest
specifier|public
class|class
name|HazelcastComponentInstanceReferenceNameSpringTest
extends|extends
name|HazelcastCamelSpringTestSupport
block|{
DECL|field|TEST_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VALUE
init|=
literal|"TestValue"
decl_stmt|;
DECL|field|TEST_KEY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_KEY
init|=
literal|"TestKey"
decl_stmt|;
annotation|@
name|Test
DECL|method|testComparePutAndGet ()
specifier|public
name|void
name|testComparePutAndGet
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:testHazelcastInstanceBeanRefPut"
argument_list|,
name|TEST_VALUE
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
name|TEST_KEY
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:testHazelcastInstanceBeanRefGet"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
name|TEST_KEY
argument_list|)
expr_stmt|;
specifier|final
name|Object
name|testValueReturn
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:out"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TEST_VALUE
argument_list|,
name|testValueReturn
argument_list|)
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
literal|"/META-INF/spring/test-camel-context-hazelcast-instance-name-reference.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

