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
name|Produce
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
name|ProducerTemplate
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
comment|/**  *  */
end_comment

begin_class
DECL|class|DefaultInjectorTest
specifier|public
class|class
name|DefaultInjectorTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testDefaultInjector ()
specifier|public
name|void
name|testDefaultInjector
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
comment|// use the injector (will use the default)
comment|// which should post process the bean to inject the @Produce
name|MyBean
name|bean
init|=
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|MyBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|reply
init|=
name|bean
operator|.
name|doSomething
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WorldWorld"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
annotation|@
name|Produce
argument_list|(
literal|"language:simple:${body}${body}"
argument_list|)
DECL|field|template
name|ProducerTemplate
name|template
decl_stmt|;
DECL|method|doSomething (String body)
specifier|public
name|Object
name|doSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
name|template
operator|.
name|requestBody
argument_list|(
name|body
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

