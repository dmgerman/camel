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
name|Date
import|;
end_import

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
name|ContextTestSupport
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
name|Endpoint
import|;
end_import

begin_comment
comment|/**  * Unit test for helper methods on the DefaultComponent.  */
end_comment

begin_class
DECL|class|DefaultComponentTest
specifier|public
class|class
name|DefaultComponentTest
extends|extends
name|ContextTestSupport
block|{
DECL|class|MyComponent
specifier|private
specifier|final
class|class
name|MyComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|MyComponent (CamelContext context)
specifier|private
name|MyComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|testGetAndRemoveParameterEmptyMap ()
specifier|public
name|void
name|testGetAndRemoveParameterEmptyMap
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Integer
name|value
init|=
name|my
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetAndRemoveParameterEmptyMapDefault ()
specifier|public
name|void
name|testGetAndRemoveParameterEmptyMapDefault
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Integer
name|value
init|=
name|my
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
operator|.
name|intValue
argument_list|()
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetAndRemoveParameterEmptyMapDefaultIsNull ()
specifier|public
name|void
name|testGetAndRemoveParameterEmptyMapDefaultIsNull
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Integer
name|value
init|=
name|my
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetAndRemoveParameterToInteger ()
specifier|public
name|void
name|testGetAndRemoveParameterToInteger
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"size"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Integer
name|value
init|=
name|my
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
operator|.
name|intValue
argument_list|()
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetAndRemoveParameterToIntegerDefault ()
specifier|public
name|void
name|testGetAndRemoveParameterToIntegerDefault
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"size"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Integer
name|value
init|=
name|my
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"level"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
operator|.
name|intValue
argument_list|()
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
DECL|method|testResolveAndRemoveReferenceParameter ()
specifier|public
name|void
name|testResolveAndRemoveReferenceParameter
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
literal|"#beginning"
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Date
name|value
init|=
name|my
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"date"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testResolveAndRemoveReferenceParameterNotInRegistryDefault ()
specifier|public
name|void
name|testResolveAndRemoveReferenceParameterNotInRegistryDefault
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
literal|"#somewhen"
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Date
name|value
init|=
name|my
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"date"
argument_list|,
name|Date
operator|.
name|class
argument_list|,
operator|new
name|Date
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
literal|1
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testResolveAndRemoveReferenceParameterNotInRegistryNull ()
specifier|public
name|void
name|testResolveAndRemoveReferenceParameterNotInRegistryNull
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
literal|"#somewhen"
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Date
name|value
init|=
name|my
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"date"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testResolveAndRemoveReferenceParameterNotInMapDefault ()
specifier|public
name|void
name|testResolveAndRemoveReferenceParameterNotInMapDefault
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
literal|"#beginning"
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Date
name|value
init|=
name|my
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"wrong"
argument_list|,
name|Date
operator|.
name|class
argument_list|,
operator|new
name|Date
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
literal|1
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testResolveAndRemoveReferenceParameterNotInMapNull ()
specifier|public
name|void
name|testResolveAndRemoveReferenceParameterNotInMapNull
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
literal|"#beginning"
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Date
name|value
init|=
name|my
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"wrong"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testResolveAndRemoveInvalidReferenceParameter ()
specifier|public
name|void
name|testResolveAndRemoveInvalidReferenceParameter
parameter_list|()
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
literal|"beginning"
argument_list|)
expr_stmt|;
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|my
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"date"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"usage of invalid reference"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// test passed
block|}
block|}
DECL|method|testContextShouldBeSet ()
specifier|public
name|void
name|testContextShouldBeSet
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|my
init|=
operator|new
name|MyComponent
argument_list|(
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|my
operator|.
name|start
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"camelContext must be specified"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndiRegistry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndiRegistry
operator|.
name|bind
argument_list|(
literal|"beginning"
argument_list|,
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|jndiRegistry
return|;
block|}
block|}
end_class

end_unit

