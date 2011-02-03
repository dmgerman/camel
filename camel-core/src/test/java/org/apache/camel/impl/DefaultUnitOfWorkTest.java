begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CamelContext
import|;
end_import

begin_class
DECL|class|DefaultUnitOfWorkTest
specifier|public
class|class
name|DefaultUnitOfWorkTest
extends|extends
name|TestCase
block|{
DECL|field|unitOfWork
specifier|private
name|DefaultUnitOfWork
name|unitOfWork
decl_stmt|;
DECL|method|setUp ()
specifier|protected
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
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setUuidGenerator
argument_list|(
operator|new
name|SimpleUuidGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|unitOfWork
operator|=
operator|new
name|DefaultUnitOfWork
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetId ()
specifier|public
name|void
name|testGetId
parameter_list|()
block|{
name|String
name|id
init|=
name|unitOfWork
operator|.
name|getId
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|unitOfWork
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

