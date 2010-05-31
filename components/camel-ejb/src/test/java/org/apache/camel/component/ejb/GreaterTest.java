begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ejb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ejb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|GreaterTest
specifier|public
class|class
name|GreaterTest
extends|extends
name|TestCase
block|{
DECL|field|initialContext
specifier|private
name|InitialContext
name|initialContext
decl_stmt|;
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
literal|"org.apache.openejb.client.LocalInitialContextFactory"
argument_list|)
expr_stmt|;
name|initialContext
operator|=
operator|new
name|InitialContext
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
DECL|method|testGreaterViaLocalInterface ()
specifier|public
name|void
name|testGreaterViaLocalInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|object
init|=
name|initialContext
operator|.
name|lookup
argument_list|(
literal|"GreaterImplLocal"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|GreaterLocal
argument_list|)
expr_stmt|;
name|GreaterLocal
name|greater
init|=
operator|(
name|GreaterLocal
operator|)
name|object
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|greater
operator|.
name|hello
argument_list|(
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

