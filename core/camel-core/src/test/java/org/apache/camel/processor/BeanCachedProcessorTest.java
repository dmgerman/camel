begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelExecutionException
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
name|Exchange
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
name|Processor
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
name|builder
operator|.
name|RouteBuilder
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|BeanCachedProcessorTest
specifier|public
class|class
name|BeanCachedProcessorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|context
specifier|private
name|Context
name|context
decl_stmt|;
DECL|field|registry
specifier|private
name|JndiRegistry
name|registry
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:noCache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:something?cache=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:cached"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:something?cache=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"something"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|registry
operator|.
name|getContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|testFreshBeanInContext ()
specifier|public
name|void
name|testFreshBeanInContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Just make sure the bean processor doesn't work if the cached is false
name|MyBean
name|originalInstance
init|=
name|registry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"something"
argument_list|,
name|MyBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:noCache"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|unbind
argument_list|(
literal|"something"
argument_list|)
expr_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"something"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
comment|// Make sure we can get the object from the registry
name|assertNotSame
argument_list|(
name|registry
operator|.
name|lookupByName
argument_list|(
literal|"something"
argument_list|)
argument_list|,
name|originalInstance
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:noCache"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanWithCached ()
specifier|public
name|void
name|testBeanWithCached
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Just make sure the bean processor doesn't work if the cached is false
name|MyBean
name|originalInstance
init|=
name|registry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"something"
argument_list|,
name|MyBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:cached"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|unbind
argument_list|(
literal|"something"
argument_list|)
expr_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"something"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
comment|// Make sure we can get the object from the registry
name|assertNotSame
argument_list|(
name|registry
operator|.
name|lookupByName
argument_list|(
literal|"something"
argument_list|)
argument_list|,
name|originalInstance
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:cached"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The IllegalStateException is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"IllegalStateException is expected!"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalStateException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This bean is not supported to be invoked again!"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
implements|implements
name|Processor
block|{
DECL|field|invoked
specifier|private
name|boolean
name|invoked
decl_stmt|;
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|invoked
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"This bean is not supported to be invoked again!"
argument_list|)
throw|;
block|}
else|else
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

