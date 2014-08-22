begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
comment|/**  * TODO Add Class documentation for ConnectionFactoryResourceTest  */
end_comment

begin_class
DECL|class|ConnectionFactoryResourceTest
specifier|public
class|class
name|ConnectionFactoryResourceTest
block|{
DECL|field|connectionFactory
specifier|private
name|ActiveMQConnectionFactory
name|connectionFactory
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|connectionFactory
operator|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker?broker.persistent=false&broker.useJmx=false"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|teardown ()
specifier|public
name|void
name|teardown
parameter_list|()
block|{
name|connectionFactory
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Test method for      * {@link org.apache.camel.component.sjms.jms.ConnectionFactoryResource#makeObject()}      * .      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testCreateObject ()
specifier|public
name|void
name|testCreateObject
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionFactoryResource
name|pool
init|=
operator|new
name|ConnectionFactoryResource
argument_list|(
literal|1
argument_list|,
name|connectionFactory
argument_list|)
decl_stmt|;
name|pool
operator|.
name|fillPool
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|ActiveMQConnection
name|connection
init|=
operator|(
name|ActiveMQConnection
operator|)
name|pool
operator|.
name|makeObject
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|connection
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|drainPool
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test method for      * {@link org.apache.camel.component.sjms.jms.ConnectionFactoryResource#destroyObject()}      * .      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testDestroyObject ()
specifier|public
name|void
name|testDestroyObject
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionFactoryResource
name|pool
init|=
operator|new
name|ConnectionFactoryResource
argument_list|(
literal|1
argument_list|,
name|connectionFactory
argument_list|)
decl_stmt|;
name|pool
operator|.
name|fillPool
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|ActiveMQConnection
name|connection
init|=
operator|(
name|ActiveMQConnection
operator|)
name|pool
operator|.
name|makeObject
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|connection
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|drainPool
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|pool
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for      * {@link org.apache.camel.component.sjms.jms.ConnectionResource#borrowConnection()}.      *       * @throws Exception      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NoSuchElementException
operator|.
name|class
argument_list|)
DECL|method|testBorrowObject ()
specifier|public
name|void
name|testBorrowObject
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionFactoryResource
name|pool
init|=
operator|new
name|ConnectionFactoryResource
argument_list|(
literal|1
argument_list|,
name|connectionFactory
argument_list|)
decl_stmt|;
name|pool
operator|.
name|fillPool
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|ActiveMQConnection
name|connection
init|=
operator|(
name|ActiveMQConnection
operator|)
name|pool
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|connection
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|borrowConnection
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test method for      * {@link org.apache.camel.component.sjms.jms.ConnectionResource#returnConnection(java.lang.Object)}      * .      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testReturnObject ()
specifier|public
name|void
name|testReturnObject
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionFactoryResource
name|pool
init|=
operator|new
name|ConnectionFactoryResource
argument_list|(
literal|1
argument_list|,
name|connectionFactory
argument_list|)
decl_stmt|;
name|pool
operator|.
name|fillPool
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|ActiveMQConnection
name|connection
init|=
operator|(
name|ActiveMQConnection
operator|)
name|pool
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|connection
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|returnConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|ActiveMQConnection
name|connection2
init|=
operator|(
name|ActiveMQConnection
operator|)
name|pool
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|connection2
argument_list|)
expr_stmt|;
name|pool
operator|.
name|drainPool
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

