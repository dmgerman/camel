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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
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
name|Language
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
name|util
operator|.
name|jndi
operator|.
name|JndiTest
import|;
end_import

begin_class
DECL|class|JndiRegistryTest
specifier|public
class|class
name|JndiRegistryTest
extends|extends
name|TestCase
block|{
DECL|method|testLookupByType ()
specifier|public
name|void
name|testLookupByType
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
operator|new
name|JndiRegistry
argument_list|(
name|JndiTest
operator|.
name|createInitialContext
argument_list|()
argument_list|)
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|SimpleLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"bar"
argument_list|,
literal|"Hello bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello bar"
argument_list|,
name|jndi
operator|.
name|lookup
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello bar"
argument_list|,
name|jndi
operator|.
name|lookupByName
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello bar"
argument_list|,
name|jndi
operator|.
name|lookupByNameAndType
argument_list|(
literal|"bar"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|jndi
operator|.
name|lookup
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|jndi
operator|.
name|lookupByName
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|assertNull
argument_list|(
name|jndi
operator|.
name|lookupByNameAndType
argument_list|(
literal|"bar"
argument_list|,
name|Language
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertNotNull
argument_list|(
name|jndi
operator|.
name|lookupByNameAndType
argument_list|(
literal|"foo"
argument_list|,
name|Language
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|jndi
operator|.
name|lookupByNameAndType
argument_list|(
literal|"foo"
argument_list|,
name|SimpleLanguage
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|jndi
operator|.
name|lookupByNameAndType
argument_list|(
literal|"foo"
argument_list|,
name|Language
operator|.
name|class
argument_list|)
argument_list|,
name|jndi
operator|.
name|lookupByNameAndType
argument_list|(
literal|"foo"
argument_list|,
name|SimpleLanguage
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|set
init|=
name|jndi
operator|.
name|lookupByType
argument_list|(
name|Language
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|set
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|jndi
operator|.
name|lookupByName
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
name|set
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefault ()
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
operator|new
name|JndiRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"bar"
argument_list|,
literal|"Hello bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello bar"
argument_list|,
name|jndi
operator|.
name|lookup
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMap ()
specifier|public
name|void
name|testMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|env
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.factory.initial"
argument_list|,
literal|"org.apache.camel.util.jndi.CamelInitialContextFactory"
argument_list|)
expr_stmt|;
name|JndiRegistry
name|jndi
init|=
operator|new
name|JndiRegistry
argument_list|(
name|env
argument_list|)
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"bar"
argument_list|,
literal|"Hello bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello bar"
argument_list|,
name|jndi
operator|.
name|lookup
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

