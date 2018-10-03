begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|impl
operator|.
name|DefaultInjector
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
name|spi
operator|.
name|Injector
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
DECL|class|InjectorDefaultsTest
specifier|public
class|class
name|InjectorDefaultsTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInjectorIsDefaultByDefault ()
specifier|public
name|void
name|testInjectorIsDefaultByDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|context
operator|.
name|getInjector
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|DefaultInjector
operator|.
name|class
argument_list|,
name|injector
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNewInstance ()
specifier|public
name|void
name|testNewInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|context
operator|.
name|getInjector
argument_list|()
decl_stmt|;
name|MyFoo
name|foo
init|=
name|injector
operator|.
name|newInstance
argument_list|(
name|MyFoo
operator|.
name|class
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|MyFoo
name|foo2
init|=
name|injector
operator|.
name|newInstance
argument_list|(
name|MyFoo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|foo2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|foo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|foo2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

