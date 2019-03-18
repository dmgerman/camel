begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rmi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rmi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|LocateRegistry
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
DECL|class|RmiIllegalArgumentExceptionTest
specifier|public
class|class
name|RmiIllegalArgumentExceptionTest
extends|extends
name|RmiRouteTestSupport
block|{
DECL|field|created
specifier|private
name|boolean
name|created
decl_stmt|;
DECL|method|getStartPort ()
specifier|protected
name|int
name|getStartPort
parameter_list|()
block|{
return|return
literal|37502
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
if|if
condition|(
name|classPathHasSpaces
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|created
condition|)
block|{
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|created
operator|=
literal|true
expr_stmt|;
block|}
name|JndiRegistry
name|context
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"echo"
argument_list|,
operator|new
name|EchoService
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|tesIllegal ()
specifier|public
name|void
name|tesIllegal
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|classPathHasSpaces
argument_list|()
condition|)
block|{
return|return;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:echo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|RemoteException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
comment|// wrapped far down
name|IllegalArgumentException
name|iae
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Illegal"
argument_list|,
name|iae
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
comment|// setup the jmi server endpoint
name|RmiEndpoint
name|echo
init|=
operator|(
name|RmiEndpoint
operator|)
name|endpoint
argument_list|(
literal|"rmi://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/echo"
argument_list|)
decl_stmt|;
name|echo
operator|.
name|setRemoteInterfaces
argument_list|(
name|IEcho
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|echo
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:echo"
argument_list|)
expr_stmt|;
comment|// and our route where we call the server
name|from
argument_list|(
literal|"direct:echo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"rmi://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/echo?method=foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

