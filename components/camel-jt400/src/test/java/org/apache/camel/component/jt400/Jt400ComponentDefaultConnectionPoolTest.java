begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
package|;
end_package

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400ConnectionPool
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
name|Assert
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

begin_class
DECL|class|Jt400ComponentDefaultConnectionPoolTest
specifier|public
class|class
name|Jt400ComponentDefaultConnectionPoolTest
extends|extends
name|Jt400TestSupport
block|{
DECL|field|component
specifier|private
name|Jt400Component
name|component
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|component
operator|=
operator|new
name|Jt400Component
argument_list|()
expr_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Use an invalid object type so that endpoints are never created
comment|// and actual connections are never requested
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"jt400://user:password@host/qsys.lib/library.lib/program.xxx"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* Expected */
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
name|component
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testDefaultConnectionPoolIsCreated ()
specifier|public
name|void
name|testDefaultConnectionPoolIsCreated
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|component
operator|.
name|getConnectionPool
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Note: white-box testing.      */
annotation|@
name|Test
DECL|method|testDefaultConnectionPoolIsOfExpectedType ()
specifier|public
name|void
name|testDefaultConnectionPoolIsOfExpectedType
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|AS400ConnectionPool
operator|.
name|class
argument_list|,
name|component
operator|.
name|getConnectionPool
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

