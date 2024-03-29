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
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|CamelTestSupport
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

begin_comment
comment|/**  * Useful base class for JT400 component unit tests. It creates a mock  * connection pool, registers it under the ID {@code "mockPool"} and releases it  * after the test runs.  */
end_comment

begin_class
DECL|class|Jt400TestSupport
specifier|public
specifier|abstract
class|class
name|Jt400TestSupport
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"mockPool"
argument_list|)
DECL|field|connectionPool
specifier|private
name|AS400ConnectionPool
name|connectionPool
decl_stmt|;
DECL|method|Jt400TestSupport ()
specifier|protected
name|Jt400TestSupport
parameter_list|()
block|{     }
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
name|connectionPool
operator|=
operator|new
name|MockAS400ConnectionPool
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|connectionPool
operator|!=
literal|null
condition|)
block|{
name|connectionPool
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the mock connection pool.      *      * @return the mock connection pool      */
DECL|method|getConnectionPool ()
specifier|public
name|AS400ConnectionPool
name|getConnectionPool
parameter_list|()
block|{
return|return
name|connectionPool
return|;
block|}
block|}
end_class

end_unit

