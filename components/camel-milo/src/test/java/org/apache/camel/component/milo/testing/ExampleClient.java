begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.testing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|testing
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
name|milo
operator|.
name|NodeIds
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

begin_comment
comment|/**  * An example application focusing on the OPC UA client endpoint  */
end_comment

begin_class
DECL|class|ExampleClient
specifier|public
specifier|final
class|class
name|ExampleClient
block|{
DECL|method|ExampleClient ()
specifier|private
name|ExampleClient
parameter_list|()
block|{     }
DECL|method|main (final String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
comment|// camel conext
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// add routes
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// bridge item1 to item2
specifier|final
name|String
name|item1
init|=
name|NodeIds
operator|.
name|nodeValue
argument_list|(
literal|"urn:org:apache:camel"
argument_list|,
literal|"items-MyItem"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|item2
init|=
name|NodeIds
operator|.
name|nodeValue
argument_list|(
literal|"urn:org:apache:camel"
argument_list|,
literal|"items-MyItem2"
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"milo-client:tcp://foo:bar@localhost:12685?node="
operator|+
name|item1
argument_list|)
operator|.
name|log
argument_list|(
literal|"From OPC UA: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"milo-client:tcp://foo:bar@localhost:12685?node"
operator|+
name|item2
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// start
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// sleep
while|while
condition|(
literal|true
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

