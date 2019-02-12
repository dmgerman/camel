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

begin_class
DECL|class|GetRegistryAsTypeTest
specifier|public
class|class
name|GetRegistryAsTypeTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testDefault ()
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|JndiRegistry
name|jndi
init|=
name|context
operator|.
name|getRegistry
argument_list|(
name|JndiRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jndi
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getRegistry
argument_list|(
name|Map
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
argument_list|(
name|SimpleRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSimple ()
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
operator|new
name|SimpleRegistry
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|SimpleRegistry
name|simple
init|=
name|context
operator|.
name|getRegistry
argument_list|(
name|SimpleRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|simple
argument_list|)
expr_stmt|;
comment|// simple extends Map
name|assertNotNull
argument_list|(
name|context
operator|.
name|getRegistry
argument_list|(
name|Map
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
argument_list|(
name|JndiRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComposite ()
specifier|public
name|void
name|testComposite
parameter_list|()
throws|throws
name|Exception
block|{
name|CompositeRegistry
name|cr
init|=
operator|new
name|CompositeRegistry
argument_list|()
decl_stmt|;
name|cr
operator|.
name|addRegistry
argument_list|(
operator|new
name|SimpleRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|cr
operator|.
name|addRegistry
argument_list|(
operator|new
name|JndiRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|cr
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|CompositeRegistry
name|comp
init|=
name|context
operator|.
name|getRegistry
argument_list|(
name|CompositeRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|comp
argument_list|)
expr_stmt|;
name|SimpleRegistry
name|simple
init|=
name|context
operator|.
name|getRegistry
argument_list|(
name|SimpleRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|simple
argument_list|)
expr_stmt|;
name|JndiRegistry
name|jndi
init|=
name|context
operator|.
name|getRegistry
argument_list|(
name|JndiRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jndi
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
