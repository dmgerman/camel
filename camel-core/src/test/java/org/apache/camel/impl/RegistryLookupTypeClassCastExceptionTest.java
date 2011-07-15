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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|NoSuchBeanException
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|RegistryLookupTypeClassCastExceptionTest
specifier|public
class|class
name|RegistryLookupTypeClassCastExceptionTest
extends|extends
name|TestCase
block|{
DECL|method|testLookupOk ()
specifier|public
name|void
name|testLookupOk
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|simple
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|MyClass
name|my
init|=
operator|new
name|MyClass
argument_list|()
decl_stmt|;
name|simple
operator|.
name|put
argument_list|(
literal|"my"
argument_list|,
name|my
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|my
argument_list|,
name|simple
operator|.
name|lookup
argument_list|(
literal|"my"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|my
argument_list|,
name|simple
operator|.
name|lookup
argument_list|(
literal|"my"
argument_list|,
name|MyClass
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|simple
operator|.
name|lookup
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|simple
operator|.
name|lookup
argument_list|(
literal|"foo"
argument_list|,
name|MyClass
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCamelContextLookupOk ()
specifier|public
name|void
name|testCamelContextLookupOk
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|simple
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|simple
argument_list|)
decl_stmt|;
name|MyClass
name|my
init|=
operator|new
name|MyClass
argument_list|()
decl_stmt|;
name|simple
operator|.
name|put
argument_list|(
literal|"my"
argument_list|,
name|my
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|my
argument_list|,
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"my"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|my
argument_list|,
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"my"
argument_list|,
name|MyClass
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"foo"
argument_list|,
name|MyClass
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLookupClassCast ()
specifier|public
name|void
name|testLookupClassCast
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|simple
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|MyClass
name|my
init|=
operator|new
name|MyClass
argument_list|()
decl_stmt|;
name|simple
operator|.
name|put
argument_list|(
literal|"my"
argument_list|,
name|my
argument_list|)
expr_stmt|;
try|try
block|{
name|simple
operator|.
name|lookup
argument_list|(
literal|"my"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"my"
argument_list|,
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"expected type was: class java.lang.String"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCamelContextLookupClassCast ()
specifier|public
name|void
name|testCamelContextLookupClassCast
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|simple
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|simple
argument_list|)
decl_stmt|;
name|MyClass
name|my
init|=
operator|new
name|MyClass
argument_list|()
decl_stmt|;
name|simple
operator|.
name|put
argument_list|(
literal|"my"
argument_list|,
name|my
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"my"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"my"
argument_list|,
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"expected type was: class java.lang.String"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyClass
specifier|public
specifier|static
class|class
name|MyClass
block|{
comment|// just a test class
block|}
block|}
end_class

end_unit

