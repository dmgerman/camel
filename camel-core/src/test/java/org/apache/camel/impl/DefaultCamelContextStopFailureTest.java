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
name|Service
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultCamelContextStopFailureTest
specifier|public
class|class
name|DefaultCamelContextStopFailureTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|stopOrder
specifier|private
specifier|static
name|String
name|stopOrder
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|MyService
argument_list|(
literal|"A"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|MyService
argument_list|(
literal|"B"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|MyService
argument_list|(
literal|"C"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testStopWithFailure ()
specifier|public
name|void
name|testStopWithFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|stopOrder
operator|=
literal|""
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CBA"
argument_list|,
name|stopOrder
argument_list|)
expr_stmt|;
block|}
DECL|class|MyService
specifier|private
class|class
name|MyService
implements|implements
name|Service
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|fail
specifier|private
name|boolean
name|fail
decl_stmt|;
DECL|method|MyService (String name, boolean fail)
specifier|private
name|MyService
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|fail
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|fail
operator|=
name|fail
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|stopOrder
operator|=
name|stopOrder
operator|+
name|name
expr_stmt|;
if|if
condition|(
name|fail
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Fail "
operator|+
name|name
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

