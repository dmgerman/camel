begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.jndi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|jndi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

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
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|TestSupport
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
name|IOHelper
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
DECL|class|JndiTest
specifier|public
class|class
name|JndiTest
extends|extends
name|TestSupport
block|{
DECL|field|context
specifier|protected
name|Context
name|context
decl_stmt|;
DECL|method|createInitialContext ()
specifier|public
specifier|static
name|Context
name|createInitialContext
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|in
init|=
name|JndiTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"jndi-example.properties"
argument_list|)
decl_stmt|;
try|try
block|{
name|assertNotNull
argument_list|(
literal|"Cannot find jndi-example.properties on the classpath!"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
operator|new
name|InitialContext
argument_list|(
operator|new
name|Hashtable
argument_list|<>
argument_list|(
name|properties
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testLookupOfSimpleName ()
specifier|public
name|void
name|testLookupOfSimpleName
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|assertLookup
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|assertLookup (String name)
specifier|protected
name|Object
name|assertLookup
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
name|Object
name|value
init|=
name|context
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found JNDI entry: "
operator|+
name|name
operator|+
literal|" in context: "
operator|+
name|context
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
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
name|context
operator|=
name|createInitialContext
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

