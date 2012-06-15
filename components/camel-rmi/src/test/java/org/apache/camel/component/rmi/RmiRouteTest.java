begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
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
name|Endpoint
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
name|component
operator|.
name|bean
operator|.
name|ProxyHelper
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
name|DefaultCamelContext
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
name|test
operator|.
name|AvailablePortFinder
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
name|JndiContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RmiRouteTest
specifier|public
class|class
name|RmiRouteTest
extends|extends
name|Assert
block|{
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|method|getPort ()
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
if|if
condition|(
name|port
operator|==
literal|0
condition|)
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|37502
argument_list|)
expr_stmt|;
block|}
return|return
name|port
return|;
block|}
annotation|@
name|Test
DECL|method|testPojoRoutes ()
specifier|public
name|void
name|testPojoRoutes
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
comment|// Boot up a local RMI registry
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// START SNIPPET: register
name|JndiContext
name|context
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"bye"
argument_list|,
operator|new
name|SayService
argument_list|(
literal|"Good Bye!"
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|// END SNIPPET: register
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|getRouteBuilder
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// START SNIPPET: invoke
name|Endpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"direct:hello"
argument_list|)
decl_stmt|;
name|ISay
name|proxy
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|ISay
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|rc
init|=
name|proxy
operator|.
name|say
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Good Bye!"
argument_list|,
name|rc
argument_list|)
expr_stmt|;
comment|// END SNIPPET: invoke
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|getRouteBuilder (final CamelContext context)
specifier|protected
name|RouteBuilder
name|getRouteBuilder
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
comment|// START SNIPPET: route
comment|// lets add simple route
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"rmi://localhost:%s/bye"
argument_list|,
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// When exposing an RMI endpoint, the interfaces it exposes must
comment|// be configured.
name|RmiEndpoint
name|bye
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
literal|"/bye"
argument_list|)
decl_stmt|;
name|bye
operator|.
name|setRemoteInterfaces
argument_list|(
name|ISay
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|bye
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:bye"
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: route
block|}
return|;
block|}
DECL|method|classPathHasSpaces ()
specifier|private
name|boolean
name|classPathHasSpaces
parameter_list|()
block|{
name|ClassLoader
name|cl
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|instanceof
name|URLClassLoader
condition|)
block|{
name|URLClassLoader
name|ucl
init|=
operator|(
name|URLClassLoader
operator|)
name|cl
decl_stmt|;
name|URL
index|[]
name|urls
init|=
name|ucl
operator|.
name|getURLs
argument_list|()
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|urls
control|)
block|{
if|if
condition|(
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|contains
argument_list|(
literal|" "
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"======================================================================="
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|" TEST Skipped: "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"   Your probably on windows.  We detected that the classpath"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"   has a space in it.  Try running maven with the following option: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"   -Dmaven.repo.local=C:\\DOCUME~1\\userid\\.m2\\repository"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"======================================================================="
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

